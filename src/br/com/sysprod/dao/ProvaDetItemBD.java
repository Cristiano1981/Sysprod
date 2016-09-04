package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.vo.ProvaDetItem;
import br.com.sysprod.vo.ProvaDetAcademico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class ProvaDetItemBD {
    
    Connection con = null;

    public ProvaDetItemBD() {
    }
    
    public ProvaDetItemBD(Connection con){
        this.con = con;
    }
    
    public boolean saveOrUpdate(ProvaDetItem provaDetItem, Integer provaDet) throws SQLException{
        PreparedStatement st             = null;
        ResultSet         rs             = null;
        StringBuilder     sql            = null;
        boolean           isUpdate       = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_provadetitem = ?");
        st = con.prepareStatement(sql.toString());
        st.setInt(1, provaDetItem.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.provadetitem");
            sql.append("   SET id_provadet     = ?,");
            sql.append("       id_pergunta     = ?,");
            sql.append("       letra           = ?,");
            sql.append("       correto         = ?");
            sql.append(" WHERE id_provadetitem = ? ");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.provadetitem(");
            sql.append("            id_provadet, id_pergunta, letra, correto)");
            sql.append("     VALUES (?, ?, ?, ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, provaDet);
        st.setInt(2, provaDetItem.getPergunta().getId());
        st.setString(3, provaDetItem.getLetra());
        st.setBoolean(4, provaDetItem.isCorreto());
        if (isUpdate){
            st.setInt(5, provaDetItem.getId());
            
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }
    
    private String getDefaultQuery() {
        return "SELECT id_provadetitem, id_provadet, id_pergunta, letra, correto FROM sys.provadetitem ";
    }
    
    public boolean delete(ProvaDetItem e) throws SQLException {
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.provadetitem");
        sql.append("      WHERE id_provadetitem = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }
     
    public List<ProvaDetItem> queryAll(String filtro, String order) throws SQLException{
        Connection                  con                        = null;
        PreparedStatement           st                         = null;
        ResultSet                   rs                         = null;
        StringBuilder               sql                        = null;
        List<ProvaDetItem>          listaProvaDetItem          = null;
        ProvaDetItem                provaDetItem               = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_provadetitem");
        }
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDetItem == null){
                listaProvaDetItem = new ArrayList<>();
            }
            provaDetItem = new ProvaDetItem();
            provaDetItem.setId(rs.getInt("id_provadetitem"));
            provaDetItem.setPergunta(new PerguntaBD().find(rs.getInt("id_pergunta")));
            provaDetItem.setLetra(rs.getString("letra"));
            provaDetItem.setCorreto(rs.getBoolean("correto"));
            listaProvaDetItem.add(provaDetItem);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDetItem;
    }
    
    public List<ProvaDetItem> queryAllByProvaDet(Integer idProvaDet) throws SQLException{
        Connection                  con                        = null;
        PreparedStatement           st                         = null;
        ResultSet                   rs                         = null;
        StringBuilder               sql                        = null;
        List<ProvaDetItem>          listaProvaDetItem          = null;
        ProvaDetItem                provaDetItem               = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_provadet = ?");
        sql.append(" ORDER BY letra");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProvaDet);
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDetItem == null){
                listaProvaDetItem = new ArrayList<>();
            }
            provaDetItem = new ProvaDetItem();
            provaDetItem.setId(rs.getInt("id_provadetitem"));
            provaDetItem.setPergunta(new PerguntaBD().find(rs.getInt("id_pergunta")));
            provaDetItem.setLetra(rs.getString("letra"));
            provaDetItem.setCorreto(rs.getBoolean("correto"));
            listaProvaDetItem.add(provaDetItem);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDetItem;
    }
}
