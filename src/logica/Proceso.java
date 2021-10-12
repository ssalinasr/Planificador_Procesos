/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author sebas
 */
public class Proceso{
    
    private String Name;
    private int Arrive;
    private int Burst;
    private int Begin;
    private int End;
    private int Return;
    private int Wait;
    private String Status;
    private int Priority;
    
    public Proceso(String name, int arrive, int burst, int priority){
        this.Name = name;
        this.Arrive = arrive;
        this.Burst = burst;
        this.Priority = priority;
        int rand = (int)Math.floor(Math.random()*5);
        if(rand == 1){
        this.Status = "bloqueado";
        }
        else{
            this.Status = "listo";
        }
    } 

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int Priority) {
        this.Priority = Priority;
    }
    
    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }    
    
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getArrive() {
        return Arrive;
    }

    public void setArrive(int Arrive) {
        this.Arrive = Arrive;
    }

    public int getBurst() {
        return Burst;
    }

    public void setBurst(int Burst) {
        this.Burst = Burst;
    }

    public int getBegin() {
        return Begin;
    }

    public void setBegin(int Begin) {
        this.Begin = Begin;
    }

    public int getEnd() {
        return End;
    }

    public void setEnd() {
        this.End = this.Burst + this.Begin;
    }
    
    public void setEnd(int End) {
        this.End = End;
    }

    public int getReturn() {
        return Return;
    }

    public void setReturn() {
        this.Return = this.End - this.Arrive;
    }

    public int getWait() {
        return Wait;
    }

    public void setWait() {
        this.Wait = this.Return - this.Burst;
        if(this.Wait < 0){
            this.Wait = 0;
        }
    }
    
    
    
    
    
    
}
