package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.TurmaDet;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */

public class TableTurmaDet extends AbstractTableModel{

    private List<TurmaDet> listaTurmaDet;
    private static final int CODIGO = 0;
    private static final int NOME   = 1;
    private final String[] columns = {"Código", "Nome"};
    
    public TableTurmaDet() {
    }
    
    public TableTurmaDet(List<TurmaDet> listaTurmaDet){
        this.listaTurmaDet = listaTurmaDet;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaTurmaDet.size();
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
            case NOME:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TurmaDet a = listaTurmaDet.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(a.getAcademico().getCodigo());
            case NOME:
                return a.getAcademico().getNome();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
