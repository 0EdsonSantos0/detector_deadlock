package com.model;
import java.util.ArrayList;

import com.model.estruturaDados.ProcessoRecurso;

public class SistemaOperacional extends Thread {
    private int tempoVerificar;
    private int tempoExecucao;
    private int marcadorTempo;
    private ArrayList<ProcessoRecurso> resquisicaoPendenteTemp;

    public SistemaOperacional( int tempoVerificar){
        this. resquisicaoPendenteTemp = new ArrayList<ProcessoRecurso>();
        this.tempoVerificar = tempoVerificar;
    }

    @Override
    public void run(){
        marcadorTempo = tempoVerificar; 
        tempoExecucao = 0;
        while(true){

            try {
                sleep(1000);
                if(tempoExecucao == tempoVerificar)
                    verificarDeadlock();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void verificarDeadlock() throws InterruptedException{
        Controle.mutex.acquire();
        if(!(Controle.requisicaoPendente.size()<=1)){
            resquisicaoPendenteTemp = Controle.requisicaoPendente;
            Controle.mutex.release();
            if(todosRecursosBloaqueados(resquisicaoPendenteTemp)){
                identRelacaoDeadlock();
            }  
        }else{
            Controle.mutex.release();
        }
        tempoVerificar = tempoVerificar + marcadorTempo;
        
    }

    private boolean todosRecursosBloaqueados(ArrayList<ProcessoRecurso> resquisicaoPendenteTemp){ // filtragem
        int cont=0;
        int incrementa =0;  
        int iDUltimoProcesso = resquisicaoPendenteTemp.get(resquisicaoPendenteTemp.size()-1).getIdProcesso();
        int[] vetorIndexRecursos;
        int indexRecursoPendente;

        while(!(resquisicaoPendenteTemp.size()<=1)){ // não da deadlock com um processo bloqueado
            indexRecursoPendente = resquisicaoPendenteTemp.get(incrementa).getIndexRecursoPendente();

            for(int i =0; i<resquisicaoPendenteTemp.size()-1; i++){
                vetorIndexRecursos = new int[resquisicaoPendenteTemp.get(i).getRecursosPosse().length];
                vetorIndexRecursos = resquisicaoPendenteTemp.get(i).getRecursosPosse();
                for(int j =0; j<vetorIndexRecursos.length; j++){
                    if(indexRecursoPendente ==vetorIndexRecursos[j]){
                        cont++;
                    }
                }
            }
            if(!(cont == Controle.recursosTotal[indexRecursoPendente])){ // todos os recursos requerido estão em posse de processos bloqueado / possivel deadlock
                vetorIndexRecursos = resquisicaoPendenteTemp.get(incrementa).getRecursosPosse();

                if(resquisicaoPendenteTemp.get(incrementa).getIdProcesso() == iDUltimoProcesso){
                    resquisicaoPendenteTemp.remove(incrementa);
                    confDependenciaEntreRecursos(vetorIndexRecursos);
                    break;
                }else{
                    resquisicaoPendenteTemp.remove(incrementa);
                    confDependenciaEntreRecursos(vetorIndexRecursos);
                }
                
                
            }else{
                if(resquisicaoPendenteTemp.get(incrementa).getIdProcesso() == iDUltimoProcesso)
                    break;
                incrementa++;
            }
            
        }

        if(resquisicaoPendenteTemp.size()>1){ // se existir recursos da lista, eles estão em deadlock
            return true;
        }
        
        return false;
    } 

    private void confDependenciaEntreRecursos(int[] vetorIndexRecursos){
        int cont;
        if(!(resquisicaoPendenteTemp.size()<=1)){
            for(int i=0; i<vetorIndexRecursos.length;i++){
                int iDUlitmo = resquisicaoPendenteTemp.get(resquisicaoPendenteTemp.size()-1).getIdProcesso();
                cont =0;
                do{
                    if(vetorIndexRecursos[i]==resquisicaoPendenteTemp.get(cont).getIndexRecursoPendente()){
                        if(resquisicaoPendenteTemp.size()<=1)
                            return;
                        resquisicaoPendenteTemp.remove(cont);
                        if(resquisicaoPendenteTemp.size()<=1)
                            return;
                    }    
                    else{
                        cont++;
                    }
                    if(iDUlitmo==resquisicaoPendenteTemp.get(cont).getIdProcesso()){
                        if(vetorIndexRecursos[i]==resquisicaoPendenteTemp.get(cont).getIndexRecursoPendente()){
                            if(resquisicaoPendenteTemp.size()<=1)
                                return;
                            iDUlitmo = resquisicaoPendenteTemp.get(cont-1).getIdProcesso();
                            resquisicaoPendenteTemp.remove(cont);
                            if(resquisicaoPendenteTemp.size()<=1)
                                return;
                            break;
                        }
                    }

                } while(iDUlitmo!=resquisicaoPendenteTemp.get(cont).getIdProcesso());

            }
        }
    }

    private void identRelacaoDeadlock(){

    }


}
