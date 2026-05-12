package br.edu.ifsul.exemplodeaplicativocommaterialdesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;import android.view.View;import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.LoginRequest;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.LoginResponse;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMainActivity extends AppCompatActivity {
    private EditText etEmail, etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("estaLogado", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        Button btnEntrar      = findViewById(R.id.btnEntrar);
        Button btnCriarConta  = findViewById(R.id.btnCriarConta);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String senha = etSenha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginMainActivity.this,
                            "Preencha e-mail e senha.", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(email, senha);
            }
        });

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInMainActivity.class));
            }
        });
    }

    private void login(String email, String senha) {
        LoginRequest login = new LoginRequest(email, senha);
        Call<LoginResponse> call = RetrofitClient.getInstance().getMyApi().login(login);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // Login OK — salva nas SharedPreferences e vai para a MainActivity
                        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("estaLogado", true);
                        editor.putString("emailLogado", email);
                        editor.apply();

                        Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginMainActivity.this,
                                "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginMainActivity.this,
                        "Sem conexão com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}