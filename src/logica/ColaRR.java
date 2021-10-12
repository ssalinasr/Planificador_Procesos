/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author sebas
 */
public class ColaRR {
    
    public Queue<Proceso> cola_rr;
    public ArrayList<Proceso> lista_rr;
    public int quantum;
    
    public ColaRR(int quantum){
        this.cola_rr = new LinkedList<>();
        this.lista_rr = new ArrayList<>();
        this.quantum =  quantum;
    }
    
    public void insertar_rr(Proceso p){
        this.lista_rr.add(p);
    }
    
    public void eliminar_rr(Proceso p){
        this.lista_rr.remove(p);
    }
    
    public void calcular_valores(ArrayList <Proceso> cola_fcfs, ArrayList<Proceso> cola_sjf){
        this.cola_rr.clear();
        for (int i = 0; i < lista_rr.size(); i++){
            try{
                if(!cola_sjf.isEmpty()  && i == 0){
                this.lista_rr.get(0).setBegin(cola_sjf.get(cola_sjf.size() -1).getEnd());
                }
                else{
               if(!cola_fcfs.isEmpty()  && i == 0){
                     this.lista_rr.get(0).setBegin(cola_fcfs.get(cola_fcfs.size() -1).getEnd());
                }
                else{
                      lista_rr.get(i).setBegin(lista_rr.get(i-1).getEnd());
                }
                }
   
            }catch(Exception e){
                lista_rr.get(i).setBegin(0);
            }
            finally{
                
                if(lista_rr.get(i).getBurst() > this.quantum){
                    lista_rr.get(i).setBurst(this.quantum);
                    lista_rr.get(i).setEnd(this.lista_rr.get(i).getBegin()+this.quantum);
                }
                else{
                    //El valor debe quedar igual lol
                    lista_rr.get(i).setEnd();
                }

                lista_rr.get(i).setReturn();
                lista_rr.get(i).setWait();
                
                this.cola_rr.add(lista_rr.get(i));
            }
            }
        }
        
    
    
    public void imprimir_cola(){
        for(Proceso p: this.cola_rr){
            System.out.println(p.getName()+", "+p.getArrive()+", "+p.getBurst()+", "+p.getBegin()+", "+p.getEnd()+", "+p.getReturn()+", "+ 
                    p.getWait()+", "+p.getStatus()+", "+p.getPriority()+".");
        }
    }
    
}
