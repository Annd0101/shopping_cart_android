package hanu.a2_2001040001.MyCart.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040001.MyCart.models.Product;


public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Product getProduct() throws Exception {

            String id = getString(getColumnIndex(DbSchema.ProductsTable.Columns.ID));
            String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Columns.THUMBNAIL));
            String name = getString(getColumnIndex(DbSchema.ProductsTable.Columns.NAME));
            String category = getString(getColumnIndex(DbSchema.ProductsTable.Columns.CATEGORY));
            String unitPrice = getString(getColumnIndex(DbSchema.ProductsTable.Columns.UNIT_PRICE));
            String quantity = getString(getColumnIndex(DbSchema.ProductsTable.Columns.QUANTITY));

            Product product = new Product(Long.parseLong(id), thumbnail, name, category, Integer.parseInt(unitPrice), Integer.parseInt(quantity));
        return product;
    }
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            Product product = null;
            try {
                product = getProduct();
            } catch (Exception e) {
                moveToNext();
                continue;
            }
            products.add(product);
            moveToNext();
        }
        return products;
    }
}
