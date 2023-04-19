package hanu.a2_2001040001;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_2001040001.adapters.ShoppingCartAdapter;
import hanu.a2_2001040001.database.ProductManager;
import hanu.a2_2001040001.models.Product;

public class CartActivity extends AppCompatActivity {
    ShoppingCartAdapter shoppingCartAdapter;
    List<Product> productList;
    ProductManager productManager;
    static TextView totalPrice;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        totalPrice = findViewById(R.id.total_price);

        productManager = ProductManager.getInstance(this);
        productList = productManager.all();
        shoppingCartAdapter = new ShoppingCartAdapter(productList);

        recyclerView = findViewById(R.id.recycle_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shoppingCartAdapter);

        updateTotal(productManager.getTotal());

    }

    public static void updateTotal(long total) {
        totalPrice.setText("đ " + String.valueOf(total));
    }
}
