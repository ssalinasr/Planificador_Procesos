/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author sebas
 */
public class ColaSJF {
    
    public Queue<Proceso> cola_sjf;
    public ArrayList<Proceso> lista_sjf;
    
    public ColaSJF(){
        this.cola_sjf = new LinkedList<>();
        this.lista_sjf = new ArrayList<>();
    }
    
    public void insertar_sjf(Proceso p){
        this.lista_sjf.add(p);
    }
    
    public void eliminar_sjf(Proceso p){
        this.lista_sjf.remove(p);
    }
    
    public static Comparator<Proceso> sjf_comp = new Comparator<Proceso>() {
        @Override
        public int compare(Proceso t, Proceso t1) {
            int burst_t = t.getBurst();
            int burst_t1 = t1.getBurst();
            
            return burst_t - burst_t1;
        }
    };
    
    public void calcular_valores(ArrayList <Proceso> cola_fcfs){
        this.cola_sjf.clear();
        Collections.sort(this.lista_sjf, sjf_comp);
           

        
        for (int i = 0; i < lista_sjf.size(); i++){
            try{
                if(!cola_fcfs.isEmpty() && i == 0){
                this.lista_sjf.get(0).setBegin(cola_fcfs.get(cola_fcfs.size() -1).getEnd());
                }
                else{
                      lista_sjf.get(i).setBegin(lista_sjf.get(i-1).getEnd());
                }
            }catch(Exception e){
                lista_sjf.get(i).setBegin(0);
            }
            finally{
                lista_sjf.get(i).setEnd();
                lista_sjf.get(i).setReturn();
                lista_sjf.get(i).setWait();
                
                this.cola_sjf.add(lista_sjf.get(i));
            }
                
            
            

        }
        
    }
        public void imprimir_cola(){
        for(Proceso p: this.cola_sjf){
            System.out.println(p.getName()+", "+p.getArrive()+", "+p.getBurst()+", "+p.getBegin()+", "+p.getEnd()+", "+p.getReturn()+", "+
                    p.getWait()+", "+p.getStatus()+", "+p.getPriority()+".");
        }
    }
    
}
