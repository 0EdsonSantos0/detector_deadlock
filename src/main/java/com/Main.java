package com;

import java.util.ArrayList;

import com.model.Controle;
import com.model.Processos;
import com.model.estruturaDados.NomeIDInstacia;

public class Main {

   
    public static void main(String[] args) throws InterruptedException {
        int quantidadeRecursos = 10;
        NomeIDInstacia[] recursos = new NomeIDInstacia[quantidadeRecursos];
        ArrayList<Processos> p = new ArrayList<Processos>();
        
        recursos[0] = new NomeIDInstacia("A", 1, 3);
        recursos[1] = new NomeIDInstacia("B", 2, 2);
        recursos[2] = new NomeIDInstacia("C", 3, 2);
        recursos[3] = new NomeIDInstacia("D", 4, 3);
        recursos[4] = new NomeIDInstacia("E", 5, 3);
        recursos[5] = new NomeIDInstacia("F", 6, 3);
        recursos[6] = new NomeIDInstacia("G", 7, 3);
        recursos[7] = new NomeIDInstacia("H", 8, 3);
        recursos[8] = new NomeIDInstacia("I", 9, 3);
        recursos[9] = new NomeIDInstacia("J",10, 3);
        p.add(new Processos(1, 4, 6,quantidadeRecursos));
        p.add(new Processos(2, 6, 10,quantidadeRecursos));
        p.add(new Processos(3, 2, 4,quantidadeRecursos));
        p.add(new Processos(4, 4, 6,quantidadeRecursos));
        p.add(new Processos(5, 2, 6,quantidadeRecursos));
        p.add(new Processos(6, 2, 6,quantidadeRecursos));
        p.add(new Processos(7, 3, 4,quantidadeRecursos));
        p.add(new Processos(8, 2, 7,quantidadeRecursos));
        p.add(new Processos(9, 5, 10,quantidadeRecursos));
        p.add(new Processos(10, 3, 9,quantidadeRecursos));

        Controle.inicializar(quantidadeRecursos, recursos, p);

        

        for (Processos a: p){
            a.start();
        }
        
        

        
    }
}
