package com.model.estruturaDados;
public class NomeIDInstacia {
    private String nome;
    private int id;
    private int instacia;

    public NomeIDInstacia(String nome, int id, int instacia){
        this.nome = nome;
        this.id = id;
        this.instacia = instacia;
    }
    
    public String getNome() {
        return nome;
    }

    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public int getInstacia() {
        return instacia;
    }

    
    public void setInstacia(int instacia) {
        this.instacia = instacia;
    }

}
