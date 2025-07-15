package com.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.model.estruturaDados.NomeIDInstacia;
import com.model.estruturaDados.ProcessoRecurso;
import com.model.estruturaDados.Semaforo;


public class Controle {

    public static Semaforo[] recursosDisponiveis; // recursos possuem valores fixos
    public static int[] recursosTotal;
    public static int[] recursosOcupados;
    public static ArrayList<ProcessoRecurso> requisicaoPendente; // lista de recursos bloqueados após requerir um recurso ocupado
    public static Semaphore mutex;
    public static ArrayList<Processos> filaProcesso;
    public static String[] nomeRecurso;

    public static void inicializar( int quantidaderecuros, NomeIDInstacia nomeIdInstacia[], ArrayList<Processos> processos){
        // cada Semaforo (recurso) possui um ID, quantidade de instancia (numero de permissoes) e um nome.
        // inicialização dos recursos
        filaProcesso = processos; // ponteiro
        requisicaoPendente = new ArrayList<ProcessoRecurso>();
        recursosDisponiveis = new Semaforo[quantidaderecuros];
        recursosTotal = new int[quantidaderecuros];
        recursosOcupados = new int[quantidaderecuros];
        nomeRecurso = new String[quantidaderecuros];
        mutex = new Semaphore(1);

       for(int i=0; i<quantidaderecuros;i++){
            recursosDisponiveis[i] = new Semaforo(nomeIdInstacia[i].getId(), nomeIdInstacia[i].getInstacia(), nomeIdInstacia[i].getNome());
            recursosTotal[i] = nomeIdInstacia[i].getInstacia();
            recursosOcupados[i] = 0;
            nomeRecurso[i] = nomeIdInstacia[i].getNome();
        
        }

    }

    public static void criarThread(){

    }

    

}
