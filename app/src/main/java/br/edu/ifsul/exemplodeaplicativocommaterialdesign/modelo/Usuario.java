package br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nome;
    private String sobre_nome;
    private String email;
    private String senha;

    public Usuario(String nome, String sobre_nome, String email, String senha) {
        this.nome = nome;
        this.sobre_nome = sobre_nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobre_nome() {
        return sobre_nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public String toString() {
        return getNome();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobre_nome(String sobre_nome) {
        this.sobre_nome = sobre_nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
