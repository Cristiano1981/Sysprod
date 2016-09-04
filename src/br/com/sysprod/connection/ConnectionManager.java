package br.com.sysprod.connection;

import br.com.sysprod.vo.BD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.PropriedadesBD;

/**
 * @author Cristiano Bombazar
 */
public class ConnectionManager {
    
     private static final String STR_DRIVER = "org.postgresql.Driver";
     private static String STR_CON = null;
     public static Connection conn = null;
    
    public static boolean openConnection() {
        BD bd = new PropriedadesBD().readyPropertiesBD();
        STR_CON = "jdbc:postgresql://" + bd.getHost() + ":" + bd.getPorta() + "/" + bd.getDatabase();
        Properties info = new Properties();
        info.setProperty("user", bd.getUser());
        info.setProperty("password", bd.getPassword());
        info.setProperty("loginTimeout", "10");
        try {
            Class.forName(STR_DRIVER);
            conn = DriverManager.getConnection(STR_CON, info);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.errBD(e, bd);
            return false;
        }
        return true;
    }
    
    public static Connection getConnection(){
        if (conn == null){
            openConnection();
        }
        return conn;
    }
    
    public static void closeAll(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nao foi possível fechar a conexão com o banco.");
            e.printStackTrace();
        }
    }
    
    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (conn != null || stmt != null) {
                closeAll(conn, stmt);
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nao foi possível fechar a conexão com o banco.");
            e.printStackTrace();
        }
    }
    
    public static void closeAll(Connection conn, Statement stmt) {
        try {
            if (conn != null) {
                closeAll(conn);
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nao foi possível fechar a conexão com o banco.");
            e.printStackTrace();
        }
    }
    
    public static void closeAll(Statement st, ResultSet rs){
        try {
            if (st != null){
                st.close();
            }
            if  (rs != null){
                rs.close();
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Nao foi possível fechar a conexão com o banco.");
            e.printStackTrace();
        }
    }
}
