package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Disciplina;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableDisciplina extends AbstractTableModel{
    
    private List<Disciplina> listaDisciplinas;
    private final int CODIGO = 0;
    private final int NOME   = 1;
    private final int FASE   = 2;
    private final String[] columns = {"Código", "Nome", "Fase"};

    public TableDisciplina() {
    }

    public TableDisciplina(List<Disciplina> listaDisciplinas) {
        this.listaDisciplinas = listaDisciplinas;
    }

    @Override
    public int getRowCount() {
        return listaDisciplinas.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case CODIGO:
                return String.class;
            case NOME:
                return String.class;
            case FASE:
                return String.class;
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
        Disciplina d = listaDisciplinas.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(d.getId());
            case NOME:
                return d.getNome();
            case FASE:
                return d.getFase()+ "ª";
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
