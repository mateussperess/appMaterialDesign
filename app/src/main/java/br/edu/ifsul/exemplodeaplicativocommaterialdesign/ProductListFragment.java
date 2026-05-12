package br.edu.ifsul.exemplodeaplicativocommaterialdesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.exemplodeaplicativocommaterialdesign.modelo.Produto;
import br.edu.ifsul.exemplodeaplicativocommaterialdesign.webservices.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProdutoAdapter productAdapter;
    private List<Produto> productList;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        fab = view.findViewById(R.id.fab_add_product);

        // Initialize product list (replace with actual data loading)
        productList = new ArrayList<>();
        // Add some dummy data for now
//        productList.add(new Produto(1, "Sample Product 1", 19.99));
//        productList.add(new Produto(2, "Sample Product 2", 29.99));
//        productList.add(new Produto(3, "Sample Product 3", 9.99));

        productAdapter = new ProdutoAdapter(productList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddProductActivity
                Intent intent = new Intent(getActivity(), ProductEditActivity.class);
                startActivity(intent);
            }
        });

        addMenuComAcoesNaToolbar();
        return view;
    }

    // You might need to override onActivityCreated or onResume to refresh data if it changes
    @Override
    public void onResume() {
        super.onResume();
        // In a real app, you would refresh your productList here
        // For example, by fetching from a database or API
        // productAdapter.notifyDataSetChanged(); // If the data source has changed
        carregarProdutos("");
    }

    private void addMenuComAcoesNaToolbar() {
        // Get the MenuHost (usually the Activity)
        MenuHost menuHost = requireActivity();


        // Adiciona o título deste fragmento na Toolbar
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista de Produtos");
        }


        // Add the MenuProvider
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Inflate your menu here
                menuInflater.inflate(R.menu.meu_list_produtos, menu);


                // Setup your SearchView here
                MenuItem searchItem = menu.findItem(R.id.app_bar_search);
                SearchView searchView = (SearchView) searchItem.getActionView();


                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        carregarProdutos(query);
                        return true;
                    }


                    @Override
                    public boolean onQueryTextChange(String newText) {
                        carregarProdutos(newText);
                        return true;
                    }
                });
            }


            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Handle clicks (like the old onOptionsItemSelected)
                if (menuItem.getItemId() == R.id.app_bar_search) {
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }




    private void carregarProdutos(String filtro) {
        // Call the service
        Call<List<Produto>> call = RetrofitClient.getInstance().getMyApi().getProducts(filtro);


        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                Log.d("PRODUTO-LIST", "Response CODE: " + response.code());


                List<Produto> produtoRespondido = response.body();
                if ((response.code() == 200) && (produtoRespondido != null)) {
                    productAdapter.productList.clear();
                    productAdapter.productList.addAll(produtoRespondido);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar produtos.", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {


                Toast.makeText(getContext(), "Ocorreu um erro na rede.", Toast.LENGTH_LONG).show();
                Log.e("PRODUTO-LIST", t.toString());
                t.printStackTrace();
            }
        });
    }

}

// Helper class for ProductAdapter (you'll need to create this file or define it within this file)
class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProductViewHolder> {

    public List<Produto> productList;
    private Context appContext;


    public ProdutoAdapter(List<Produto> productList, Context appContext) {
        this.productList = productList;
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Produto product = productList.get(position);
        holder.bind(product);
        // levar conteúdo para a próxima tela
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(appContext, ProductEditActivity.class);
                it.putExtra("produto",product);
                appContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productValue; // Assuming you want to display value

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productValue = itemView.findViewById(R.id.product_value); // Assuming you have this ID in item_product.xml
        }



        public void bind(Produto product) {
            productName.setText(product.getNome());
            productValue.setText(String.valueOf(product.getValor())); // Format as needed
        }
    }
}
