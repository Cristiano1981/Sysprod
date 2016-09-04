package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Periodo;
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
public class PeriodoBD implements Crud<Periodo>{

    @Override
    public boolean saveOrUpdate(Periodo e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_periodo = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.periodo");
            sql.append("   SET id_periodo = ?,");
            sql.append("       dt_inicio  = ?,");
            sql.append("       dt_fim     = ?,");
            sql.append("       encerrado  = ?,");
            sql.append("       descricao  = ?");
            sql.append( "WHERE id_periodo = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.periodo(id_periodo, dt_inicio, dt_fim, encerrado, descricao)");
            sql.append("     VALUES (?, ?, ?, ?, ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setDate(2, Utils.converteDateBD(e.getDtInicio()));
        st.setDate(3, Utils.converteDateBD(e.getDtFim()));
        st.setBoolean(4, e.isEncerrado());
        st.setString(5, e.getDescricao());
        if (isUpdate){
            st.setInt(6, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Periodo e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.periodo");
        sql.append("      WHERE id_periodo = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Periodo find(Integer codigo) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        Periodo           periodo = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_periodo = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            periodo = new Periodo();
            periodo.setId(rs.getInt("id_periodo"));
            periodo.setDtInicio(rs.getDate("dt_inicio"));
            periodo.setDtFim(rs.getDate("dt_fim"));
            periodo.setEncerrado(rs.getBoolean("encerrado"));
            periodo.setDescricao(rs.getString("descricao"));
        }
        ConnectionManager.closeAll(st, rs);
        return periodo;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_periodo, dt_inicio, dt_fim, encerrado, descricao FROM sys.periodo ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_periodo),0)+1 as codigo");
        sql.append("  FROM sys.periodo;");
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
    public List<Periodo> queryAll(String filtro, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Periodo>     listaPeriodo = null;
        Periodo           periodo      = null;
        StringBuilder     sql          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_periodo;");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaPeriodo == null){
                listaPeriodo = new ArrayList<Periodo>();
            }
            periodo = new Periodo();
            periodo.setId(rs.getInt("id_periodo"));
            periodo.setDtInicio(rs.getDate("dt_inicio"));
            periodo.setDtFim(rs.getDate("dt_fim"));
            periodo.setEncerrado(rs.getBoolean("encerrado"));
            periodo.setDescricao(rs.getString("descricao"));
            listaPeriodo.add(periodo);
        }
        ConnectionManager.closeAll(st, rs);
        return listaPeriodo;
    }
    
}
