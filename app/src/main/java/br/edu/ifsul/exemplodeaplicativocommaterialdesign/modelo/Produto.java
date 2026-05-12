package br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo;

import java.io.Serializable;

public class Produto implements Serializable {
    // atributos da classe Produto
    private int id;
    private String nome;
    private Double valor;

    public Produto(int id, String nome, double valor)  {
        this.id=id;
        this.nome=nome;
        this.valor=valor;
    }

    public Produto(){

    }

    // métodos da classe Produto
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    @Override
    public String toString() { return getNome(); }
}
