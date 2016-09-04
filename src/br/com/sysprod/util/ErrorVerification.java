package br.com.sysprod.util;

import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.constantes.ConstanteFile;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import org.postgresql.util.PSQLException;
import br.com.sysprod.vo.BD;

/**
 * @author Cristiano Bombazar
 */
public class ErrorVerification {
    
    /**
        Tratar exceções do banco de dados;
        Exemplo: Data base não existe, falta de driver e etc...
    */
    public static void errBD(Exception e, BD bd){
        if (e instanceof ClassNotFoundException){
            ClassNotFoundException c = (ClassNotFoundException) e;
            String texto = c.getMessage();
            JOptionPane.showMessageDialog(null, ConstanteBD.DRIVER_ERRO, "Driver n�o encontrado.", JOptionPane.ERROR_MESSAGE);
            Log.err(texto);
            
        }else if(e instanceof PSQLException){
            PSQLException pl = (PSQLException) e;
            String texto = pl.getMessage();
            
            if (texto.contains(ConstanteBD.CONNECTION_DATABASE_DOES_NOT_EXIST)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_BD_NOT_EXISTS+bd.getDatabase().toUpperCase(), "Database n�o existe", JOptionPane.ERROR_MESSAGE);
                
            }else if (texto.contains(ConstanteBD.CONNECTION_FAILED_AUTHENTICATION)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_BD_AUTHENTICATION_FAILED, "Falha na autentica��o", JOptionPane.ERROR_MESSAGE);
                
            }else if (texto.contains(ConstanteBD.CONNECTION_REFUSED) || texto.contains(ConstanteBD.CONNECTION_REFUSED_PTBR)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_BD_CONNECTION_REFUSED, "Conex�o recusada", JOptionPane.ERROR_MESSAGE);
                
            }else if (texto.contains(ConstanteBD.CONNECTION_FAILED)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_BD_CONNECTION_REFUSED, "Falha na conex�o", JOptionPane.ERROR_MESSAGE);
                
            }else if (texto.contains(ConstanteBD.CONNECTION_FAILED_2)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_BD_CONNECTION_REFUSED, "Falha na conex�o", JOptionPane.ERROR_MESSAGE);
                
            }else if (texto.contains(ConstanteBD.CONNECTION_PG_HBA)){
                JOptionPane.showMessageDialog(null, ConstanteBD.MESSAGE_PG_HBA, "Configure o pg_hba", JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, texto, "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            
            Log.err(texto);
        }else{
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            Log.err(e.getMessage());
        } 
    }
    
    public static void errFile(Exception e){
        if (e instanceof FileNotFoundException){
            FileNotFoundException f = (FileNotFoundException) e;
            String texto = f.getLocalizedMessage();
            
            if (texto.contains(ConstanteFile.FILE_ACCESS_DENIED) || texto.contains(ConstanteFile.FILE_ACCESS_DENIED_PT_BR)){
                JOptionPane.showMessageDialog(null, ConstanteFile.MESSAGE_ACCESS_DENIED, "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, texto, "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            
            Log.err(texto);
        }else{
            JOptionPane.showMessageDialog(null, "Erro no arquivo", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            Log.err(e.getMessage());
        }
    }
    
    public static void ErrDetalhe(Exception e, String mensagem){
        String msgEspecifica = e.getMessage();
        if (msgEspecifica.contains("violates foreign key constraint")){
            msgEspecifica = "N�o foi poss�vel salvar/deletar. Vioala��o de restri��o de tabelas.";
        }else if (msgEspecifica.contains("duplicate key value violates unique constraint")){
            msgEspecifica = "N�o foi poss�vel gravar registro. Viola restri��o de integridade. Verifique duplicidade de registro.";
        }
        int result = JOptionPane.showConfirmDialog(null, mensagem+". Deseja mostrar detalhe do erro?", "Erro", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (result == JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(null, msgEspecifica, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
