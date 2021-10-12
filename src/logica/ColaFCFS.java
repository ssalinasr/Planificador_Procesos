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
public class ColaFCFS {
    
    public Queue<Proceso> cola_fcfs;
    public ArrayList<Proceso> lista_fcfs;
    
    public ColaFCFS(){
        this.cola_fcfs = new LinkedList<>();
        this.lista_fcfs = new ArrayList<>();
    }
    
    public void insertar_fcfs(Proceso p){
        this.lista_fcfs.add(p);
    }
    
    public void eliminar_fcfs(Proceso p){
        this.lista_fcfs.remove(p);
    }
    
    public void calcular_valores(){
        this.cola_fcfs.clear();
        for (int i = 0; i < lista_fcfs.size(); i++){
            try{
                lista_fcfs.get(i).setBegin(lista_fcfs.get(i-1).getEnd());
            }catch(Exception e){
                lista_fcfs.get(i).setBegin(0);
            }
            finally{
                lista_fcfs.get(i).setEnd();
                lista_fcfs.get(i).setReturn();
                lista_fcfs.get(i).setWait();
                
                this.cola_fcfs.add(lista_fcfs.get(i));
            }
        }
        
    }
    
    
    public void imprimir_cola(){
        for(Proceso p: this.cola_fcfs){
            System.out.println(p.getName()+", "+p.getArrive()+", "+p.getBurst()+", "+p.getBegin()+", "+p.getEnd()+", "+p.getReturn()+", "+
                    p.getWait()+", "+p.getStatus()+", "+p.getPriority()+".");
        }
    }
}
