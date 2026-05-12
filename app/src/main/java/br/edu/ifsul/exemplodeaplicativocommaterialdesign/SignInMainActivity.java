package br.edu.ifsul.exemplodeaplicativocommaterialdesign;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Produto;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Usuario;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.Api;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInMainActivity extends AppCompatActivity {
    private EditText etNome, etSobrenome, etEmail, etSenha;
    private Button btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNome        = findViewById(R.id.etCriarNome);
        etSobrenome   = findViewById(R.id.etCriarSobreNome);
        etEmail       = findViewById(R.id.etCriarEmail);
        etSenha       = findViewById(R.id.etCriarSenha);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome      = etNome.getText().toString().trim();
                String sobrenome = etSobrenome.getText().toString().trim();
                String email     = etEmail.getText().toString().trim();
                String senha     = etSenha.getText().toString().trim();

                if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(SignInMainActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                criarConta(nome, sobrenome, email, senha);
            }
        });
    }

    private void criarConta(String primeiroNome, String sobreNome, String email, String senha) {
        Usuario usuario = new Usuario(primeiroNome, sobreNome, email, senha);
        Call<Usuario> call = RetrofitClient.getInstance().getMyApi().createUser(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignInMainActivity.this,
                            "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignInMainActivity.this,
                            "Erro: e-mail já cadastrado ou falha no servidor.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(SignInMainActivity.this,
                        "Sem conexão com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}