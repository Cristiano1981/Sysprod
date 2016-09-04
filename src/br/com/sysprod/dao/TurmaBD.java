package br.com.sysprod.dao;

import br.com.sysprod.Global;
import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteBD;
import br.com.sysprod.interfaces.Crud;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Turma;
import br.com.sysprod.vo.TurmaDet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class TurmaBD implements Crud<Turma> {

    @Override
    public boolean saveOrUpdate(Turma e) throws SQLException {
        Connection        con     = null;
        PreparedStatement st      = null;
        ResultSet         rs      = null;
        StringBuilder     sql     = null;
        int               result  = 0;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_turma = ?");
        con = ConnectionManager.getConnection();
        con.setAutoCommit(false);
        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        rs = st.executeQuery();
        if (rs.next()) {
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.turma ");
            sql.append("   SET id_turma   = ?,");
            sql.append("       id_unidade = ?,");
            sql.append("       id_curso   = ?,");
            sql.append("       id_periodo = ?,");
            sql.append("       fase       = ?,");
            sql.append("       obs        = ?,");
            sql.append("       qtd_maxima = ? ");
            sql.append("  WHERE id_turma  = ?;");
        } else {
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.turma (id_turma, id_unidade, id_curso, id_periodo, fase, obs, qtd_maxima)");
            sql.append("     VALUES (?, ?, ?, ?, ?, ?, ?);");
        }

        st = con.prepareStatement(sql.toString());
        st.setInt(1, e.getId());
        st.setInt(2, e.getUnidade().getId());
        st.setInt(3, e.getCurso().getId());
        st.setInt(4, e.getPeriodo().getId());
        st.setInt(5, e.getFase());
        st.setString(6, e.getObs());
        st.setInt(7, e.getQtdMaximaAcertos());
        if (isUpdate) {
            st.setInt(8, e.getId());
        }
        TurmaDetBD detBD = new TurmaDetBD(con);
        try {
            result = st.executeUpdate();
            if (result == ConstanteBD.DONE) {
                if (e.getListaTurmaDetExcluir() != null && !e.getListaTurmaDetExcluir().isEmpty()) {
                    for (TurmaDet det : e.getListaTurmaDetExcluir()) {
                        detBD.delete(det);
                    }
                }
                if (e.getListaTurmaDet() != null && !e.getListaTurmaDet().isEmpty()) {
                    for (TurmaDet det : e.getListaTurmaDet()) {
                        detBD.saveOrUpdate(det, e.getId());
                    }
                }
                con.commit();
                result = ConstanteBD.DONE;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ErrorVerification.ErrDetalhe(ex, "Ocorreu um erro ao gravar acadêmicos");
            con.rollback();
            result = ConstanteBD.ERR;
        }
        ConnectionManager.closeAll (st, rs);
        return result == ConstanteBD.DONE ;
    }

    @Override
    public boolean delete(Turma e) throws SQLException {
        Connection        con    = null;
        PreparedStatement st     = null;
        StringBuilder     sql    = null;
        Integer           result = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.turma");
        sql.append(" WHERE id_turma = ?");
        con = ConnectionManager.getConnection();
        con.setAutoCommit(false);
        TurmaDetBD turmaDetBD = new TurmaDetBD(con);
            try {
                if (e.getListaTurmaDet() != null && !e.getListaTurmaDet().isEmpty()){
                    for (TurmaDet turmaDet : e.getListaTurmaDet()) {
                        turmaDetBD.delete(turmaDet);
                    }
                }
                st = con.prepareStatement(sql.toString());
                st.setInt(1, e.getId());
                result = st.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                ErrorVerification.ErrDetalhe(ex, "Erro ao excluir turma");
                result = ConstanteBD.ERR;
                con.rollback();
            }finally{
                con.setAutoCommit(true);
            }
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    @Override
    @Deprecated
    public Turma find(Integer codigo) throws SQLException {
//        Connection con = null;
//        PreparedStatement st = null;
//        ResultSet rs = null;
//        StringBuilder sql = null;
//        Turma turma = null;
//        sql = new StringBuilder();
//        sql.append(getDefaultQuery());
//        sql.append(" WHERE id_turma = ?;");
//        con = ConnectionManager.getConnection();
//        st = con.prepareStatement(sql.toString());
//        rs = st.executeQuery();
//        if (rs.next()) {
//            turma = new Turma();
//            turma.setId(rs.getInt("id_turma"));
//            turma.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
//            turma.setPeriodo(new PeriodoBD().find(rs.getInt("id_periodo")));
//            turma.setCurso(new CursoBD().find(rs.getInt("id_curso")));
//            turma.setFase(rs.getInt("fase"));
//            turma.setObs(rs.getString("obs"));
//            turma.setListaTurmaDet(new TurmaDetBD().queryAllByTurma(turma.getId()));
//        }
//        return turma;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Turma find(Integer codigo, boolean academicos) throws SQLException {
        Connection        con   = null;
        PreparedStatement st    = null;
        ResultSet         rs    = null;
        StringBuilder     sql   = null;
        Turma             turma = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_turma = ?;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, codigo);
        rs = st.executeQuery();
        if (rs.next()) {
            turma = new Turma();
            turma.setId(rs.getInt("id_turma"));
            turma.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
            turma.setPeriodo(new PeriodoBD().find(rs.getInt("id_periodo")));
            turma.setCurso(new CursoBD().find(rs.getInt("id_curso")));
            turma.setFase(rs.getInt("fase"));
            turma.setObs(rs.getString("obs"));
            turma.setQtdMaximaAcertos(rs.getInt("qtd_maxima"));
            if (academicos) {
                turma.setListaTurmaDet(new TurmaDetBD().queryAllByTurma(turma.getId()));
            }
        }
        ConnectionManager.closeAll(st, rs);
        return turma;
    }

    @Override
        public String getDefaultQuery() {
        return "SELECT id_turma, id_unidade, id_curso, id_periodo, fase, obs, qtd_maxima FROM sys.turma ";
    }

    @Override
        public Integer nextCode() throws SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Integer next = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce(max(id_turma),0)+1 as codigo");
        sql.append("  FROM sys.turma;");
        con = ConnectionManager.getConnection();
        st = con.prepareCall(sql.toString());
        rs = st.executeQuery();
        if (rs.next()) {
            next = rs.getInt("codigo");
        }
        ConnectionManager.closeAll(st, rs);
        return next;
    }

    @Override
        public List<Turma> queryAll(String filtro, String order) throws SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Turma> listaAcademicoFase = null;
        Turma academicoFase = null;
        StringBuilder sql = null;
        sql = new StringBuilder();
        sql.append("SELECT a.id_turma,");
        sql.append("       b.id_unidade,");
        sql.append("       c.id_curso,");
        sql.append("       d.id_periodo,");
        sql.append("       a.fase,");
        sql.append("       a.obs,");
        sql.append("       a.qtd_maxima");
        sql.append("  FROM sys.turma   AS A");
        sql.append("  JOIN sys.unidade AS B on (a.id_unidade = b.id_unidade)");
        sql.append("  JOIN sys.curso   AS C on (a.id_curso   = c.id_curso)");
        sql.append("  JOIN sys.periodo AS D on (a.id_periodo = d.id_periodo)");
        sql.append(" WHERE b.id_unidade = ?");
        if (filtro != null) {
            sql.append(filtro);
        }
        if (order != null) {
            sql.append(order);
        } else {
            sql.append("ORDER BY a.id_turma");
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, Global.getUnidade().getId());
        rs = st.executeQuery();
        while (rs.next()) {
            if (listaAcademicoFase == null) {
                listaAcademicoFase = new ArrayList<Turma>();
            }
            academicoFase = new Turma();
            academicoFase.setId(rs.getInt("id_turma"));
            academicoFase.setUnidade(new UnidadeBD().find(rs.getInt("id_unidade")));
            academicoFase.setCurso(new CursoBD().find(rs.getInt("id_curso")));
            academicoFase.setPeriodo(new PeriodoBD().find(rs.getInt("id_periodo")));
            academicoFase.setFase(rs.getInt("fase"));
            academicoFase.setObs(rs.getString("obs"));
            academicoFase.setQtdMaximaAcertos(rs.getInt("qtd_maxima"));
            listaAcademicoFase.add(academicoFase);
        }
        ConnectionManager.closeAll(st, rs);
        return listaAcademicoFase;

}

    public static class TurmaDetBD {

    Connection con;

    public TurmaDetBD(Connection con) {
        this.con = con;
    }

    public TurmaDetBD() {
    }

    public boolean saveOrUpdate(TurmaDet t, Integer idTurma) throws SQLException {
        PreparedStatement st       = null;
        ResultSet         rs       = null;
        StringBuilder     sql      = null;
        boolean           isUpdate = false;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append(" WHERE id_turmadet = ?;");
        st = con.prepareStatement(sql.toString());
        st.setInt(1, t.getId() == null ? 0 : t.getId());
        rs = st.executeQuery();
        if (rs.next()) {
            isUpdate = true;
            sql = new StringBuilder();
            sql.append("UPDATE sys.turmadet");
            sql.append("   SET id_turma     = ?,");
            sql.append("       id_academico = ?");
            sql.append(" WHERE id_turmadet = ?;");
        } else {
            sql = new StringBuilder();
            sql.append("INSERT INTO sys.turmadet (id_turma, id_academico)");
            sql.append("     VALUES (?, ?);");
        }
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idTurma);
        st.setInt(2, t.getAcademico().getId());
        if (isUpdate) {
            st.setInt(3, t.getId());
        }
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, rs);
        return result == ConstanteBD.DONE;
    }

    public boolean delete(TurmaDet t) throws SQLException {
        PreparedStatement st  = null;
        StringBuilder     sql = null;
        sql = new StringBuilder();
        sql.append("DELETE FROM sys.turmadet");
        sql.append("      WHERE id_turmadet = ?");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, t.getId());
        int result = st.executeUpdate();
        ConnectionManager.closeAll(st, null);
        return result == ConstanteBD.DONE;
    }

    public String getDefaultQuery() {
        return "SELECT id_turmadet, id_turma, id_academico FROM sys.turmadet ";
    }

    public List<TurmaDet> queryAll(String filtro, String order) throws SQLException {
        Connection        con           = null;
        PreparedStatement st            = null;
        ResultSet         rs            = null;
        StringBuilder     sql           = null;
        List<TurmaDet>    listaTurmaDet = null;
        TurmaDet          turmaDet      = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        if (filtro != null) {
            sql.append(filtro);
        }
        if (order != null) {
            sql.append(order);
        }
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        rs = st.executeQuery();
        while (rs.next()) {
            if (listaTurmaDet == null) {
                listaTurmaDet = new ArrayList<TurmaDet>();
            }
            turmaDet = new TurmaDet();
            turmaDet.setId(rs.getInt("id_turma"));
            turmaDet.setAcademico(new AcademicoBD().find(rs.getInt("id_academico")));
            listaTurmaDet.add(turmaDet);
        }
        ConnectionManager.closeAll(st, rs);
        return listaTurmaDet;
    }

    public List<TurmaDet> queryAllByTurma(Integer idTurma) throws SQLException {
        Connection        con           = null;
        PreparedStatement st            = null;
        ResultSet         rs            = null;
        StringBuilder     sql           = null;
        List<TurmaDet>    listaTurmaDet = null;
        TurmaDet          turmaDet      = null;
        sql = new StringBuilder();
        sql.append(getDefaultQuery());
        sql.append("WHERE id_turma = ?;");
        con = ConnectionManager.getConnection();
        st = con.prepareStatement(sql.toString());
        st.setInt(1, idTurma);
        rs = st.executeQuery();
        while (rs.next()) {
            if (listaTurmaDet == null) {
                listaTurmaDet = new ArrayList<TurmaDet>();
            }
            turmaDet = new TurmaDet();
            turmaDet.setId(rs.getInt("id_turmadet"));
            turmaDet.setAcademico(new AcademicoBD().find(rs.getInt("id_academico")));
            listaTurmaDet.add(turmaDet);
        }
        ConnectionManager.closeAll(st, rs);
        return listaTurmaDet;
    }
}
}
