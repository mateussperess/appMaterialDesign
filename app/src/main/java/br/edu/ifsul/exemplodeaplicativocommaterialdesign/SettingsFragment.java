package br.edu.ifsul.exemplodeaplicativocommaterialdesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.AlterarSenhaRequest;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Usuario;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private EditText etSenhaAtual, etNovaSenha, etConfirmarSenha;
    private Button btnSalvarSenha;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
        );

        etSenhaAtual = view.findViewById(R.id.etSenhaAtual);
        etNovaSenha = view.findViewById(R.id.etNovaSenha);
        etConfirmarSenha = view.findViewById(R.id.etConfirmarSenha);

        btnSalvarSenha = view.findViewById(R.id.btnSalvarSenha);

        btnSalvarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senhaAtual = etSenhaAtual.getText().toString().trim();
                String novaSenha = etNovaSenha.getText().toString().trim();
                String confirmarSenha = etConfirmarSenha.getText().toString().trim();

                if (TextUtils.isEmpty(senhaAtual) || TextUtils.isEmpty(novaSenha) || TextUtils.isEmpty(confirmarSenha)) {
                    Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!novaSenha.equals(confirmarSenha)) {
                    Toast.makeText(getContext(), "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (novaSenha.length() < 6) {
                    Toast.makeText(getContext(), "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                String email = prefs.getString("emailLogado", "");

                AlterarSenhaRequest request = new AlterarSenhaRequest(email, senhaAtual, novaSenha);

                RetrofitClient.getInstance().getMyApi().updatePassword(request)
                        .enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Senha atual incorreta.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }
}