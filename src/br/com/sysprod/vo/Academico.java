package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Academico {
    
    private int     id;
    private int     codigo;
    private String  nome;
    private Curso   curso;
    private Unidade unidade;
    private boolean ativo;

    public Academico() {
    }

    public Academico(int id, String nome,Unidade unidade, Curso curso, boolean ativo) {
        this.id      = id;
        this.nome    = nome;
        this.unidade = unidade;
        this.curso   = curso;
        this.ativo   = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Curso getCurso() {
        return curso;
    }
    
    
    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    
    @Override
    public String toString() {
        return id+ " - "+nome ;
    }
    
     public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.codigo;
        hash = 41 * hash + Objects.hashCode(this.unidade);
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
        final Academico other = (Academico) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        return true;
    }
}
