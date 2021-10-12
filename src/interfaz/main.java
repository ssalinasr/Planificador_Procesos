/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import logica.*;

/**
 *
 * @author sebas
 */
public class main {
    
    public static void main(String[] args) {
        
        //para FCFS
        Proceso p1 = new Proceso("A",0,3,0);
        Proceso p2 = new Proceso("B",1,5,0);
        Proceso p3 = new Proceso("C",2,8,0);
        Proceso p4 = new Proceso("D",3,7,0);
        Proceso p5 = new Proceso("E",4,4,0);
        ColaFCFS col = new ColaFCFS();
        col.insertar_fcfs(p1);
        col.insertar_fcfs(p2);
        col.insertar_fcfs(p3);
        col.insertar_fcfs(p4);
        col.insertar_fcfs(p5);
        col.calcular_valores();
        
        //SJF
        Proceso p6 = new Proceso("A",0,3,1);
        Proceso p7 = new Proceso("B",1,5,1);
        Proceso p8 = new Proceso("C",2,8,1);
        Proceso p9 = new Proceso("D",3,7,1);
        Proceso p10 = new Proceso("E",4,4,1);
       ColaSJF cols = new ColaSJF();
        cols.insertar_sjf(p6);
        cols.insertar_sjf(p7);
        cols.insertar_sjf(p8);
        cols.insertar_sjf(p9);
        cols.insertar_sjf(p10);
        cols.calcular_valores(col.lista_fcfs);
        
        //RR
        Proceso p11 = new Proceso("A",0,3,2);
        Proceso p12 = new Proceso("B",1,5,2);
        Proceso p13 = new Proceso("C",2,8,2);
        Proceso p14 = new Proceso("D",3,7,2);
        Proceso p15 = new Proceso("E",4,4,2);
        ColaRR colr = new ColaRR(4);
        colr.insertar_rr(p11);
        colr.insertar_rr(p12);
        colr.insertar_rr(p13);
        colr.insertar_rr(p14);
        colr.insertar_rr(p15);
        colr.calcular_valores(col.lista_fcfs, cols.lista_sjf);
        
        System.out.println("FCFS:");
        col.imprimir_cola();
        System.out.println("SJF:");
        cols.imprimir_cola();
        System.out.println("RR:");
        colr.imprimir_cola();
        
        
        
    }
    
}
