package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class ProvaDetAcademico {
    
    private Integer   id;
    private Academico academico;
    private String    assinalado;
    private boolean   correta;

    public ProvaDetAcademico() {
    }

    public ProvaDetAcademico(Integer id, Academico academico, String assinalado, boolean correta) {
        this.id         = id;
        this.academico  = academico;
        this.assinalado = assinalado;
        this.correta    = correta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Academico getAcademico() {
        return academico;
    }

    public void setAcademico(Academico academico) {
        this.academico = academico;
    }

    public String getAssinalado() {
        return assinalado;
    }

    public void setAssinalado(String assinalado) {
        this.assinalado = assinalado;
    }

    public boolean isCorreta() {
        return correta;
    }

    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.academico);
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
        final ProvaDetAcademico other = (ProvaDetAcademico) obj;
        if (!Objects.equals(this.academico, other.academico)) {
            return false;
        }
        return true;
    }
    
    
    
}
