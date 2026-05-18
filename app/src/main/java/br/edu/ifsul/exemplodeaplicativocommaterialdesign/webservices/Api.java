package br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices;

import java.util.List;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.AlterarSenhaRequest;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.LoginRequest;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.LoginResponse;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Produto;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {
//    String BASE_URL = "http://192.168.2.190/ws_aula/";
    String BASE_URL = "http://192.168.237.0/ws_aula/";


    // rotas RESTFUL para produto
    @GET("produto.php")
    Call<Produto> getProduct(@Header("id") String id);


    @GET("produto.php")
    Call<List<Produto>> getProducts(@Header("filter") String filterLike);


    @POST("produto.php")
    Call<Produto> addProduct(@Body Produto user);


    @PUT("produto.php")
    Call<Produto> updateProduct(@Body Produto user);


    @DELETE("produto.php")
    Call<Produto> deleteProduct(@Query("id") int productId);

    @POST("usuario.php")
    Call<Usuario> createUser(@Body Usuario usuario);

    @POST("login.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @PATCH("usuario.php")
    Call<Usuario> updatePassword(@Body AlterarSenhaRequest request);
}

