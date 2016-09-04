package br.com.sysprod.vo;

import java.util.Objects;
/**
 * 
 * @author Cristiano Bombazar
 */
public class Usuario {

    private Integer id;
    private String  nome;
    private String  login;
    private String  senha;
    private boolean ativo;

    public Usuario() {
    }

    public Usuario(Integer id, String nome, String login, String senha, boolean ativo) {
        this.id    = id;
        this.nome  = nome;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
   
    
    @Override
    public String toString() {
        return "ID: "+getId() + " Nome: "+getNome();
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
