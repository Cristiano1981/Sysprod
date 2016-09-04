package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class ProvaDetItem {
    
    private Integer  id;
    private Pergunta pergunta;
    private String   letra;
    private boolean  correto;

    public ProvaDetItem() {
    }

    public ProvaDetItem(Integer id, Pergunta pergunta, String letra, boolean correto) {
        this.id                                = id;
        this.pergunta                          = pergunta;
        this.letra                             = letra;
        this.correto                           = correto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public boolean isCorreto() {
        return correto;
    }

    public void setCorreto(boolean correto) {
        this.correto = correto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.pergunta);
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
        final ProvaDetItem other = (ProvaDetItem) obj;
        if (!Objects.equals(this.pergunta, other.pergunta)) {
            return false;
        }
        return true;
    }
    
    
    
}
