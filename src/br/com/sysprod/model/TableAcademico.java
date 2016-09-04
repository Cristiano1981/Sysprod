package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableAcademico extends AbstractTableModel{

    private List<Academico> listaAcademico;
    private static final int CODIGO  = 0;
    private static final int NOME    = 1;
    private static final int CURSO   = 2;
    private static final int UNIDADE = 3;
    private final String[] columns = {"Código", "Nome", "Curso", "Unidade/Campos"};
    
    public TableAcademico() {
    }
    
    public TableAcademico(List<Academico> listaAcademico){
        this.listaAcademico = listaAcademico;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaAcademico.size();
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
            case CURSO:
                return String.class;
            case UNIDADE:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Academico ac = listaAcademico.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(ac.getCodigo());
            case NOME:
                return ac.getNome();
            case CURSO:
                return ac.getCurso().getDescricao();
            case UNIDADE:
                return ac.getUnidade().getFantasia();            
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
