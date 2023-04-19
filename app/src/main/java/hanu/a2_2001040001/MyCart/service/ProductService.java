package hanu.a2_2001040001.MyCart.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

;
import hanu.a2_2001040001.MyCart.models.Product;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ProductService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://hanu-congnv.github.io/mpr-cart-api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    ProductService productService = retrofit.create(ProductService.class);
    @GET("https://hanu-congnv.github.io/mpr-cart-api/products.json")
    Call<List<Product>> getProducts();
}
