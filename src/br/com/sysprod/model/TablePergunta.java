package br.com.sysprod.model;

import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Pergunta;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public class TablePergunta extends AbstractTableModel{
    
    private List<Pergunta> listaPergunta;
    private final int CODIGO     = 0;
    private final int DESCRICAO  = 1;
    private final int DISCIPLINA = 2;
    private final String[] columns = {"Código", "Descrição", "Disciplina"};

    public TablePergunta(List<Pergunta> listaPergunta) {
        this.listaPergunta = listaPergunta;
    }

    @Override
    public int getRowCount() {
        return listaPergunta.size();
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
            case DESCRICAO:
                return String.class;
            case DISCIPLINA:
                return String.class;
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pergunta p = listaPergunta.get(rowIndex);
        switch(columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(p.getId());
            case DESCRICAO:
                return p.getDescricao();
            case DISCIPLINA:
                return p.getDisciplina().getNome();
            default:
                throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");  
        }
    }
}
