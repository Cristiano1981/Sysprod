package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Unidade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class UnidadeBD implements Crud<Unidade>{

    @Override
    public boolean saveOrUpdate(Unidade e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_unidade = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.unidade");
            sql.append("   SET id_unidade = ?,");
            sql.append("       fantasia   = ?,");
            sql.append("       ativo      = ?");
            sql.append(" WHERE id_unidade = ?");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.unidade(id_unidade, fantasia, ativo)");
            sql.append("     VALUES (?, ?, ?)");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setString(2, e.getFantasia());
        st.setBoolean(3, e.isAtivo());
        if (isUpdate){
            st.setInt(4, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Unidade e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.unidade");
        sql.append("      WHERE id_unidade = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_unidade, fantasia, ativo FROM sys.unidade ";
    }
    
    @Override
    public Unidade find(Integer codigo) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        Unidade           unidade = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_unidade = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            unidade = new Unidade();
            unidade.setId(rs.getInt("id_unidade"));
            unidade.setFantasia(rs.getString("fantasia"));
            unidade.setAtivo(rs.getBoolean("ativo"));
        }
        ConnectionManager.closeAll(st, rs);
        return unidade;
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_unidade),0)+1 as codigo");
        sql.append("  FROM sys.unidade;");
        con = ConnectionManager.getConnection();
        st  = con.prepareCall(sql.toString());
        rs  = st.executeQuery();
        if (rs.next()){
            next = rs.getInt("codigo");
        }
        ConnectionManager.closeAll(st, rs);
        return next;
    }

    @Override
    public List<Unidade> queryAll(String filtro, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Unidade>     listaUnidade = null;
        Unidade           unidade      = null;
        StringBuilder     sql          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_unidade;");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaUnidade == null){
                listaUnidade = new ArrayList<Unidade>();
            }
            unidade = new Unidade();
            unidade.setId(rs.getInt("id_unidade"));
            unidade.setFantasia(rs.getString("fantasia"));
            unidade.setAtivo(rs.getBoolean("ativo"));
            listaUnidade.add(unidade);
        }
        ConnectionManager.closeAll(st, rs);
        return listaUnidade;
    }
    
    public List<Unidade> queryAll(Integer ativos, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Unidade>     listaUnidade = null;
        Unidade           unidade      = null;
        StringBuilder     sql          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (ativos != 2){
            boolean ativo = ativos == 0;
            sql.append("WHERE ativo is ").append(ativo);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_unidade;");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaUnidade == null){
                listaUnidade = new ArrayList<Unidade>();
            }
            unidade = new Unidade();
            unidade.setId(rs.getInt("id_unidade"));
            unidade.setFantasia(rs.getString("fantasia"));
            unidade.setAtivo(rs.getBoolean("ativo"));
            listaUnidade.add(unidade);
        }
        ConnectionManager.closeAll(st, rs);
        return listaUnidade;
    }
}
