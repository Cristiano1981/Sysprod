package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 * @author Cristiano Bombazar
 */
public class TableProva extends AbstractTableModel{
    
    private final List<Prova> listaProva;
    private final int CODIGO       = 0;
    private final int TURMA        = 1;
    private final int DT_CADASTRO  = 2;
    private final int DT_APLICACAO = 3;
    private final String[] columns = {"Código", "Turma", "Dt Cadastro", "Dt Aplicação"};
    
    public TableProva(final List<Prova> listaProva) {
        this.listaProva = listaProva;
    }

    @Override
    public int getRowCount() {
        return listaProva.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
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
            case TURMA:
                return String.class;
            case DT_CADASTRO:
                return String.class;
            case DT_APLICACAO:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prova p = listaProva.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(p.getId());
            case TURMA:
                return p.getTurma().getCurso().getDescricao() + " - "+p.getTurma().getFase()+"ª";
            case DT_CADASTRO:
                return Utils.converteDateParaString(p.getDtCadastro());
            case DT_APLICACAO:
                return Utils.converteDateParaString(p.getDtAplicacao());
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
