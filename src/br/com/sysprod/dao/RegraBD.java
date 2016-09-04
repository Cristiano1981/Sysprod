package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Regra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class RegraBD implements Crud<Regra>{

    @Override
    public boolean saveOrUpdate(Regra e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_regra = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.regra");
            sql.append("   SET id_regra   = ?,");
            sql.append("       fase       = ?,");
            sql.append("       acertos    = ?,");
            sql.append("       arredondar = ?,");
            sql.append("       ativo      = ? ");
            sql.append(" WHERE id_regra   = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.regra(id_regra, fase, acertos, arredondar, ativo)");
            sql.append("     VALUES (?, ?, ?, ?, ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setInt(2, e.getFase());
        st.setInt(3, e.getAcertos());
        st.setString(4, e.getArredondar());
        st.setBoolean(5, e.isAtivo());
        if (isUpdate){
            st.setInt(6, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Regra e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.regra");
        sql.append("      WHERE id_regra = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Regra find(Integer codigo) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        Regra             regra   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_regra = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            regra = new Regra();
            regra.setId(rs.getInt("id_regra"));
            regra.setFase(rs.getInt("fase"));
            regra.setAcertos(rs.getInt("acertos"));
            regra.setArredondar(rs.getString("arredondar"));
            regra.setAtivo(rs.getBoolean("ativo"));
        }
        ConnectionManager.closeAll(st, rs);
        return regra;
    }
    
    public Regra find(Integer fase, Integer acertos) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        Regra             regra   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE fase    = ?");
        sql.append("   AND acertos = ?");
        sql.append("   AND ativo is true;");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, fase);
        st.setInt(2, acertos);
        rs  = st.executeQuery();
        if (rs.next()){
            regra = new Regra();
            regra.setId(rs.getInt("id_regra"));
            regra.setFase(rs.getInt("fase"));
            regra.setAcertos(rs.getInt("acertos"));
            regra.setArredondar(rs.getString("arredondar"));
            regra.setAtivo(rs.getBoolean("ativo"));
        }
        ConnectionManager.closeAll(st, rs);
        return regra;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_regra, fase, acertos, arredondar, ativo FROM sys.regra ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_regra),0)+1 as codigo");
        sql.append("  FROM sys.regra;");
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
    public List<Regra> queryAll(String filtro, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Regra>       listaRegra   = null;
        Regra             regra        = null;
        StringBuilder    sql           = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_regra");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaRegra == null){
                listaRegra = new ArrayList<Regra>();
            }
            regra = new Regra();
            regra.setId(rs.getInt("id_regra"));
            regra.setFase(rs.getInt("fase"));
            regra.setAcertos(rs.getInt("acertos"));
            regra.setArredondar(rs.getString("arredondar"));
            regra.setAtivo(rs.getBoolean("ativo"));
            listaRegra.add(regra);
        }
        ConnectionManager.closeAll(st, rs);
        return listaRegra;
    }
}
