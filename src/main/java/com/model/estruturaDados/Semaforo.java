package com.model.estruturaDados;
import java.util.concurrent.Semaphore;

public class Semaforo extends Semaphore{
    private int id;
    private String nomeRecurso; 
    

    public Semaforo( int id, int permits,String nomeRecurso) {
        super(permits);
        this.id = id;
        this.nomeRecurso = nomeRecurso;
    }

    public int getId(){
        return id;
    }

    public String getNome(){
        return nomeRecurso;
    }
    
}