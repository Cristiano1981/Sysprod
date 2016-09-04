package br.com.sysprod.model;

import br.com.sysprod.vo.ProvaDetItem;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableProvaDetItem extends AbstractTableModel{
    
    private List<ProvaDetItem> listaProvaDetItem;
    private static final int LETRA     = 0;
    private static final int DESCRICAO = 1;
    private static final int CORRETO   = 2;
    private final String[] columns = {"Letra", "Descrição", "Correta"};
    
    public TableProvaDetItem(List<ProvaDetItem> listaProvaDetItem){
        this.listaProvaDetItem = listaProvaDetItem;
    }

    public TableProvaDetItem() {
    }

    @Override
    public int getRowCount() {
        return listaProvaDetItem.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case LETRA:
                return String.class;
            case DESCRICAO:
                return String.class;
            case CORRETO:
                return Boolean.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProvaDetItem item = listaProvaDetItem.get(rowIndex);
        switch(columnIndex){
            case LETRA:
                return item.getLetra();
            case DESCRICAO:
                return item.getPergunta().getDescricao();
            case CORRETO:
                return item.isCorreto();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
