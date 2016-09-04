package br.com.sysprod.vo;

import java.util.List;
import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Turma {
    
    
    private Integer id;
    private Unidade unidade;
    private Curso   curso;
    private Periodo periodo;
    private Integer fase;
    private String  obs;
    private Integer qtdMaximaAcertos;
    private boolean turmaSelection;
    private List<TurmaDet> listaTurmaDet;
    private List<TurmaDet> listaTurmaDetExcluir;

    public Turma() {
    }

    public Turma(Integer id, Unidade unidade, Curso curso, Periodo periodo, Integer fase, String obs, Integer qtdMaximaAcertos, List<TurmaDet> listaTurmaDet, List<TurmaDet> listaTurmaDetExcluir) {
        this.id                   = id;
        this.unidade              = unidade;
        this.curso                = curso;
        this.periodo              = periodo;
        this.fase                 = fase;
        this.obs                  = obs;
        this.qtdMaximaAcertos     = qtdMaximaAcertos;
        this.listaTurmaDet        = listaTurmaDet;
        this.listaTurmaDetExcluir = listaTurmaDetExcluir;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public void setQtdMaximaAcertos(Integer qtdMaximaAcertos) {
        this.qtdMaximaAcertos = qtdMaximaAcertos;
    }

    public Integer getQtdMaximaAcertos() {
        return qtdMaximaAcertos;
    }

    public boolean isTurmaSelection() {
        return turmaSelection;
    }

    public void setTurmaSelection(boolean turmaSelection) {
        this.turmaSelection = turmaSelection;
    }
    
    public List<TurmaDet> getListaTurmaDet() {
        return listaTurmaDet;
    }

    public void setListaTurmaDet(List<TurmaDet> listaTurmaDet) {
        this.listaTurmaDet = listaTurmaDet;
    }

    public List<TurmaDet> getListaTurmaDetExcluir() {
        return listaTurmaDetExcluir;
    }

    public void setListaTurmaDetExcluir(List<TurmaDet> listaTurmaDetExcluir) {
        this.listaTurmaDetExcluir = listaTurmaDetExcluir;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.unidade);
        hash = 43 * hash + Objects.hashCode(this.curso);
        hash = 43 * hash + Objects.hashCode(this.periodo);
        hash = 43 * hash + Objects.hashCode(this.fase);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Turma other = (Turma) obj;
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        if (!Objects.equals(this.curso, other.curso)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!Objects.equals(this.fase, other.fase)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ID :"+id + " Curso: "+curso.getDescricao()+ " Período: "+periodo.getDescricao();
    }

}
