package br.com.sysprod.model;

import br.com.sysprod.vo.Usuario;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import br.com.sysprod.util.Utils;

/**
 * @author Cristiano
 */
public class TableUsuario extends AbstractTableModel{

    List<Usuario> listaUsuario;
    private static final int CODIGO = 0;
    private static final int LOGIN  = 1;
    private static final int NOME   = 2;
    private static final String[] columns = {"Código", "Login", "Nome"};
    
    public TableUsuario() {
    }
    
    public TableUsuario(List<Usuario> listaUsuario){
        this.listaUsuario = listaUsuario;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return listaUsuario.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case CODIGO:
                return String.class;
            case LOGIN:
                return String.class;
            case NOME:
                return String.class;
            default:
               throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario user = listaUsuario.get(rowIndex);
        switch (columnIndex){
            case CODIGO:
                return Utils.codigoFormatado(user.getId());
            case LOGIN:
                return user.getLogin();
            case NOME:
                return user.getNome();
            default:
               throw new ArrayIndexOutOfBoundsException("Número de colunas inválido");
        }
    }
}
