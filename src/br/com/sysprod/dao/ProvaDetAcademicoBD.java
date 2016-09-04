package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.vo.ProvaDetAcademico;
import br.com.sysprod.vo.ProvaDetItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class ProvaDetAcademicoBD {
    
    Connection con = null;

    public ProvaDetAcademicoBD() {
    }
    
    public ProvaDetAcademicoBD(Connection con){
        this.con = con;
    }
    
    public boolean saveOrUpdate(ProvaDetAcademico provaDetAcademico, Integer provaDet) throws SQLException{
        PreparedStatement st             = null;
        ResultSet         rs             = null;
        StringBuilder     sql            = null;
        boolean           isUpdate       = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_provadetacademico = ?");
        st = con.prepareStatement(sql.toString());
        st.setInt(1, provaDetAcademico.getId() == null ? 0 : provaDetAcademico.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.provadetacademico");
            sql.append("   SET id_provadet     = ?,");
            sql.append("       id_academico    = ?,");
            sql.append("       assinalado      = ?,");
            sql.append("       correto         = ?");
            sql.append(" WHERE id_provadetacademico = ? ");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.provadetacademico(");
            sql.append("            id_provadet, id_academico, assinalado, correto)");
            sql.append("     VALUES (?, ?, ?, ?);");
        }
//        System.out.println("------------");
//        System.out.println(sql.toString());
//        System.out.print("ProvaDet: "+provaDet);
//        System.out.print(". Acadêmico: "+provaDetAcademico.getAcademico().getId());
//        System.out.print(". Assinalado: "+provaDetAcademico.getAssinalado());
//        System.out.print(". É correto: "+provaDetAcademico.isCorreta());
//        System.out.println("------------------");
        st = con.prepareStatement(sql.toString());
        st.setInt(1, provaDet);
        st.setInt(2, provaDetAcademico.getAcademico().getId());
        st.setString(3, provaDetAcademico.getAssinalado());
        st.setBoolean(4, provaDetAcademico.isCorreta());
        if (isUpdate){
            st.setInt(5, provaDetAcademico.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }
       
    private String getDefaultQuery() {
        return "SELECT id_provadetacademico, id_provadet, id_academico, assinalado, correto FROM sys.provadetacademico ";
    }
    
    public boolean delete(ProvaDetAcademico e) throws SQLException {
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        if (e.getId() != null){
            sql.append("DELETE FROM sys.provadetacademico");
            sql.append("      WHERE id_provadetacademico = ?");
        }else{
            sql.append("DELETE FROM sys.provadetacademico");
            sql.append("      WHERE id_academico = ?");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        if (e.getId() != null){
            st.setInt(1, e.getId());
        }else{
            st.setInt(1, e.getAcademico().getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }
    
    public boolean deleteByProva(Integer idProva) throws SQLException {
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.provadetacademico A ");
        sql.append(" USING (SELECT provadetacademico.id_provadetacademico ");
        sql.append("          FROM sys.provadetacademico");
        sql.append("          JOIN sys.provadet ON (provadetacademico.id_provadet = provadet.id_provadet)");
        sql.append("          JOIN sys.prova    ON (provadet.id_prova             = prova.id_prova)");
        sql.append("         WHERE prova.id_prova = ?) as foo");
        sql.append(" WHERE a.id_provadetacademico = foo.id_provadetacademico;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProva);
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }
    
    public List<ProvaDetAcademico> queryAll(String filtro, String order) throws SQLException{
        Connection                  con                        = null;
        PreparedStatement           st                         = null;
        ResultSet                   rs                         = null;
        StringBuilder               sql                        = null;
        List<ProvaDetAcademico>     listaProvaDetAcademico     = null;
        ProvaDetAcademico           provaDetAcademico          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_provadetacademico");
        }
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDetAcademico == null){
                listaProvaDetAcademico = new ArrayList<>();
            }
            provaDetAcademico = new ProvaDetAcademico();
            provaDetAcademico.setId(rs.getInt("id_provadetacademico"));
            provaDetAcademico.setAcademico(new AcademicoBD().find(rs.getInt("id_academico")));
            provaDetAcademico.setAssinalado(rs.getString("assinalado"));
            provaDetAcademico.setCorreta(rs.getBoolean("correto"));
            listaProvaDetAcademico.add(provaDetAcademico);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDetAcademico;
    }
    
    public List<ProvaDetAcademico> queryAllByProvaDet(Integer idProvaDet) throws SQLException{
        Connection                  con                        = null;
        PreparedStatement           st                         = null;
        ResultSet                   rs                         = null;
        StringBuilder               sql                        = null;
        List<ProvaDetAcademico>     listaProvaDetAcademico     = null;
        ProvaDetAcademico           provaDetAcademico          = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_provadet = ?");
        sql.append(" ORDER BY id_academico;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProvaDet);
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDetAcademico == null){
                listaProvaDetAcademico = new ArrayList<>();
            }
            provaDetAcademico = new ProvaDetAcademico();
            provaDetAcademico.setId(rs.getInt("id_provadetacademico"));
            provaDetAcademico.setAcademico(new AcademicoBD().find(rs.getInt("id_academico")));
            provaDetAcademico.setAssinalado(rs.getString("assinalado"));
            provaDetAcademico.setCorreta(rs.getBoolean("correto"));
            listaProvaDetAcademico.add(provaDetAcademico);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDetAcademico;
    }
    
}
