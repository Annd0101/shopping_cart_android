package hanu.a2_2001040001.MyCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_2001040001.MyCart.adapters.RecyclerViewAdapter;
import hanu.a2_2001040001.MyCart.models.Product;
import hanu.a2_2001040001.R;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    private ImageButton search_button;
    private List<Product> mListProduct;
    private SearchView searchView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // search view
        searchView = findViewById(R.id.search_view);
        // recycle view
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        // make divider for layout
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        DividerItemDecoration itemDecoration1 = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration1);

        // main part
        mListProduct = new ArrayList<>();
        handleSearchView();
        callApiGetProducts();
    }

    private void handleSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void filterList(String s) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : mListProduct) {
            if (product.getName().toLowerCase().trim().contains(s.toLowerCase())) {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            recyclerViewAdapter.setFilteredList(filteredList);
        }
    }

    private void callApiGetProducts() {
//        ProductService.productService.getProducts().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                mListProduct = response.body();
//                recyclerViewAdapter = new RecyclerViewAdapter(mListProduct);
//                recyclerView.setAdapter(recyclerViewAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executorService.execute(new Runnable() {
            @Override
            public void run() {
                //load json
                String json = loadJSON("https://hanu-congnv.github.io/mpr-cart-api/products.json");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(json == null ){
                            Toast.makeText(MainActivity.this, "Opps", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONArray root = new JSONArray(json);
                            int i;
                            for(i = 0 ; i < root.length(); i++){
                                JSONObject product = root.getJSONObject(i);
                                Long id = product.getLong("id");
                                String thumbnail = product.getString("thumbnail");
                                String name  = product.getString("name");
                                String category = product.getString("category");
                                Integer unitPrice = product.getInt("unitPrice");
                                mListProduct.add(new Product(id,thumbnail,name,category,unitPrice));
                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(mListProduct);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_cart: // Replace "menu_item_id" with the ID of your menu item
                Intent intent = new Intent(this, CartActivity.class); // Replace "SecondActivity" with the name of your second activity
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}