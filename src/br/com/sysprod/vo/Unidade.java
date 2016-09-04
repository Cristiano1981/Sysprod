package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Unidade {
    
    private Integer id;
    private String  fantasia;
    private boolean ativo;

    public Unidade() {
    }

    public Unidade(Integer id, String fantasia, boolean ativo) {
        this.id       = id;
        this.fantasia = fantasia;
        this.ativo    = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return id + " - "+fantasia;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Unidade other = (Unidade) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
