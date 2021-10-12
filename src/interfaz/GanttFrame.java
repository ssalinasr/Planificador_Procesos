/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.*;

/**
 *
 * @author sebas
 */
public class GanttFrame extends javax.swing.JFrame {
    
    DefaultTableModel gmd;
    DefaultTableModel pmd_fcfs;
    DefaultTableModel pmd_sjf;
    DefaultTableModel pmd_rr;
    DefaultTableModel nmd;
    int llegada_fcfs = 0;
    int llegada_sjf = 0;
    int llegada_rr = 0;
    int j = 0;
    int prior = 0;
       
    ArrayList<Proceso> lista_procesos;
    ArrayList<Proceso> lista_terminados;
    Queue<Proceso> cola_bloqueados;
    
    int quantum = 0;
    
    ColaFCFS cola_fcfs = new ColaFCFS();
    ColaSJF cola_sjf = new ColaSJF();
    ColaRR cola_rr = new ColaRR(quantum);
    
    Thread h_ejecucion;
    int timer = 0;
    int maxFinal = 0;
    int counter = 0;

    /**
     * Creates new form GanttFrame
     */
    public GanttFrame() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Algoritmo FCFS");
        this.initTables();
        lista_procesos = new ArrayList<>();
        cola_bloqueados = new LinkedList<>();
        lista_terminados = new ArrayList<>();
    }
    
    public void initTables(){
        
         gmd = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
         };
        
        gmd.addColumn("Nombre");
        gmd.addColumn("Llegada");
        gmd.addColumn("Ráfaga");
        gmd.addColumn("Estado");
        gmd.addColumn("Prioridad");
       
        this.TablaGenerados.setModel(gmd);
        
        pmd_fcfs = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_fcfs.addColumn("Nombre");
        pmd_fcfs.addColumn("Llegada");
        pmd_fcfs.addColumn("Ráfaga");
        pmd_fcfs.addColumn("Comienzo");
        pmd_fcfs.addColumn("Final");
        pmd_fcfs.addColumn("Retorno");
        pmd_fcfs.addColumn("Espera");
        pmd_fcfs.addColumn("Estado");
        
        this.TablaProcesosFCFS.setModel(pmd_fcfs);
        
        pmd_sjf = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_sjf.addColumn("Nombre");
        pmd_sjf.addColumn("Llegada");
        pmd_sjf.addColumn("Ráfaga");
        pmd_sjf.addColumn("Comienzo");
        pmd_sjf.addColumn("Final");
        pmd_sjf.addColumn("Retorno");
        pmd_sjf.addColumn("Espera");
        pmd_sjf.addColumn("Estado");
        
        this.TablaProcesosSJF.setModel(pmd_sjf);
        
        pmd_rr = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_rr.addColumn("Nombre");
        pmd_rr.addColumn("Llegada");
        pmd_rr.addColumn("Ráfaga");
        pmd_rr.addColumn("Comienzo");
        pmd_rr.addColumn("Final");
        pmd_rr.addColumn("Retorno");
        pmd_rr.addColumn("Espera");
        pmd_rr.addColumn("Estado");
        
        this.TablaProcesosRR.setModel(pmd_rr);
        
        nmd = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }};
        
        nmd.addColumn("Proceso");
        
        for(int i = 1; i < 100; i++){
            nmd.addColumn(i);
        }

        this.TablaGantt.setModel(nmd);
        
                
       for(int i = 1; i < 100; i++){
            this.TablaGantt.getColumn(Integer.toString(i)).setPreferredWidth(25);
            this.TablaGantt.getColumn(Integer.toString(i)).setResizable(false);
        }
        
    }
    
    public Proceso generarProceso(){
        
        int number_proc1 = (int)Math.floor(Math.random()*15);
        int number_proc2 = (int)Math.floor(Math.random()*15);
        
        String name = "";
        

        int rafaga = (int) Math.floor(Math.random()*10) + 5;
        
        int prioridad = (int) Math.floor(Math.random()*3);
        
        int llegada = 0;
        
        if(prioridad == 0){
            name ="FCFS."+number_proc1 + number_proc2;   
           llegada = this.llegada_fcfs;
           this.llegada_fcfs++;
        }
        
        if(prioridad == 1){
            name ="SJF."+number_proc1 + number_proc2;
           llegada = this.llegada_sjf;
           this.llegada_sjf++;
        }
        
       if(prioridad == 2){
             name ="RR."+number_proc1 + number_proc2;
           llegada = this.llegada_rr;
           this.llegada_rr++;
        }

        
        return new Proceso(name, llegada, rafaga, prioridad);
        
    }
    
    public void insertarProceso(Proceso p){
    
        Object[] row = new Object[5]; 
        row[0] = p.getName();
        row[1] = p.getArrive();
        row[2] = p.getBurst();
        row[3] = p.getStatus();
        row[4] = p.getPriority();
        
        
        gmd.addRow(row);
        lista_procesos.add(p);
        
    }
    
    public void iniciarEjecucion(){
        
        this.h_ejecucion = new Thread(new Runnable() {
            @Override
            public void run() {
                try {                    
                    ejecutarGantt();
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "El hilo se ha detenido.", "Advertencia", 2);
                } catch(NullPointerException ex){
                    JOptionPane.showMessageDialog(null, "La ejecución de procesos ha finalizado.", "Info", 1);
                }
            }
        });
        this.h_ejecucion.start();
    }
    
     public void insertarGantt(Proceso p){
            
       StatusColumnCellRenderer tcr = new StatusColumnCellRenderer();
        for(int k = 1; k < this.TablaGantt.getColumnCount(); k++){
            this.TablaGantt.getColumn(Integer.toString(k)).setCellRenderer(tcr);
        }
            Object[] row = new Object[this.TablaGantt.getColumnCount()]; 
            row[0] = p.getName();
            
            for(int r = 1; r < row.length; r++){
                row[r] = " ";
            }
            
        
            nmd.addRow(row);
        
    }
   
    public void ejecutarGantt() throws InterruptedException{
        this.BtnInsertar.setEnabled(false);
        while(this.prior < 3){
            
            if(this.prior == 0){
                this.ejecutarColaFCFS();
                this.timer = 0;
                this.prior++;
            }
            
            if(this.prior == 1){
                this.ejecutarColaSJF();
                this.timer = 0;
                this.prior++;
            }
            
            if(this.prior == 2){
                this.ejecutarColaRR();
                this.timer = 0;
                this.prior++;
            }
            
            
        }
        
        this.prior = 0;
        if(!this.cola_bloqueados.isEmpty()){
            System.out.println("Ejecución con bloqueados...");
            this.lista_procesos.clear();
            for(Proceso proc: this.cola_bloqueados){
                proc.setStatus("listo");
                this.insertarProceso(proc);
            }
            this.insertarTabla();
        
            while(this.prior < 3){
            
            if(this.prior == 0){
                this.ejecutarColaFCFS();
                this.timer = 0;
                this.prior++;
            }
            
            if(this.prior == 1){
                this.ejecutarColaSJF();
                this.timer = 0;
                this.prior++;
            }
            
            if(this.prior == 2){
                this.ejecutarColaRR();
                this.timer = 0;
                this.prior++;
            }
            
            
        }
        }
        this.j = 0;
       this.BtnInsertar.setEnabled(true);
    }
    
    public void ejecutarColaFCFS() throws InterruptedException{
        
        while(!this.cola_fcfs.cola_fcfs.isEmpty()){
        Proceso current = this.cola_fcfs.cola_fcfs.peek();
        System.out.println(current);
        this.LblProcess.setText("Proceso actual: "+current.getName());
        
            try{
            this.insertarGantt(current);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "La cola de procesos está vacía", "Advertencia",1);
            }
            
            for(int i = 1; i < this.TablaGantt.getColumnCount(); i++){
                
                if(current.getStatus().equals("listo")){
                    this.TablaGantt.setValueAt("A", j, i);
                }

                if(current.getStatus().equals("bloqueado")){
                    this.TablaGantt.setValueAt("B", j, i);
                }

                if(i == current.getEnd()){
                        j ++;
                        this.cola_fcfs.cola_fcfs.remove();
                        this.cola_fcfs.lista_fcfs.remove(current);
                        this.lista_terminados.add(current);
                        
                        
                if(current.getStatus().equals("bloqueado")){
                        if(!this.cola_bloqueados.contains(current)){
                                this.cola_bloqueados.add(current);
                        }
                                
               }
                    
            current = this.cola_fcfs.cola_fcfs.peek();
                        if(current == null){
                            break;
                        }
                
                        this.LblProcess.setText("Proceso actual: "+current.getName());
                        this.insertarGantt(current);      

                }
                timer++;
                this.LblTimer.setText("Tiempo transcurrido: "+ this.timer);
                Thread.sleep(50);
        }
        }
    }
    
    public void ejecutarColaSJF() throws InterruptedException{
        
        while(!this.cola_sjf.cola_sjf.isEmpty()){
        Proceso current = this.cola_sjf.cola_sjf.peek();
        this.LblProcess.setText("Proceso actual: "+current.getName());
        
            try{
            this.insertarGantt(current);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "La cola de procesos está vacía", "Advertencia",1);
            }
            
            for(int i = this.cola_sjf.lista_sjf.get(0).getBegin() + 1; i < this.TablaGantt.getColumnCount(); i++){               
                
                if(current.getStatus().equals("listo")){
                    this.TablaGantt.setValueAt("C", j, i);
                }

                if(current.getStatus().equals("bloqueado")){
                    this.TablaGantt.setValueAt("D", j, i);
                }      
                
                if(i == current.getEnd()){
                        j ++;
                        this.cola_sjf.cola_sjf.remove();
                         this.cola_sjf.lista_sjf.remove(current);
                        this.lista_terminados.add(current);
                        
                    if(current.getStatus().equals("bloqueado")){
                        if(!this.cola_bloqueados.contains(current)){
                                this.cola_bloqueados.add(current);
                        }
                                
               }
                    current = this.cola_sjf.cola_sjf.peek();
                     if(current == null){
                            break;
                        }
                     
                  this.LblProcess.setText("Proceso actual: "+current.getName());
                  this.insertarGantt(current);      

                }
                timer++;
                this.LblTimer.setText("Tiempo transcurrido: "+ this.timer);
                Thread.sleep(50);
        }
        }
        
    }
    
    public void ejecutarColaRR() throws InterruptedException{
        
        while(!this.cola_rr.cola_rr.isEmpty()){
        Proceso current = this.cola_rr.cola_rr.peek();
        this.LblProcess.setText("Proceso actual: "+current.getName());
        
            try{
            this.insertarGantt(current);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "La cola de procesos está vacía", "Advertencia",1);
            }
            
            for(int i = this.cola_rr.lista_rr.get(0).getBegin() + 1; i < this.TablaGantt.getColumnCount(); i++){
                
                if(current.getStatus().equals("listo")){
                    this.TablaGantt.setValueAt("E", j, i);
                }

                if(current.getStatus().equals("bloqueado")){
                    this.TablaGantt.setValueAt("F", j, i);
                }     
                      
                if(i == current.getEnd()){
                        j ++;
                        this.cola_rr.cola_rr.remove();
                        this.cola_rr.lista_rr.remove(current);
                        this.lista_terminados.add(current);
  
                        
                    if(current.getStatus().equals("bloqueado")){
                        if(!this.cola_bloqueados.contains(current)){
                                this.cola_bloqueados.add(current);
                        }
                                
               }
                    
                     current = this.cola_rr.cola_rr.peek();
                       if(current == null){
                            break;
                        }
                       
                        this.LblProcess.setText("Proceso actual: "+current.getName());
                        this.insertarGantt(current);      
        

                }
                timer++;
                this.LblTimer.setText("Tiempo transcurrido: "+ this.timer);
                Thread.sleep(50);
        }
        }
    }
            
     
   public static Comparator<Proceso> prior_comp = new Comparator<Proceso>() {
        @Override
        public int compare(Proceso t, Proceso t1) {
            int prior_t = t.getPriority();
            int prior_t1 = t1.getPriority();
            
            return prior_t - prior_t1;
        }
    };
    
    
    public void insertarBloqueado(){
        try{
            System.out.println(this.cola_bloqueados.size());
            Proceso e = this.cola_bloqueados.peek();
            e.setStatus("listo");
            e.setBegin(this.maxFinal);
            e.setEnd();
            e.setReturn();
            e.setWait();
            
            this.maxFinal = e.getEnd();
            
            System.out.println(e.getArrive());
            System.out.println(e.getEnd());
            System.out.println(e.getReturn());
            System.out.println(e.getWait());

            this.cola_bloqueados.remove();
            
            JOptionPane.showMessageDialog(null, "El proceso bloqueado "+e.getName()+ " ha sido desbloqueado e insertado de nuevo a la cola.","Aviso",1);

            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "La cola de bloqueados está vacía.", "Advertencia", 2);
        }
         
    }
    
    public void insertarTabla(){
        
        System.out.println(this.lista_procesos);        
        this.cola_fcfs.lista_fcfs.clear();
        this.cola_fcfs.cola_fcfs.clear();
        
        this.cola_sjf.lista_sjf.clear();
        this.cola_sjf.cola_sjf.clear();
        
        this.cola_rr.lista_rr.clear();
        this.cola_rr.cola_rr.clear();
      
        Collections.sort(this.lista_procesos, prior_comp);
        
 
        Object[] row = new Object[8];
        
        for(Proceso p: this.lista_procesos){
            if(p.getPriority() == 0){ //FCFS
                this.cola_fcfs.insertar_fcfs(p);
                this.cola_fcfs.calcular_valores();
            }
            
            if(p.getPriority() == 1){ //SJF
                this.cola_sjf.insertar_sjf(p);
                this.cola_sjf.calcular_valores(this.cola_fcfs.lista_fcfs);
            } 
            
            if(p.getPriority() == 2){ //RR
                this.cola_rr.quantum = this.quantum;
                this.cola_rr.insertar_rr(p);
                this.cola_rr.calcular_valores(this.cola_fcfs.lista_fcfs, this.cola_sjf.lista_sjf);
                
            }

            
            System.out.println("FCFS");
            System.out.println(this.cola_fcfs.cola_fcfs);
            System.out.println("SJF");
            System.out.println(this.cola_sjf.cola_sjf);
            System.out.println("RR");
            System.out.println(this.cola_rr.cola_rr);
            
                    
        pmd_fcfs = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_fcfs.addColumn("Nombre");
        pmd_fcfs.addColumn("Llegada");
        pmd_fcfs.addColumn("Ráfaga");
        pmd_fcfs.addColumn("Comienzo");
        pmd_fcfs.addColumn("Final");
        pmd_fcfs.addColumn("Retorno");
        pmd_fcfs.addColumn("Espera");
        pmd_fcfs.addColumn("Estado");
        
        this.TablaProcesosFCFS.setModel(pmd_fcfs);
        
        pmd_sjf = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_sjf.addColumn("Nombre");
        pmd_sjf.addColumn("Llegada");
        pmd_sjf.addColumn("Ráfaga");
        pmd_sjf.addColumn("Comienzo");
        pmd_sjf.addColumn("Final");
        pmd_sjf.addColumn("Retorno");
        pmd_sjf.addColumn("Espera");
        pmd_sjf.addColumn("Estado");
        
        this.TablaProcesosSJF.setModel(pmd_sjf);
        
        pmd_rr = new DefaultTableModel(){
        @Override
         public boolean isCellEditable(int row, int col){
             return false;
         }
        };
        
        pmd_rr.addColumn("Nombre");
        pmd_rr.addColumn("Llegada");
        pmd_rr.addColumn("Ráfaga");
        pmd_rr.addColumn("Comienzo");
        pmd_rr.addColumn("Final");
        pmd_rr.addColumn("Retorno");
        pmd_rr.addColumn("Espera");
        pmd_rr.addColumn("Estado");
        
        this.TablaProcesosRR.setModel(pmd_rr);
            
            
            for(Proceso proc : this.cola_fcfs.cola_fcfs){
                row[0] = proc.getName();
                row[1] = proc.getArrive();
                row[2] = proc.getBurst();
                row[3] = proc.getBegin();
                row[4] = proc.getEnd();
                row[5] = proc.getReturn();
                row[6] = proc.getWait();
                row[7] = proc.getStatus();
                this.pmd_fcfs.addRow(row);
            }
            
            for(Proceso proc : this.cola_sjf.cola_sjf){
                row[0] = proc.getName();
                row[1] = proc.getArrive();
                row[2] = proc.getBurst();
                row[3] = proc.getBegin();
                row[4] = proc.getEnd();
                row[5] = proc.getReturn();
                row[6] = proc.getWait();
                row[7] = proc.getStatus();
                this.pmd_sjf.addRow(row);
            }
            
          for(Proceso proc : this.cola_rr.cola_rr){
                row[0] = proc.getName();
                row[1] = proc.getArrive();
                row[2] = proc.getBurst();
                row[3] = proc.getBegin();
                row[4] = proc.getEnd();
                row[5] = proc.getReturn();
                row[6] = proc.getWait();
                row[7] = proc.getStatus();
                this.pmd_rr.addRow(row);
            }                  
        }   
    }
    
    
    public void limpiarTablas(){
        this.initTables();
        this.lista_procesos.clear();
        this.lista_terminados.clear();
        this.cola_bloqueados.clear();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGenerados = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaGenerados = new javax.swing.JTable();
        BtnInsertar = new javax.swing.JButton();
        BtnGenerar = new javax.swing.JButton();
        LblProc = new javax.swing.JLabel();
        LblQuan = new javax.swing.JLabel();
        TxtQuantum = new javax.swing.JTextField();
        PanelProcesosFCFS = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProcesosFCFS = new javax.swing.JTable();
        LblProcFCFS = new javax.swing.JLabel();
        PanelGantt = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaGantt = new javax.swing.JTable();
        LblGantt = new javax.swing.JLabel();
        LblProcess = new javax.swing.JLabel();
        LblTimer = new javax.swing.JLabel();
        BtnIniciar = new javax.swing.JButton();
        BtnLimpiar = new javax.swing.JButton();
        PanelProcesosSJF = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaProcesosSJF = new javax.swing.JTable();
        LblProcSJF = new javax.swing.JLabel();
        PanelProcesosRR = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaProcesosRR = new javax.swing.JTable();
        LblProcT2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelGenerados.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TablaGenerados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaGenerados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(TablaGenerados);

        BtnInsertar.setText("Insertar");
        BtnInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInsertarActionPerformed(evt);
            }
        });

        BtnGenerar.setText("Generar");
        BtnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGenerarActionPerformed(evt);
            }
        });

        LblProc.setText("Lista de procesos");

        LblQuan.setText("Quantum");

        javax.swing.GroupLayout PanelGeneradosLayout = new javax.swing.GroupLayout(PanelGenerados);
        PanelGenerados.setLayout(PanelGeneradosLayout);
        PanelGeneradosLayout.setHorizontalGroup(
            PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneradosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                    .addComponent(LblProc)
                    .addGroup(PanelGeneradosLayout.createSequentialGroup()
                        .addGroup(PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BtnGenerar, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(LblQuan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtQuantum)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGeneradosLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(BtnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)))))
                .addContainerGap())
        );
        PanelGeneradosLayout.setVerticalGroup(
            PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneradosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblProc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblQuan)
                    .addComponent(TxtQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelGeneradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnInsertar)
                    .addComponent(BtnGenerar))
                .addContainerGap())
        );

        PanelProcesosFCFS.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TablaProcesosFCFS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaProcesosFCFS.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(TablaProcesosFCFS);

        LblProcFCFS.setText("Tabla de procesos FCFS");

        javax.swing.GroupLayout PanelProcesosFCFSLayout = new javax.swing.GroupLayout(PanelProcesosFCFS);
        PanelProcesosFCFS.setLayout(PanelProcesosFCFSLayout);
        PanelProcesosFCFSLayout.setHorizontalGroup(
            PanelProcesosFCFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosFCFSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelProcesosFCFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .addGroup(PanelProcesosFCFSLayout.createSequentialGroup()
                        .addComponent(LblProcFCFS)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelProcesosFCFSLayout.setVerticalGroup(
            PanelProcesosFCFSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosFCFSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblProcFCFS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelGantt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        TablaGantt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaGantt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(TablaGantt);
        if (TablaGantt.getColumnModel().getColumnCount() > 0) {
            TablaGantt.getColumnModel().getColumn(2).setResizable(false);
            TablaGantt.getColumnModel().getColumn(3).setResizable(false);
        }

        LblGantt.setText("Diagrama de Gantt");

        LblProcess.setText("Proceso actual:");

        LblTimer.setText("Tiempo transcurrido:");

        BtnIniciar.setText("Iniciar");
        BtnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnIniciarActionPerformed(evt);
            }
        });

        BtnLimpiar.setText("Limpiar");
        BtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelGanttLayout = new javax.swing.GroupLayout(PanelGantt);
        PanelGantt.setLayout(PanelGanttLayout);
        PanelGanttLayout.setHorizontalGroup(
            PanelGanttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGanttLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelGanttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(PanelGanttLayout.createSequentialGroup()
                        .addComponent(LblGantt)
                        .addGap(30, 30, 30)
                        .addComponent(LblProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(PanelGanttLayout.createSequentialGroup()
                .addComponent(BtnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PanelGanttLayout.setVerticalGroup(
            PanelGanttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGanttLayout.createSequentialGroup()
                .addGroup(PanelGanttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnIniciar)
                    .addComponent(BtnLimpiar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelGanttLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblGantt)
                    .addComponent(LblProcess)
                    .addComponent(LblTimer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelProcesosSJF.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TablaProcesosSJF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaProcesosSJF.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane4.setViewportView(TablaProcesosSJF);

        LblProcSJF.setText("Tabla de procesos SJF");

        javax.swing.GroupLayout PanelProcesosSJFLayout = new javax.swing.GroupLayout(PanelProcesosSJF);
        PanelProcesosSJF.setLayout(PanelProcesosSJFLayout);
        PanelProcesosSJFLayout.setHorizontalGroup(
            PanelProcesosSJFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosSJFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelProcesosSJFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .addGroup(PanelProcesosSJFLayout.createSequentialGroup()
                        .addComponent(LblProcSJF)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelProcesosSJFLayout.setVerticalGroup(
            PanelProcesosSJFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosSJFLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblProcSJF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelProcesosRR.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TablaProcesosRR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaProcesosRR.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane5.setViewportView(TablaProcesosRR);

        LblProcT2.setText("Tabla de procesos RR");

        javax.swing.GroupLayout PanelProcesosRRLayout = new javax.swing.GroupLayout(PanelProcesosRR);
        PanelProcesosRR.setLayout(PanelProcesosRRLayout);
        PanelProcesosRRLayout.setHorizontalGroup(
            PanelProcesosRRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosRRLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelProcesosRRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .addGroup(PanelProcesosRRLayout.createSequentialGroup()
                        .addComponent(LblProcT2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelProcesosRRLayout.setVerticalGroup(
            PanelProcesosRRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProcesosRRLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblProcT2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PanelGenerados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelProcesosFCFS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PanelProcesosSJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelProcesosRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PanelGantt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PanelGenerados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelProcesosFCFS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelProcesosSJF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanelProcesosRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(PanelGantt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGenerarActionPerformed
        try{
            this.quantum = Integer.parseInt(this.TxtQuantum.getText());
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "El valor de quantum no es válido.", "Error", 0);
        }
        
        Proceso proc = this.generarProceso();
        if(proc.getPriority() == 2){
            ArrayList<Proceso> proc_rr_gen = new ArrayList<>();
            proc_rr_gen.add(proc);
            int burst = proc.getBurst();
            int count = 1;
            while(burst > this.quantum){                
                Proceso proc_gen = new Proceso(proc.getName() +" Gen"+count ,proc.getArrive(),burst - this.quantum, proc.getPriority());
                proc_rr_gen.add(proc_gen);
                burst -= this.quantum;
                count++;
            }
            for(Proceso p: proc_rr_gen){
                this.insertarProceso(p);
            }
        }
        else{
              this.insertarProceso(proc);
        }
    }//GEN-LAST:event_BtnGenerarActionPerformed

    private void BtnInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInsertarActionPerformed
        this.insertarTabla();
    }//GEN-LAST:event_BtnInsertarActionPerformed

    private void BtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnIniciarActionPerformed
        this.iniciarEjecucion();
    }//GEN-LAST:event_BtnIniciarActionPerformed

    private void BtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLimpiarActionPerformed
        this.limpiarTablas();
    }//GEN-LAST:event_BtnLimpiarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GanttFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GanttFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GanttFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GanttFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GanttFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnGenerar;
    private javax.swing.JButton BtnIniciar;
    private javax.swing.JButton BtnInsertar;
    private javax.swing.JButton BtnLimpiar;
    private javax.swing.JLabel LblGantt;
    private javax.swing.JLabel LblProc;
    private javax.swing.JLabel LblProcFCFS;
    private javax.swing.JLabel LblProcSJF;
    private javax.swing.JLabel LblProcT2;
    private javax.swing.JLabel LblProcess;
    private javax.swing.JLabel LblQuan;
    private javax.swing.JLabel LblTimer;
    private javax.swing.JPanel PanelGantt;
    private javax.swing.JPanel PanelGenerados;
    private javax.swing.JPanel PanelProcesosFCFS;
    private javax.swing.JPanel PanelProcesosRR;
    private javax.swing.JPanel PanelProcesosSJF;
    private javax.swing.JTable TablaGantt;
    private javax.swing.JTable TablaGenerados;
    private javax.swing.JTable TablaProcesosFCFS;
    private javax.swing.JTable TablaProcesosRR;
    private javax.swing.JTable TablaProcesosSJF;
    private javax.swing.JTextField TxtQuantum;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables
}
