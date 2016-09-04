package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Regra;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableRegra extends AbstractTableModel{
    
    private List<Regra> listaRegra;
    private static final int CODIGO     = 0;
    private static final int FASE       = 1;
    private static final int ACERTOS    = 2;
    private static final int ARREDONDAR = 3;
    private static final String[] columns = {"Código", "Fase", "Acertos", "Arredondar"};

    public TableRegra() {
    }

    public TableRegra(List<Regra> listaRegra) {
        this.listaRegra = listaRegra;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaRegra.size();
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
            case FASE:
                return String.class;
            case ACERTOS:
                return Integer.class;
            case ARREDONDAR:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Regra r = listaRegra.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(r.getId());
            case FASE:
                return r.getFase()+" ª";
            case ACERTOS:
                return r.getAcertos();
            case ARREDONDAR:
                return r.getArredondar().equals("+") ? "Para Cima (+)" : "Para baixo (-)";
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
