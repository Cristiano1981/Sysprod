package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.ProvaDetAcademico;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TableProvaDetAcademico extends AbstractTableModel {

    private List<ProvaDetAcademico> listaProvaDetAcademico;
    private static final int CODIGO  = 0;
    private static final int LETRA   = 1;
    private static final int CORRETO = 2;
    private static final String[] columns = {"Código/Nome", "Letra", "Correto"};
    
    
    public TableProvaDetAcademico(List<ProvaDetAcademico> listaProvaDetAcademico){
        this.listaProvaDetAcademico = listaProvaDetAcademico;
    }
    
    public TableProvaDetAcademico() {
    }

    @Override
    public int getRowCount() {
        return listaProvaDetAcademico.size();
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
            case LETRA:
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
        ProvaDetAcademico item = listaProvaDetAcademico.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(item.getAcademico().getCodigo())+" - "+item.getAcademico().getNome();
            case LETRA:
                return item.getAssinalado();
            case CORRETO:
                return item.isCorreta();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido.");
        }
    }
}
