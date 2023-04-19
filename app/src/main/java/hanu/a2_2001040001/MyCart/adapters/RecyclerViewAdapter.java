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

import hanu.a2_2001040001.MyCart.database.ProductManager;
import hanu.a2_2001040001.MyCart.models.Product;
import hanu.a2_2001040001.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Product> mListProduct;

    public RecyclerViewAdapter(List<Product> mListProduct) {
        this.mListProduct = mListProduct;

    }
    public void setFilteredList(List<Product> filteredList){
        this.mListProduct = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_infor, parent,false);
        return new MyViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = mListProduct.get(position);
        if( product == null){
            return;
        }
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
                            holder.imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.textView.setText(product.getName());
        holder.textView2.setText("đ " + String.valueOf(product.getUnitPrice()));
        holder.handleAddtoCart(product);

    }



    @Override
    public int getItemCount() {
        if(mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final Context context;
        ImageView imageView;
        TextView textView;
        TextView textView2;
        ImageButton addtocart;
        private ProductManager productManager;
        private Integer quantity = 0;
        public MyViewHolder(@NonNull View itemView, Context context){
            super(itemView);
            productManager = ProductManager.getInstance(context);
            this.context = context;
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            addtocart = itemView.findViewById(R.id.shopping);
        }
         public void handleAddtoCart(Product product) {
             addtocart.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     product.setQuantity(1);
                     if (productManager.add(product))
                         Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
                     else Toast.makeText(context, "Add failed", Toast.LENGTH_SHORT).show();
                     notifyDataSetChanged();
                 }
             });
         }
    }
}
