package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Unidade;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableUnidade extends AbstractTableModel{

    private List<Unidade> listaUnidade;
    private static final int CODIGO   = 0;
    private static final int FANTASIA = 1;
    private static final String[] columns = {"Código", "Nome Fantasia"};
    
    public TableUnidade() {
    }
    
    public TableUnidade(List<Unidade> listaUnidade){
        this.listaUnidade = listaUnidade;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaUnidade.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case CODIGO:
                return String.class;
            case FANTASIA:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
    
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Unidade unidade = listaUnidade.get(rowIndex);
        switch (columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(unidade.getId());
            case FANTASIA:
                return unidade.getFantasia();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
