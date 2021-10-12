/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sebas
 */
public class StatusColumnCellRenderer extends DefaultTableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

    //Cells are by default rendered as a JLabel.
    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    


    //Get the status for the current row.
    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
    
    if(tableModel.getValueAt(row, col).equals(" ")){
        l.setBackground(Color.white);
    }
    
    
    if(tableModel.getValueAt(row, col).equals("A")  && col !=0){
        l.setBackground(Color.blue);
        l.setForeground(Color.blue);
    }
        
    if(tableModel.getValueAt(row, col).equals("B")  && col !=0){
        l.setBackground(Color.red);
        l.setForeground(Color.red);
        }
       
    if(tableModel.getValueAt(row, col).equals("C")  && col !=0){
        l.setBackground(Color.green);
        l.setForeground(Color.green);
        }
       
        
    if(tableModel.getValueAt(row, col).equals("D")  && col !=0){
        l.setBackground(Color.orange);
        l.setForeground(Color.orange);
        }
   
     if(tableModel.getValueAt(row, col).equals("E")  && col !=0){
        l.setBackground(Color.cyan);
        l.setForeground(Color.cyan);
        }
      
     if(tableModel.getValueAt(row, col).equals("F")  && col !=0){
        l.setBackground(Color.magenta);
        l.setForeground(Color.magenta);
        }
       
       
    
  //Return the JLabel which renders the cell.
  return l;
  }
}
  