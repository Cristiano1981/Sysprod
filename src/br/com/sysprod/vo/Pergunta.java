package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano
 */
public class Pergunta {
    
    
    private Integer    id;
    private Disciplina disciplina;
    private String     descricao;
    private String     tipo;
    private boolean    ativo;

    public Pergunta() {
    }

    public Pergunta(Integer id,Disciplina disciplina, String descricao, String tipo, boolean ativo) {
        this.id         = id;
        this.disciplina = disciplina;
        this.descricao  = descricao;
        this.tipo       = tipo;
        this.ativo      = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
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
        final Pergunta other = (Pergunta) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
