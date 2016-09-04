package br.com.sysprod.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public class Prova {
    
    
    private Integer id;
    private Turma   turma;
    private Date    dtCadastro;
    private Date    dtAplicacao;
    private String  obs;
    private List<ProvaDet> listaProvaDet;
    private List<ProvaDet> listaProvaDetExcluir;

    public Prova() {
    }

    public Prova(Integer id, Turma turma, Date dtCadastro, Date dtAplicacao, String obs, List<ProvaDet> listaProvaDet, List<ProvaDet> listaProvaDetExcluir) {
        this.id                   = id;
        this.turma                = turma;
        this.dtCadastro           = dtCadastro;
        this.dtAplicacao          = dtAplicacao;
        this.obs                  = obs;
        this.listaProvaDet        = listaProvaDet;
        this.listaProvaDetExcluir = listaProvaDetExcluir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Date getDtAplicacao() {
        return dtAplicacao;
    }

    public void setDtAplicacao(Date dtAplicacao) {
        this.dtAplicacao = dtAplicacao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<ProvaDet> getListaProvaDet() {
        return listaProvaDet;
    }

    public void setListaProvaDet(List<ProvaDet> listaProvaDet) {
        this.listaProvaDet = listaProvaDet;
    }

    public List<ProvaDet> getListaProvaDetExcluir() {
        return listaProvaDetExcluir;
    }

    public void setListaProvaDetExcluir(List<ProvaDet> listaProvaDetExcluir) {
        this.listaProvaDetExcluir = listaProvaDetExcluir;
    }

}
