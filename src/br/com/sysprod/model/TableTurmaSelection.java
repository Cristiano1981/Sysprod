package br.com.sysprod.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class TableTurmaSelection extends AbstractTableModel{

    public TableTurmaSelection() {
    }
    
    public TableTurmaSelection(List lista) {
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List getSelecteds(){
        return new ArrayList();
    }
    
}