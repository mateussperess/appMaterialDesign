package br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo;

public class EventoAgenda {
    // atributos da classe Produto
    private int id;
    private String descricao;
    private String data;

    public EventoAgenda(int id, String descricao, String data)  {
        this.id=id;
        this.descricao=descricao;
        this.data=data;
    }
    public EventoAgenda(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
