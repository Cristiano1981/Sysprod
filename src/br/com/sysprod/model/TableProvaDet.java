package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.ProvaDet;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableProvaDet extends AbstractTableModel{
    
    
    private final List<ProvaDet> listaProvaDet;
    private static final int CODIGO     = 0;
    private static final int DESCRICAO  = 1;
    private static final String[] columns = {"Código", "Descrição"};

    public TableProvaDet(final List<ProvaDet> listaProvaDet) {
        this.listaProvaDet = listaProvaDet;
    }

    @Override
    public int getRowCount() {
       return listaProvaDet.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
       return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case CODIGO:
                return String.class;
            case DESCRICAO:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProvaDet p = listaProvaDet.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return rowIndex+1; //Ordem da pergunta
            case DESCRICAO:
                return p.getPergunta().getDescricao();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
