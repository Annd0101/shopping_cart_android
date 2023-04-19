package hanu.a2_2001040001.MyCart.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hanu.a2_2001040001.MyCart.CartActivity;
import hanu.a2_2001040001.MyCart.database.ProductManager;
import hanu.a2_2001040001.MyCart.models.Product;
import hanu.a2_2001040001.R;


public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyCartHolder> {

        private List<Product> mListProducts;

        public ShoppingCartAdapter(List<Product> products) {
            this.mListProducts = products;
        }

        @NonNull
        @Override
        public MyCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new MyCartHolder(view, parent.getContext());
        }

        @Override
        public void onBindViewHolder(@NonNull ShoppingCartAdapter.MyCartHolder holder, int position) {
            Product product = this.mListProducts.get(position);
            // Load the product thumbnail using an Executor
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(product.getThumbnail());
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        // Display the product thumbnail in the UI thread
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.thumbnail.setImageBitmap(bitmap);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.p_name.setText(product.getName());
            holder.u_price.setText("đ " + String.valueOf(product.getUnitPrice()));
            holder.total_price.setText(String.valueOf(product.getUnitPrice() * product.getQuantity()));
            holder.quantity.setText(String.valueOf(product.getQuantity()));
            holder.handleQuantity(product);
        }

        @Override
        public int getItemCount() {
            if(mListProducts != null){
                return mListProducts.size();
            }
            return 0;
        }

        public class MyCartHolder extends RecyclerView.ViewHolder {

            private Context context;
            private ImageView thumbnail;
            private TextView p_name;
            private TextView u_price;
            private TextView total_price;
            private TextView quantity;
            private Product product;
            private ImageButton minus;
            private ImageButton add;
            private ProductManager productManager;

            public MyCartHolder(@NonNull View itemView, Context context) {
                super(itemView);
                thumbnail = itemView.findViewById(R.id.image);
                p_name = itemView.findViewById(R.id.p_name);
                u_price = itemView.findViewById(R.id.u_price);
                total_price = itemView.findViewById(R.id.total_price);
                quantity = itemView.findViewById(R.id.quantity);
                minus = itemView.findViewById(R.id.minus);
                add = itemView.findViewById(R.id.add);
                productManager = ProductManager.getInstance(context);
                this.context = context;
            }

            public void handleQuantity(Product product) {
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        product.setQuantity(product.getQuantity() - 1);
                        if (product.getQuantity() == 0) {
                            if (productManager.delete(product.getId())) {
                                mListProducts.clear();
                                mListProducts.addAll(productManager.all());
                                notifyDataSetChanged();
                            }
                        } else {
                            if (!productManager.update(product))
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, "Update succeed", Toast.LENGTH_SHORT).show();
                        }
                        CartActivity.updateTotal(productManager.getTotal());

                        notifyDataSetChanged();
                    }
                });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                product.setQuantity(product.getQuantity() + 1);
                if (productManager.update(product))
                    Toast.makeText(context, "Update succeed", Toast.LENGTH_SHORT).show();
                else Toast.makeText(context, "Update fail", Toast.LENGTH_SHORT).show();
                CartActivity.updateTotal(productManager.getTotal());
                notifyDataSetChanged();
            }
            });
        }
        }
    }

