package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Turma;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableTurma extends AbstractTableModel{

    private List<Turma> listaAcademicoFase;
    private static final int CODIGO  = 0;
    private static final int CURSO   = 1;
    private static final int PERIODO = 2;
    private static final int FASE    = 3;
    private final String[] columns = {"Código", "Curso", "Período", "Fase"};
    
    public TableTurma() {
    }
    
    public TableTurma(List<Turma> listaAcademicoFase){
        this.listaAcademicoFase = listaAcademicoFase;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaAcademicoFase.size();
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
            case CURSO:
                return String.class;
            case PERIODO:
                return String.class;
            case FASE:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Turma acad = listaAcademicoFase.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(acad.getId());
            case CURSO:
                return acad.getCurso().getDescricao();
            case PERIODO:
                return acad.getPeriodo().getDescricao();
            case FASE:
                return acad.getFase()+"ª";
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
