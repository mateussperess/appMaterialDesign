package br.edu.ifsul.exemplodeaplicativocommaterialdesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Produto;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private EditText editTextProductValue;
    private Button buttonSaveProduct;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextProductName = findViewById(R.id.edit_text_product_name);
        editTextProductValue = findViewById(R.id.edit_text_product_value);
        buttonSaveProduct = findViewById(R.id.button_save_product);

        buttonSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to save the product will go here
                // For now, we'll just close the activity
                saveProduct(v);
            }
        });
        // exibe os valore, se for edição
        Intent it = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // New, safer way for Android 13+ (API 33+)
            produto = it.getSerializableExtra("produto", Produto.class);
        } else {
            // Old way for older Android versions
            @SuppressWarnings("deprecation")
            Produto tempProduto = (Produto) it.getSerializableExtra("produto");
            produto = tempProduto;
        }

        if(produto != null){
            editTextProductName.setText(produto.getNome());
            editTextProductValue.setText(produto.getValor().toString());
        }
    }

    public void saveProduct(View view) {
        String productName = editTextProductName.getText().toString();
        String productValue = editTextProductValue.getText().toString();
        // validação de campo
        if(productName.isEmpty()){
            editTextProductName.setError("O nome do produto é obrigatório");
            return;
        }
        if(productValue.isEmpty()){
            editTextProductName.setError("O valor do produto é obrigatório");
            return;
        }


        if(produto != null){
            produto.setNome(productName);
            produto.setValor(Double.parseDouble(productValue));
        }else{
            produto = new Produto();
            produto.setNome(productName);
            produto.setValor(Double.parseDouble(productValue));
        }
        // envia para o servidor
        // se houver ID atualiza, senão insere um novo produto
        // ---------------------- adiciona um produto novo --------------------
        if(produto.getId() == 0){
            //chamada ao serviço
            Call<Produto> call = RetrofitClient.getInstance().getMyApi().addProduct(produto);


            call.enqueue(new Callback<Produto>() {
                @Override
                public void onResponse(Call<Produto> call, Response<Produto> response) {


                    Log.e("PRODUTO-ADD", "Response CODE: "+response.code());
                    Log.e("PRODUTO-ADD", "Response message: "+response.message());


                    Produto produtoRespondido = response.body();
                    Log.e("PRODUTO-ADD", "Response: "+produtoRespondido);


                    // se resposta for 200, fecha a activity
                    if((response.code() == 200) && (produtoRespondido != null)){
                        produto = produtoRespondido;
                        Log.e("PRODUTO-ADD", "Response produdo ID inserido: "+produto.getId());
                        // informa o usuário
                        Toast.makeText(getApplicationContext(), "Resistro salvo com sucesso.", Toast.LENGTH_LONG).show();


                        // adiciona resultado
//                        setResult(Utils.ACTIVITY_RESPONSE_DADOS_MUDARAM);


                        // fecha a activity
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro no servidor.", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<Produto> call, Throwable t) {


                    Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();
                    Log.e("PRODUTO-ADD", t.toString());
                }
            });
        } else { // ---------------------- atualiza um produto --------------------
            //chamada ao serviço
            Call<Produto> call = RetrofitClient.getInstance().getMyApi().updateProduct(produto);

            call.enqueue(new Callback<Produto>() {
                @Override
                public void onResponse(Call<Produto> call, Response<Produto> response) {

                    Log.e("PRODUTO-UPDATE", "Response CODE: "+response.code());
                    Log.e("PRODUTO-UPDATE", "Response message: "+response.message());


                    Produto produtoRespondido = response.body();
                    Log.e("PRODUTO-UPDATE", "Response: "+response.raw().toString());
                    Log.e("PRODUTO-UPDATE", "Response: "+produtoRespondido);

                    // se resposta for 200, fecha a activity
                    if((response.code() == 200) && (produtoRespondido != null)){
                        produto = produtoRespondido;
                        Log.e("PRODUTO-UPDATE", "Response produdo ID atualizado: "+produto.getId());
                        // informa o usuário
                        Toast.makeText(getApplicationContext(), "Resistro salvo com sucesso.", Toast.LENGTH_LONG).show();

                        // fecha a activity
                        finish();
                    }
                }


                @Override
                public void onFailure(Call<Produto> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();
                    Log.e("PRODUTO-UPDATE", t.toString());
                }
            });
        }
    }

    public void excluirDado(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover registro");
        builder.setMessage("Você tem certeza que quer remover este dado?");


        builder.setPositiveButton("Sim!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //chamada ao serviço
                Call<Produto> call = RetrofitClient.getInstance().getMyApi().deleteProduct(produto.getId());

                call.enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> call, Response<Produto> response) {


                        Log.e("PRODUTO-DELETE", "Response CODE: "+response.code());
                        Log.e("PRODUTO-DELETE", "Response message: "+response.message());


                        Produto produtoRespondido = response.body();
                        Log.e("PRODUTO-DELETE", "Response: "+response.raw().toString());
                        Log.e("PRODUTO-DELETE", "Response: "+produtoRespondido);


                        // se resposta for 200, fecha a activity
                        if((response.code() == 200) && (produtoRespondido != null)){
                            produto = produtoRespondido;
                            Log.e("PRODUTO-DELETE", "Response produdo ID atualizado: "+produto.getId());
                            // informa o usuário
                            Toast.makeText(getApplicationContext(), "Registro removido com sucesso.", Toast.LENGTH_LONG).show();


                            // adiciona resultado
//                            setResult(Utils.ACTIVITY_RESPONSE_DADOS_MUDARAM);


                            // fecha a activity
                            finish();
                        }
                    }


                    @Override
                    public void onFailure(Call<Produto> call, Throwable t) {


                        Toast.makeText(getApplicationContext(), "Ocorreu um erro.", Toast.LENGTH_LONG).show();
                        Log.e("PRODUTO-UPDATE", t.toString());


                    }
                });
            }
        });


        builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

}
