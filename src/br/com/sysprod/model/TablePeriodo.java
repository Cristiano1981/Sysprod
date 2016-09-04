package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Periodo;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TablePeriodo extends AbstractTableModel{

    private List<Periodo> listaPeriodo;
    private static final int CODIGO    = 0;
    private static final int DESCRICAO = 1;
    private static final int DT_INICIO = 2;
    private static final int DT_FIM    = 3;
    private static final int ENCERRADO = 4;
    
    private final String[] columns = {"Código", "Descrição","Dt Inicio", "Dt Encerramento", "Encerrado"};
    
    public TablePeriodo() {
    }
    
    
    public TablePeriodo(List<Periodo> listaPeriodo){
        this.listaPeriodo = listaPeriodo;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaPeriodo.size();
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
            case DT_INICIO:
                return String.class;
            case DT_FIM:
                return String.class;
            case ENCERRADO:
                return Boolean.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Periodo p = listaPeriodo.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(p.getId());
            case DESCRICAO:
                return p.getDescricao();
            case DT_INICIO:
                return Utils.converteDateParaString(p.getDtInicio());
            case DT_FIM:
                return Utils.converteDateParaString(p.getDtFim());
            case ENCERRADO:
                return p.isEncerrado();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
