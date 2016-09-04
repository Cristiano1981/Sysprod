package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.vo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class UsuarioBD implements Crud<Usuario>{

    @Override
    public boolean saveOrUpdate(Usuario e) throws SQLException {
        Connection        con      = null;
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_usuario = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareCall(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.usuario");
            sql.append("   SET id_usuario = ?,");
            sql.append("       nome       = ?,");
            sql.append("       login      = ?,");
            sql.append("       senha      = senha,");
            sql.append("       ativo      = ?");
            sql.append(" WHERE id_usuario = ?;");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.usuario(id_usuario, nome,login,senha,ativo)");
            sql.append("     VALUES (?, ?, ?, md5(?), ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setString(2, e.getNome());
        st.setString(3, e.getLogin());
        if (!isUpdate){
            st.setString(4, e.getSenha());
            st.setBoolean(5, e.isAtivo());
        }else{
            st.setBoolean(4, e.isAtivo());
        }
        if (isUpdate){
            st.setInt(5, e.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    @Override
    public boolean delete(Usuario e) throws SQLException {
        Connection        con = null;
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.usuario");
        sql.append("      WHERE id_usuario = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    public Usuario find(Integer codigo) throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        StringBuilder     sql  = null;
        Usuario           user = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_usuario = ?");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        if (rs.next()){
            user = new Usuario();
            user.setId(rs.getInt("id_usuario"));
            user.setNome(rs.getString("nome"));
            user.setLogin(rs.getString("login"));
            user.setAtivo(rs.getBoolean("ativo"));
            user.setSenha(rs.getString("senha"));
        }
        ConnectionManager.closeAll(st, rs);
        return user;
    }
    
    @Override
    public String getDefaultQuery() {
        return "SELECT id_usuario, nome, login, senha, ativo FROM sys.usuario ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_usuario),0)+1 as codigo");
        sql.append("  FROM sys.usuario;");
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
    public List<Usuario> queryAll(String filtro, String order) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    public List<Usuario> queryAll(Integer ativos, String order) throws SQLException {
        Connection        con          = null;
        PreparedStatement st           = null;
        ResultSet         rs           = null;
        List<Usuario>     listaUsuario = null;
        Usuario           user         = null;
        StringBuilder     sql          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (ativos != 2){
            boolean ativo = ativos == 0;
            sql.append("WHERE ativo is "+ativo);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_usuario;");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaUsuario == null){
                listaUsuario = new ArrayList<Usuario>();
            }
            user = new Usuario();
            user.setId(rs.getInt("id_usuario"));
            user.setNome(rs.getString("nome"));
            user.setLogin(rs.getString("login"));
            user.setAtivo(rs.getBoolean("ativo"));
            listaUsuario.add(user);
        }
        ConnectionManager.closeAll(st, rs);
        return listaUsuario;
    }

    public Usuario validaLogin(String login, String senha) throws SQLException{
        StringBuilder     sql    = null;
        Connection        con    = null;
        PreparedStatement st     = null;
        ResultSet         rs     = null;
        Usuario           user   = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE login = ?");
        sql.append("   AND senha = md5(?)");
        con = ConnectionManager.getConnection();
        st = con.prepareCall(sql.toString());
        st.setString(1, login);
        st.setString(2, senha);
        rs = st.executeQuery();
        if (rs.next()){
            user = new Usuario();
            user.setId(rs.getInt("id_usuario"));
            user.setNome(rs.getString("nome"));
            user.setLogin(rs.getString("login"));
        }
        ConnectionManager.closeAll(st, rs);
        return user;
    }
}
