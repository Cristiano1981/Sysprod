package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Curso;
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
public class CursoBD implements Crud<Curso>{

    @Override
    public boolean saveOrUpdate(Curso e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_curso = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.curso");
            sql.append("   SET id_curso   = ?,");
            sql.append("       id_unidade = ?,");
            sql.append("       descricao  = ?,");
            sql.append("       ativo      = ?");
            sql.append(" WHERE id_curso   = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.curso(id_curso,id_unidade,descricao,ativo)");
            sql.append("     VALUES (?, ?, ?, ?)");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setInt(2, e.getUnidade().getId());
        st.setString(3, e.getDescricao());
        st.setBoolean(4, e.isAtivo());
        if (isUpdate){
            st.setInt(5, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Curso e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.curso");
        sql.append("      WHERE id_curso = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Curso find(Integer codigo) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        Curso             curso   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_curso = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            curso = new Curso();
            curso.setId(rs.getInt("id_curso"));
            Integer idUnidade = rs.getInt("id_unidade");
            curso.setUnidade(new UnidadeBD().find(idUnidade));
            curso.setDescricao(rs.getString("descricao"));
            curso.setAtivo(rs.getBoolean("ativo"));
        }
        ConnectionManager.closeAll(st, rs);
        return curso;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_curso, id_unidade, descricao, ativo FROM sys.curso ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_curso),0)+1 as codigo");
        sql.append("  FROM sys.curso;");
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
    public List<Curso> queryAll(String filtro, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Curso>       listaCurso   = null;
        Curso             curso        = null;
        StringBuilder    sql           = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_unidade = ? ");
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_curso");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaCurso == null){
                listaCurso = new ArrayList<Curso>();
            }
            curso = new Curso();
            curso.setId(rs.getInt("id_curso"));
            int idUnidade = rs.getInt("id_unidade");
            curso.setUnidade(new UnidadeBD().find(idUnidade));
            curso.setDescricao(rs.getString("descricao"));
            curso.setAtivo(rs.getBoolean("ativo"));
            listaCurso.add(curso);
        }
        ConnectionManager.closeAll(st, rs);
        return listaCurso;
    }
}
