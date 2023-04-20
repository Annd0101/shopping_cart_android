package hanu.a2_2001040001.MyCart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_2001040001.MyCart.adapters.ShoppingCartAdapter;
import hanu.a2_2001040001.MyCart.database.ProductManager;
import hanu.a2_2001040001.MyCart.models.Product;
import hanu.a2_2001040001.R;


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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shoppingCartAdapter);
        updateTotal(productManager.getTotal());

    }

    public static void updateTotal(long total) {
        totalPrice.setText("đ " + String.valueOf(total));
    }
}
