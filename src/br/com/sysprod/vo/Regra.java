package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Regra {
    
    public static final String ROUND_DOWN = "-";
    public static final String ROUND_UP   = "+";
    
    private Integer id;
    private Integer fase;
    private Integer acertos;
    private String  arredondar;
    private Boolean ativo;

    public Regra() {
    }

    public Regra(Integer id, Integer fase, Integer acertos, String arredondar, Boolean ativo) {
        this.id         = id;
        this.fase       = fase;
        this.acertos    = acertos;
        this.arredondar = arredondar;
        this.ativo      = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public Integer getAcertos() {
        return acertos;
    }

    public void setAcertos(Integer acertos) {
        this.acertos = acertos;
    }

    public String getArredondar() {
        return arredondar;
    }

    public void setArredondar(String arredondar) {
        this.arredondar = arredondar;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.fase);
        hash = 11 * hash + Objects.hashCode(this.acertos);
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
        final Regra other = (Regra) obj;
        if (!Objects.equals(this.fase, other.fase)) {
            return false;
        }
        if (!Objects.equals(this.acertos, other.acertos)) {
            return false;
        }
        return true;
    }
    
}
