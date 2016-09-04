
package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class TurmaDet {

    public TurmaDet() {
    }
    
    public TurmaDet(Integer id, Academico academico) {
        this.id        = id;
        this.academico = academico;
    }
    
    private Integer id;
    private Academico academico;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.academico);
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
        final TurmaDet other = (TurmaDet) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.academico, other.academico)) {
            return false;
        }
        return true;
    }

    
}
