package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class DisciplinaBD implements Crud<Disciplina>{

    @Override
    public boolean saveOrUpdate(Disciplina e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_disciplina = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.disciplina");
            sql.append("   SET id_disciplina  = ?,");
            sql.append("       id_unidade     = ?,");
            sql.append("       nome           = ?,");
            sql.append("       fase           = ?,");
            sql.append("       ativo          = ?");
            sql.append(" WHERE id_disciplina   = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.disciplina(id_disciplina, id_unidade, nome, fase, ativo)");
            sql.append("     VALUES (?, ?, ?, ?, ?)");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setInt(2, e.getUnidade().getId());
        st.setString(3, e.getNome());
        st.setInt(4, e.getFase());
        st.setBoolean(5, e.isAtivo());
        if (isUpdate){
            st.setInt(6, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Disciplina e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.disciplina");
        sql.append("      WHERE id_disciplina = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Disciplina find(Integer codigo) throws SQLException {
        Connection        con        = null;
        PreparedStatement st         = null;
        ResultSet         rs         = null;
        StringBuilder     sql        = null;
        Disciplina        disciplina = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_disciplina = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            disciplina = new Disciplina();
            disciplina.setId(rs.getInt("id_disciplina"));
            Integer idUnidade = rs.getInt("id_unidade");
            disciplina.setUnidade(new UnidadeBD().find(idUnidade));
            disciplina.setNome(rs.getString("nome"));
            disciplina.setFase(rs.getInt("fase"));
            disciplina.setAtivo(rs.getBoolean("ativo"));
        }
        ConnectionManager.closeAll(st, rs);
        return disciplina;
    }

    @Override
    public String getDefaultQuery() {
        return "SELECT id_disciplina, id_unidade, nome, fase, ativo FROM sys.disciplina ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_disciplina),0)+1 as codigo");
        sql.append("  FROM sys.disciplina;");
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
    public List<Disciplina> queryAll(String filtro, String order) throws SQLException {
        Connection        con             = null;
        PreparedStatement st              = null;
        ResultSet         rs              = null;
        List<Disciplina>  listaDisciplina = null;
        Disciplina        disciplina      = null;
        StringBuilder     sql              = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_unidade = ? ");
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_disciplina");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaDisciplina == null){
                listaDisciplina = new ArrayList<Disciplina>();
            }
            disciplina = new Disciplina();
            disciplina.setId(rs.getInt("id_disciplina"));
            int idUnidade = rs.getInt("id_unidade");
            disciplina.setUnidade(new UnidadeBD().find(idUnidade));
            disciplina.setNome(rs.getString("nome"));
            disciplina.setFase(rs.getInt("fase"));
            disciplina.setAtivo(rs.getBoolean("ativo"));
            listaDisciplina.add(disciplina);
        }
        ConnectionManager.closeAll(st, rs);
        return listaDisciplina;
    }
}
