package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Academico;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class AcademicoBD implements Crud<Academico>{

    @Override
    public boolean saveOrUpdate(Academico e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        Boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_academico = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.academico");
            sql.append("   SET codigo       = ?,");
            sql.append("       nome         = ?,");
            sql.append("       ativo        = ?,");
            sql.append("       id_curso     = ?,");
            sql.append("       id_unidade   = ? ");
            sql.append(" WHERE id_academico = ? ");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.academico(codigo, nome, ativo, id_curso, id_unidade)");
            sql.append("     VALUES (?, ?, ?, ?, ?);");
        }
        String s = new String(sql.toString().getBytes(), StandardCharsets.ISO_8859_1);
        st = con.prepareStatement(s);
        st.setInt(1, e.getCodigo());
        st.setString(2, e.getNome());
        st.setBoolean(3, e.isAtivo());
        st.setInt(4, e.getCurso().getId());
        st.setInt(5, e.getUnidade().getId());
        if (isUpdate){
            st.setInt(6, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Academico e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.academico");
        sql.append(" WHERE id_academico = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
     /**
     * Busca por id
     * @param id academico
     * @return
     * @throws SQLException 
     */
    public Academico find(Integer codigo) throws SQLException {
        Connection        con         = null;
        PreparedStatement st          = null;
        ResultSet         rs          = null;
        StringBuilder     sql         = null;
        Academico         academico   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_academico = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            academico = new Academico();
            academico.setId(rs.getInt("id_academico"));
            academico.setCodigo(rs.getInt("codigo"));
            academico.setNome(rs.getString("nome"));
            academico.setAtivo(rs.getBoolean("ativo"));
            academico.setCurso(new CursoBD().find(rs.getInt("id_curso")));
            academico.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
        }
        ConnectionManager.closeAll(st, rs);
        return academico;
    }
    
    /**
     * Busca por codigo + Unidade
     * @param codigo
     * @return
     * @throws SQLException 
     */
    public Academico findByCodigo(Integer codigo) throws SQLException {
        Connection        con         = null;
        PreparedStatement st          = null;
        ResultSet         rs          = null;
        StringBuilder     sql         = null;
        Academico         academico   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE codigo = ?");
        sql.append("   AND id_unidade = ?;");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        st.setInt(2, Global.getUnidade().getId());
        rs  = st.executeQuery();
        if (rs.next()){
            academico = new Academico();
            academico.setId(rs.getInt("id_academico"));
            academico.setCodigo(rs.getInt("codigo"));
            academico.setNome(rs.getString("nome"));
            academico.setAtivo(rs.getBoolean("ativo"));
            academico.setCurso(new CursoBD().find(rs.getInt("id_curso")));
            academico.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
        }
        ConnectionManager.closeAll(st, rs);
        return academico;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_academico,codigo, nome, ativo, id_unidade, id_curso FROM sys.academico ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(codigo),0)+1 as codigo");
        sql.append("  FROM sys.academico;");
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
    public List<Academico> queryAll(String filtro, String order) throws SQLException {
        Connection        con            = null;
        PreparedStatement st             = null;
        ResultSet         rs             = null;
        List<Academico>   listaAcademico = null;
        Academico             academico  = null;
        StringBuilder    sql             = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_unidade = ? ");
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY codigo");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaAcademico == null){
                listaAcademico = new ArrayList<Academico>();
            }
            academico = new Academico();
            academico.setId(rs.getInt("id_academico"));
            academico.setCodigo(rs.getInt("codigo"));
            academico.setNome(rs.getString("nome"));
            academico.setAtivo(rs.getBoolean("ativo"));
            academico.setCurso(new CursoBD().find(rs.getInt("id_curso")));
            academico.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
            listaAcademico.add(academico);
        }
        ConnectionManager.closeAll(st, rs);
        return listaAcademico;
    }
}
