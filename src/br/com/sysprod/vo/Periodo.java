package br.com.sysprod.vo;

import java.util.Date;
import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Periodo {
    
    private Integer id;
    private Date    dtInicio;
    private Date    dtFim;
    private boolean encerrado;
    private String  descricao;

    public Periodo() {
    }

    public Periodo(Integer id, Date dtInicio, Date dtFim, boolean encerrado, String descricao) {
        this.id = id;
        this.dtInicio = dtInicio;
        this.dtFim = dtFim;
        this.encerrado = encerrado;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public boolean isEncerrado() {
        return encerrado;
    }

    public void setEncerrado(boolean encerrado) {
        this.encerrado = encerrado;
    }
    
       public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Periodo other = (Periodo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}
