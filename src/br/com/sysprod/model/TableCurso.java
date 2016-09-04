package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Curso;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableCurso extends AbstractTableModel{

    private List<Curso> listaCurso;
    private static final int CODIGO    = 0;
    private static final int DESCRICAO = 1;
    private static final int UNIDADE   = 2;
    private final String[] columns     = {"Código", "Curso", "Unidade/Campos"};
    
    public TableCurso() {
    }

    public TableCurso(List<Curso> listaCurso){
        this.listaCurso = listaCurso;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaCurso.size();
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
            case UNIDADE:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Curso c = listaCurso.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(c.getId());
            case DESCRICAO:
                return c.getDescricao();
            case UNIDADE:
                return c.getUnidade().getFantasia();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
