package br.com.sysprod.dao;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.vo.ProvaDet;
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
public class ProvaDetBD {

    Connection con = null;
    
    public ProvaDetBD() {
    }
    
    public ProvaDetBD(Connection con){
        this.con = con;
    }
    
    public boolean saveOrUpdate(ProvaDet p, Integer idProva) throws SQLException{
        PreparedStatement st             = null;
        ResultSet         rs             = null;
        StringBuilder     sql            = null;
        boolean           isUpdate       = false;
        int               idProvaDet     = 0;
        int               result         = 0;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_provadet = ?");
        st = con.prepareStatement(sql.toString());
        st.setInt(1, p.getId());
        rs = st.executeQuery();
        if (rs.next()){
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.provadet    ");
            sql.append("   SET id_prova    = ?,");
            sql.append("       id_pergunta = ? ");
            sql.append(" WHERE id_provadet = ? ");
        }else{
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.provadet(id_prova, id_pergunta)");
            sql.append("     VALUES (?, ?)");
            sql.append("  RETURNING id_provadet");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProva);
        st.setInt(2, p.getPergunta().getId());
        if (isUpdate){
            st.setInt(3, p.getId());
            result = st.executeUpdate();
            idProvaDet = p.getId();
        }else{
            rs = st.executeQuery();
            if (rs.next()){
                idProvaDet = rs.getInt("id_provadet");
                result = ConstanteBD.DONE;
            }
        }
        ProvaDetItemBD provaDetItemBD = new ProvaDetItemBD(con);
        ProvaDetAcademicoBD provaDetAcademicoBD = new ProvaDetAcademicoBD(con);
        if (result == ConstanteBD.DONE){
            try {
                if (p.getListaProvaDetItemExcluir() != null && !p.getListaProvaDetItemExcluir().isEmpty()){
                    for (ProvaDetItem detItem : p.getListaProvaDetItemExcluir()) {
                        provaDetItemBD.delete(detItem);
                    }
                }
                if (p.getListaProvaDetItem() != null && !p.getListaProvaDetItem().isEmpty()){
                    for (ProvaDetItem detItem : p.getListaProvaDetItem()) {
                        provaDetItemBD.saveOrUpdate(detItem, idProvaDet);
                    }
                } 
                if (p.getListaProvaDetAcademicoExcluir() != null && !p.getListaProvaDetAcademicoExcluir().isEmpty()){
                    for (ProvaDetAcademico acadItem : p.getListaProvaDetAcademicoExcluir()) {
                        provaDetAcademicoBD.delete(acadItem);
                    }
                }
                if (p.getListaProvaDetAcademico() != null && !p.getListaProvaDetAcademico().isEmpty()){
                     for (ProvaDetAcademico acadItem : p.getListaProvaDetAcademico()) {
                        provaDetAcademicoBD.saveOrUpdate(acadItem, idProvaDet);
                    }
                }
                result = ConstanteBD.DONE;
            } catch (SQLException e) {
                result = ConstanteBD.ERR;
                throw e;
            }
        }
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }
    
    private String getDefaultQuery(){
        return "SELECT id_provadet, id_prova, id_pergunta FROM sys.provadet ";
    }
    
    public boolean delete(ProvaDet p) throws SQLException{
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.provadet");
        sql.append("      WHERE id_provadet = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, p.getId());
        
        if (p.getListaProvaDetItem() != null && !p.getListaProvaDetItem().isEmpty()){
            ProvaDetItemBD itemBD = new ProvaDetItemBD(con);
            for (ProvaDetItem item : p.getListaProvaDetItem()) {
                itemBD.delete(item);
            }
        }
        if (p.getListaProvaDetAcademico() != null && !p.getListaProvaDetAcademico().isEmpty()){
            ProvaDetAcademicoBD detAcademicoBD = new ProvaDetAcademicoBD(con);
            for(ProvaDetAcademico detAcad : p.getListaProvaDetAcademico()){
                detAcademicoBD.delete(detAcad);
            }
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }
    
    public List<ProvaDet> queryAll(String filtro, String order) throws SQLException{
        Connection        con           = null;
        PreparedStatement st            = null;
        ResultSet         rs            = null;
        StringBuilder     sql           = null;
        List<ProvaDet>    listaProvaDet = null;
        ProvaDet          provaDet      = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY id_provadet");
        }
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDet == null){
                listaProvaDet = new ArrayList<>();
            }
            provaDet = new ProvaDet();
            provaDet.setId(rs.getInt("id_provadet"));
            provaDet.setPergunta(new PerguntaBD().find(rs.getInt("id_pergunta")));
            listaProvaDet.add(provaDet);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDet;
    }
    
    public List<ProvaDet> queryAllByIdProva(Integer idProva) throws SQLException{
        Connection        con           = null;
        PreparedStatement st            = null;
        ResultSet         rs            = null;
        StringBuilder     sql           = null;
        List<ProvaDet>    listaProvaDet = null;
        ProvaDet          provaDet      = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_prova = ?");
        sql.append(" ORDER BY id_provadet;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProva);
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDet == null){
                listaProvaDet = new ArrayList<>();
            }
            provaDet = new ProvaDet();
            provaDet.setId(rs.getInt("id_provadet"));
            provaDet.setPergunta(new PerguntaBD().find(rs.getInt("id_pergunta")));
            listaProvaDet.add(provaDet);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDet;
    }
    
    public List<ProvaDet> queryAllByIdProva(Integer idProva, boolean auxiliares) throws SQLException{
        Connection        con           = null;
        PreparedStatement st            = null;
        ResultSet         rs            = null;
        StringBuilder     sql           = null;
        List<ProvaDet>    listaProvaDet = null;
        ProvaDet          provaDet      = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_prova = ?");
        sql.append(" ORDER BY id_provadet;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idProva);
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProvaDet == null){
                listaProvaDet = new ArrayList<>();
            }
            provaDet = new ProvaDet();
            provaDet.setId(rs.getInt("id_provadet"));
            provaDet.setPergunta(new PerguntaBD().find(rs.getInt("id_pergunta")));
            if (auxiliares){
                provaDet.setListaProvaDetItem(new ProvaDetItemBD().queryAllByProvaDet(provaDet.getId()));
                provaDet.setListaProvaDetAcademico(new ProvaDetAcademicoBD().queryAllByProvaDet(provaDet.getId()));
            }
            listaProvaDet.add(provaDet);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProvaDet;
    }
}
