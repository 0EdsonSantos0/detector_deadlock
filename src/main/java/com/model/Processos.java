package com.model;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import com.model.estruturaDados.ProcessoRecurso;

/*
 * funcões para ser criadas: 
 * requisitar recursos, liberar recursos, conferir tempo; 
 * um processo não pode requerir mais que o limete de recursos existentes
 */

public class Processos extends Thread{
    private int tempoRequisicao;
    private int tempoProcesso;
    private int tempoExecucao;
    private int quantidadeRecuros;
    private int marcadorRequisicao;
    private int idProcesso;
    private  Random random;
    private ArrayList<IndexTemp> filaRecursosPosse;
    private Semaphore mutexGett; 

    public Processos(int idProcesso, int tempoRequisicao, int tempoProcesso, int quantidadeRecuros){
        this.mutexGett = new Semaphore(1);
        this.filaRecursosPosse = new ArrayList<IndexTemp>();
        this.tempoRequisicao = tempoRequisicao;
        this.tempoProcesso = tempoProcesso; 
        this.quantidadeRecuros = quantidadeRecuros;
        this.idProcesso = idProcesso;
    }
    
    @Override
    public void run() {
        marcadorRequisicao = tempoRequisicao;
        tempoExecucao = 0;
        random = new Random();

        while(true){
            try {
                sleep(1000);
                requisitarRecursos();
                liberarRecursos();
                tempoExecucao++;

            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
            
        }


    }

    private void requisitarRecursos() throws InterruptedException{
        if(tempoExecucao == tempoRequisicao){
            int indexRecurso = realizarRequisicao();
            
            if(!(indexRecurso==-1)){

                Controle.mutex.acquire();
                if(Controle.recursosDisponiveis[indexRecurso].availablePermits()==0){// conferir se o processo entrar em estado de bloqueado
                    Controle.requisicaoPendente.add(new ProcessoRecurso( idProcesso, indexRecurso, getFilaRecursos()));
                    System.out.println("processo "+ idProcesso+" está esperando o recurso "+ Controle.nomeRecurso[indexRecurso]);
                }
                Controle.mutex.release(); 

                Controle.recursosDisponiveis[indexRecurso].acquire(); // caso não exista recursos disponiveis, parar aqui até liberar uma nova instância.
                Controle.mutex.acquire();
                System.out.println("processo "+ idProcesso+" está utilizando o recurso "+ Controle.nomeRecurso[indexRecurso]);
                removerRequisicaoPendente();
                Controle.recursosOcupados[indexRecurso]++;
                Controle.mutex.release();
                mutexGett.acquire();
                filaRecursosPosse.add(new IndexTemp(indexRecurso, tempoExecucao+tempoProcesso));
                mutexGett.release();
            }    
            
            tempoRequisicao = tempoRequisicao + marcadorRequisicao;
        }
        
    }

    private void liberarRecursos() throws InterruptedException{
        mutexGett.acquire();
        if(!filaRecursosPosse.isEmpty()){ // verificar se a fila está vazia
            
            int recursoTemp = filaRecursosPosse.get(0).getTempo();
            int recursoIndex = filaRecursosPosse.get(0).getIndex();
            mutexGett.release();

            if(tempoExecucao == recursoTemp){
                Controle.mutex.acquire();
                Controle.recursosDisponiveis[recursoIndex].release();
                Controle.recursosOcupados[recursoIndex] --;
                Controle.mutex.release();
                mutexGett.acquire();
                System.out.println("processo "+ idProcesso+" liberou o recurso"+ Controle.nomeRecurso[recursoIndex]);
                filaRecursosPosse.remove(0);
                mutexGett.release();
            }
        }
        mutexGett.release();  
    }

    private int realizarRequisicao() throws InterruptedException{
        int cont =0;
        int tentativas = 0;
        int indexRecurso;
        do{
            indexRecurso = random.nextInt(quantidadeRecuros); // gera um valor de index aleatoria (0 a quantidadeRecursos-1)
            mutexGett.acquire();
            for(IndexTemp a: filaRecursosPosse){
            
                if(indexRecurso == a.getIndex()){
                    cont++;
                }
            }
            mutexGett.release();
            if(cont < Controle.recursosTotal[indexRecurso]) // verificar se o recurso requerido supera a instancia do recurso existente
                return indexRecurso;
            cont = 0;    
            tentativas++; // tenta 3 vezes buscar um recurso novo
            System.out.println("processo "+ idProcesso+" não pode pedir mais do que a quantidade de recursos existentes");

        }while(tentativas<=3);

        return -1;
    }

    private void removerRequisicaoPendente(){
        for(int i=0; i< Controle.requisicaoPendente.size()-1;i++){
            if(idProcesso==Controle.requisicaoPendente.get(i).getIdProcesso()){
                Controle.requisicaoPendente.remove(i);
                break;
            }
        }
    }

    
    public int[] getFilaRecursos()throws InterruptedException{
        mutexGett.acquire();
        int[] filaRecursos = new int[(filaRecursosPosse.size()-1)];
        int i=0;
        for(IndexTemp a: filaRecursosPosse){
            filaRecursos[i] = a.getIndex();
        }
        mutexGett.release();
        return filaRecursos;
    }

    

    public class IndexTemp {
        private int index;
        private int tempo;

        public IndexTemp(int index, int tempo){
            this.index = index;
            this.tempo = tempo;
        }

        public int getIndex(){
            return index;
        }

        public int getTempo(){
            return tempo;
        }
        
    }
}
