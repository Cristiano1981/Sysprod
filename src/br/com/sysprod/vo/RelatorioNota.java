package br.com.sysprod.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Cristiano Bombazar
 */
public class RelatorioNota implements Serializable{
    
    private Integer codigoAcademico;
    private String  nomeAcademico;
    private Integer fase;
    private Integer qtdAcertos;
    private Double  nota;
    private Integer qtdMaximaAcerto;

    public RelatorioNota() {
    }

    public RelatorioNota(Integer codigoAcademico, String nomeAcademico, Integer fase, Integer qtdAcertos, Double nota) {
        this.codigoAcademico = codigoAcademico;
        this.nomeAcademico   = nomeAcademico;
        this.fase            = fase;
        this.qtdAcertos      = qtdAcertos;
        this.nota            = nota;
    }

    public Integer getCodigoAcademico() {
        return codigoAcademico;
    }

    public void setCodigoAcademico(Integer codigoAcademico) {
        this.codigoAcademico = codigoAcademico;
    }

    public String getNomeAcademico() {
        return nomeAcademico;
    }

    public void setNomeAcademico(String nomeAcademico) {
        this.nomeAcademico = nomeAcademico;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public Integer getQtdAcertos() {
        return qtdAcertos;
    }

    public void setQtdAcertos(Integer qtdAcertos) {
        this.qtdAcertos = qtdAcertos;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.codigoAcademico);
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
        final RelatorioNota other = (RelatorioNota) obj;
        if (!Objects.equals(this.codigoAcademico, other.codigoAcademico)) {
            return false;
        }
        return true;
    }

    public Integer getQtdMaximaAcerto() {
        return qtdMaximaAcerto;
    }

    public void setQtdMaximaAcerto(Integer qtdMaximaAcerto) {
        this.qtdMaximaAcerto = qtdMaximaAcerto;
    }
    
}
