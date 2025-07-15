package com.model.estruturaDados;
public class ProcessoRecurso {
    private int idProcesso;
    private int indexRecursoPendente;
    private int[] recursosPosse; 

    public ProcessoRecurso( int idProcesso, int indexRecursoPendente, int[] recursosPosse){
        this.idProcesso = idProcesso;
        this.indexRecursoPendente = indexRecursoPendente;
        this.recursosPosse = new int[recursosPosse.length];
        this.recursosPosse = recursosPosse;
    }

    public int getIdProcesso(){
        return idProcesso;
    }

    public int getIndexRecursoPendente(){
        return indexRecursoPendente;
    }
    public int[] getRecursosPosse(){
        return recursosPosse;
    }
    
}
