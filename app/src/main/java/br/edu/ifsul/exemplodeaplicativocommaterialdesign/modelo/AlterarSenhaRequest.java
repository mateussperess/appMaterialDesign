package br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo;

public class AlterarSenhaRequest {
    public String email;
    public String antigaSenha;
    public String novaSenha;

    public AlterarSenhaRequest(String email, String antigaSenha, String novaSenha) {
        this.email = email;
        this.antigaSenha = antigaSenha;
        this.novaSenha = novaSenha;
    }
}
