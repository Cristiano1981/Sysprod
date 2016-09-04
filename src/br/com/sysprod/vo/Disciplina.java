package br.com.sysprod.vo;

import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */
public class Disciplina {
    
    private Integer id;
    private Unidade unidade;
    private String  nome;
    private Integer fase;
    private boolean ativo;

    public Disciplina() {
    }

    public Disciplina(Integer id, Unidade unidade, String nome, Integer fase, boolean ativo) {
        this.id = id;
        this.unidade = unidade;
        this.nome = nome;
        this.fase = fase;
        this.ativo = ativo;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public Integer getFase() {
        return fase;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Disciplina other = (Disciplina) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
