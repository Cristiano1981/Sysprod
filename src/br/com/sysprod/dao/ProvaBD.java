package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.ProvaDetItem;
import br.com.sysprod.vo.RelatorioNota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class ProvaBD implements Crud<Prova>{

    @Override
    public boolean saveOrUpdate(Prova e) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        int               result  = ConstanteBD.ERR;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_prova = ?");
        con = ConnectionManager.getConnection();
        con.setAutoCommit(false);
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()) {
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.prova ");
            sql.append("   SET id_prova     = ?,");
            sql.append("       id_turma     = ?,");
            sql.append("       dt_cadastro  = ?,");
            sql.append("       dt_aplicacao = ?,");
            sql.append("       obs          = ?");
            sql.append("  WHERE id_prova    = ?;");
        } else {
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.prova (id_prova, id_turma, dt_cadastro, dt_aplicacao, obs)");
            sql.append("     VALUES (?, ?, ?, ?, ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setInt(2, e.getTurma().getId());
        st.setDate(3, Utils.converteDateBD(e.getDtCadastro()));
        st.setDate(4, Utils.converteDateBD(e.getDtAplicacao()));
        st.setString(5, e.getObs());
        if (isUpdate) {
            st.setInt(6, e.getId());
        }
        try {
            result = st.executeUpdate();
        } catch (SQLException ex) {
            con.rollback();
            ConnectionManager.closeAll (st, rs);
            throw ex;
        }
        ProvaDetBD provaDetBD = new ProvaDetBD(con);
        try {
            if (result == ConstanteBD.DONE) {
                if (e.getListaProvaDetExcluir()!= null && !e.getListaProvaDetExcluir().isEmpty()) {
                    for (ProvaDet det : e.getListaProvaDetExcluir()) {
                        provaDetBD.delete(det);
                    }
                }
                if (e.getListaProvaDet()!= null && !e.getListaProvaDet().isEmpty()) {
                    for (ProvaDet det : e.getListaProvaDet()) {
                        provaDetBD.saveOrUpdate(det, e.getId());
                    }
                }
                con.commit();
                result = ConstanteBD.DONE;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ErrorVerification.ErrDetalhe(ex, "Ocorreu um erro ao gravar prova");
            con.rollback();
            result = ConstanteBD.ERR;
        }
        ConnectionManager.closeAll (st, rs);
        return result == ConstanteBD.DONE ;
    }

    public boolean delete(Prova e) throws SQLException {
        Connection        con    = null;
        PreparedStatement st     = null;
        StringBuilder     sql    = null;
        Integer           result = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.prova");
        sql.append(" WHERE id_prova = ?");
        con = ConnectionManager.getConnection();
        con.setAutoCommit(false);
        ProvaDetBD provaDetBD = new ProvaDetBD(con);
            try {
                if (e.getListaProvaDet()!= null && !e.getListaProvaDet().isEmpty()){
                    for (ProvaDet turmaDet : e.getListaProvaDet()) {
                        provaDetBD.delete(turmaDet);
                    }
                }
                st = con.prepareStatement(sql.toString());
                st.setInt(1, e.getId());
                result = st.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                ErrorVerification.ErrDetalhe(ex, "Erro ao excluir prova");
                result = ConstanteBD.ERR;
                con.rollback();
            }
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }


    @Override
    @Deprecated
    public Prova find(Integer codigo) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Prova find(Integer codigo, boolean listaProvaDet) throws SQLException{
        Connection        con   = null;
        PreparedStatement st    = null;
        ResultSet         rs    = null;
        StringBuilder     sql   = null;
        Prova             prova = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_prova = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs = st.executeQuery();
        if (rs.next()){
            prova = new Prova();
            prova.setId(rs.getInt("id_prova"));
            prova.setTurma(new TurmaBD().find(rs.getInt("id_turma"), false));
            prova.setDtCadastro(rs.getDate("dt_cadastro"));
            prova.setDtAplicacao(rs.getDate("dt_aplicacao"));
            prova.setObs(rs.getString("obs"));
            if (listaProvaDet){
                prova.setListaProvaDet(new ProvaDetBD().queryAllByIdProva(prova.getId(), true));
            }
        }
        ConnectionManager.closeAll(st, rs);
        return prova;
    }
    
    @Override
    public String getDefaultQuery() {
        return "SELECT id_prova, id_turma, dt_cadastro, dt_aplicacao, obs FROM sys.prova ";
    }

    @Override
    public Integer nextCode() throws SQLException {
        Connection        con  = null;
        PreparedStatement st   = null;
        ResultSet         rs   = null;
        Integer           next = null;
        StringBuilder     sql  = new StringBuilder();
        sql.append("SELECT coalesce(max(id_prova),0)+1 as codigo");
        sql.append("  FROM sys.prova;");
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
    public List<Prova> queryAll(String filtro, String order) throws SQLException {
        Connection        con        = null;
        PreparedStatement st         = null;
        ResultSet         rs         = null;
        List<Prova>       listaProva = null;
        Prova             prova      = null;
        StringBuilder    sql         = null;
        sql = new StringBuilder();
        sql.append("SELECT prova.id_prova, prova.id_turma, prova.dt_cadastro, prova.dt_aplicacao, prova.obs");
        sql.append("  FROM sys.prova");
        sql.append("  JOIN sys.turma ON (prova.id_turma = turma.id_turma)");
        sql.append(" WHERE turma.id_unidade = ?");
        if (filtro != null){
            sql.append(filtro);
        }
        if (order != null){
            sql.append(order);
        }else{
            sql.append(" ORDER BY prova.id_prova;");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()){
            if (listaProva == null){
                listaProva = new ArrayList<Prova>();
            }
            prova = new Prova();
            prova.setId(rs.getInt("id_prova"));
            prova.setTurma(new TurmaBD().find(rs.getInt("id_turma"), false));
            prova.setDtCadastro(rs.getDate("dt_cadastro"));
            prova.setDtAplicacao(rs.getDate("dt_aplicacao"));
            prova.setObs(rs.getString("obs"));
            listaProva.add(prova);
        }
        ConnectionManager.closeAll(st, rs);
        return listaProva;
    }
    
    public Prova selectByIDTurma(Integer idTurma) throws SQLException{
       Connection          con   = null;
       PreparedStatement   st    = null;
       ResultSet           rs    = null;
       Prova               prova = null;
       StringBuilder       sql   = null;
       sql = new StringBuilder();
       sql.append("SELECT prova.id_prova ");
       sql.append("  FROM sys.prova");
       sql.append("  JOIN sys.turma   ON (turma.id_turma     = prova.id_turma)");
       sql.append("  JOIN sys.periodo ON (periodo.id_periodo = turma.id_periodo)");
       sql.append(" WHERE turma.id_turma = ?");
       sql.append("   AND periodo.encerrado is false");
       sql.append(" ORDER BY prova.id_prova LIMIT 1;");
       con = ConnectionManager.getConnection();
       st = con.prepareStatement(sql.toString());
       st.setInt(1, idTurma);
       rs = st.executeQuery();
       if (rs.next()){
           prova = find(rs.getInt("id_prova"), true);
       }
       ConnectionManager.closeAll(st, rs);
       return prova;
    }
    
    public List<RelatorioNota> selectRelByIDProva(Integer codigo) throws SQLException{
        Connection          con          = null;
        PreparedStatement   st           = null;
        ResultSet           rs           = null;
        List<RelatorioNota> listaRelNota = null;
        RelatorioNota       rel          = null;
        StringBuilder       sql          = null;
        sql = new StringBuilder();
        sql.append("SELECT d.codigo,d.nome,e.fase, min(e.qtd_maxima) as qtd_acertos_max,count(*) as qtd_Acertos");
        sql.append("  FROM sys.prova a");
        sql.append("  JOIN sys.turma e             on (a.id_turma     = e.id_turma)");
        sql.append("  JOIN sys.provadet b          on (a.id_prova     = b.id_prova)");
        sql.append("  JOIN sys.provadetacademico c on (b.id_provadet  = c.id_provadet)");
        sql.append("  JOIN sys.academico d         on (c.id_academico = d.id_academico)");
        sql.append(" WHERE a.id_prova = ?");
        sql.append("   AND c.correto is true");
        sql.append(" GROUP BY d.codigo,d.nome,e.fase");
        sql.append(" ORDER BY e.fase, qtd_acertos;");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs  = st.executeQuery();
        while (rs.next()){
            if (listaRelNota == null){
                listaRelNota = new ArrayList<RelatorioNota>();
            }
            rel = new RelatorioNota();
            rel.setCodigoAcademico(rs.getInt("codigo"));
            rel.setNomeAcademico(rs.getString("nome"));
            rel.setQtdMaximaAcerto(rs.getInt("qtd_acertos_max"));
            rel.setFase(rs.getInt("fase"));
            rel.setQtdAcertos(rs.getInt("qtd_acertos"));
            listaRelNota.add(rel);
        }
        ConnectionManager.closeAll(st, rs);
        return listaRelNota;
    }
    
    public List<RelatorioNota> selectRelByTurma(String turmas) throws SQLException{
        Connection          con          = null;
        PreparedStatement   st           = null;
        ResultSet           rs           = null;
        List<RelatorioNota> listaRelNota = null;
        RelatorioNota       rel          = null;
        StringBuilder       sql          = null;
        sql = new StringBuilder();
        sql.append("SELECT d.codigo,d.nome,e.fase, min(e.qtd_maxima) as qtd_acertos_max,count(*) as qtd_Acertos");
        sql.append("  FROM sys.prova a");
        sql.append("  JOIN sys.turma e             on (a.id_turma     = e.id_turma)");
        sql.append("  JOIN sys.provadet b          on (a.id_prova     = b.id_prova)");
        sql.append("  JOIN sys.provadetacademico c on (b.id_provadet  = c.id_provadet)");
        sql.append("  JOIN sys.academico d         on (c.id_academico = d.id_academico)");
        sql.append(" WHERE e.id_turma in ("+turmas+")");
        sql.append("   AND c.correto is true");
        sql.append(" GROUP BY d.codigo,d.nome,e.fase");
        sql.append(" ORDER BY e.fase, qtd_acertos");
        con = ConnectionManager.getConnection();
        st  = con.prepareStatement(sql.toString());
        rs  = st.executeQuery();
        while (rs.next()){
            if (listaRelNota == null){
                listaRelNota = new ArrayList<RelatorioNota>();
            }
            rel = new RelatorioNota();
            rel.setCodigoAcademico(rs.getInt("codigo"));
            rel.setNomeAcademico(rs.getString("nome"));
            rel.setQtdMaximaAcerto(rs.getInt("qtd_acertos_max"));
            rel.setFase(rs.getInt("fase"));
            rel.setQtdAcertos(rs.getInt("qtd_acertos"));
            listaRelNota.add(rel);
        }
        ConnectionManager.closeAll(st, rs);
        return listaRelNota;
    }
}
