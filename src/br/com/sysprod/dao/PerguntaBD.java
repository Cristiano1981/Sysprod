package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Pergunta;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano
 */
public class PerguntaBD implements Crud<Pergunta>{

    @Override
    public boolean saveOrUpdate(Pergunta e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_pergunta = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.pergunta");
            sql.append("   SET id_pergunta   = ?,");
            sql.append("       id_disciplina = ?,");
            sql.append("       descricao     = ?,");
            sql.append("       ativo         = ?,");
            sql.append("       tipo          = ?");
            sql.append(" WHERE id_pergunta   = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.pergunta(id_pergunta, id_disciplina, descricao, ativo, tipo)");
            sql.append("     VALUES (?, ?, ?, ?, ?);");
        }
        String s = new String(sql.toString().getBytes(), StandardCharsets.ISO_8859_1);
        st = con.prepareStatement(s);
        st.setInt(1, e.getId());
        st.setInt(2, e.getDisciplina().getId());
        st.setString(3, e.getDescricao());
        st.setBoolean(4, e.isAtivo());
        st.setString(5, e.getTipo());
        if (isUpdate){
            st.setInt(6, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Pergunta e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.pergunta");
        sql.append(" WHERE id_pergunta = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Pergunta find(Integer codigo) throws SQLException {
        Connection        con         = null;
        PreparedStatement st          = null;
        ResultSet         rs          = null;
        StringBuilder     sql         = null;
        Pergunta          pergunta   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_pergunta = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            pergunta = new Pergunta();
            pergunta.setId(rs.getInt("id_pergunta"));
            pergunta.setDisciplina(new DisciplinaBD().find(rs.getInt("id_disciplina")));
            pergunta.setDescricao(rs.getString("descricao"));
            pergunta.setAtivo(rs.getBoolean("ativo"));
            pergunta.setTipo(rs.getString("tipo"));
        }
        ConnectionManager.closeAll(st, rs);
        return pergunta;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_pergunta, id_disciplina, descricao, tipo, ativo FROM sys.pergunta ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_pergunta),0)+1 as codigo");
        sql.append("  FROM sys.pergunta;");
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
    public List<Pergunta> queryAll(String filtro, String order) throws SQLException {
        Connection        con            = null;
        PreparedStatement st             = null;
        ResultSet         rs             = null;
        List<Pergunta>    listaPergunta  = null;
        Pergunta          pergunta       = null;
        StringBuilder    sql             = null;
        sql = new StringBuilder();
        sql.append("SELECT pergunta.*");
        sql.append("  FROM sys.pergunta");
        sql.append("  JOIN sys.disciplina ON (pergunta.id_disciplina = disciplina.id_disciplina)");       
        sql.append(" WHERE disciplina.id_unidade = ?");
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY pergunta.id_pergunta");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaPergunta == null){
                listaPergunta = new ArrayList<Pergunta>();
            }
            pergunta = new Pergunta();
            pergunta.setId(rs.getInt("id_pergunta"));
            pergunta.setDisciplina(new DisciplinaBD().find(rs.getInt("id_disciplina")));
            pergunta.setDescricao(rs.getString("descricao"));
            pergunta.setAtivo(rs.getBoolean("ativo"));
            pergunta.setTipo(rs.getString("tipo"));
            listaPergunta.add(pergunta);
        }
        ConnectionManager.closeAll(st, rs);
        return listaPergunta;
    }
}
