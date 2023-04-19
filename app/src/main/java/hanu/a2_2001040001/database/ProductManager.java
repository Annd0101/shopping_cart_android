package hanu.a2_2001040001.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040001.models.Product;

public class ProductManager {
    private static ProductManager instance;
    private static final String INSERT_STMT =
            "INSERT INTO " + DbSchema.ProductsTable.NAME + "("
                    + DbSchema.ProductsTable.Columns.THUMBNAIL + ", "
                    + DbSchema.ProductsTable.Columns.NAME + ", "
                    + DbSchema.ProductsTable.Columns.CATEGORY + ", "
                    + DbSchema.ProductsTable.Columns.UNIT_PRICE + ", "
                    + DbSchema.ProductsTable.Columns.QUANTITY + ") VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_STMT =
            "UPDATE " + DbSchema.ProductsTable.NAME +
                    " SET " + DbSchema.ProductsTable.Columns.THUMBNAIL + " = ?, "
                    + DbSchema.ProductsTable.Columns.NAME + " = ?, "
                    + DbSchema.ProductsTable.Columns.CATEGORY + " = ?, "
                    + DbSchema.ProductsTable.Columns.UNIT_PRICE + " = ?, "
                    + DbSchema.ProductsTable.Columns.QUANTITY + " = ? " +
                    " WHERE " + DbSchema.ProductsTable.Columns.ID + " = ?";
    private final DbHelper dbHelper;
    private final SQLiteDatabase db;
    private final Context context;

    public static ProductManager getInstance(Context context) {
        if (instance == null) return new ProductManager(context);
        return instance;
    }
    public ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        this.context = context;
    }
    public List<Product> all() {
        String query = "SELECT * FROM product_details";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null );
        }
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper.getProducts();
    }
    public long getTotal() {
        long total = 0;
        for (Product prdt : all()) {
            total += (long) prdt.getUnitPrice()*prdt.getQuantity();
        }
        return total;
    }
    public boolean add(Product product) {
        for (Product p : all()) {
            if (product.equals(p)) {
                p.setQuantity(p.getQuantity() + 1);
                update(p);
                return true;
            }
        }
        SQLiteStatement statement = db.compileStatement(INSERT_STMT);
        statement.bindString(1, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindString(3, product.getCategory());
        statement.bindString(4, String.valueOf(product.getUnitPrice()));
        statement.bindString(5, String.valueOf(product.getQuantity()));

        long id = statement.executeInsert();
        if (id > 0) {
            product.setId(id);
            return true;
        }
        return false;
    }
    public boolean update(Product product) {
        SQLiteStatement statement = db.compileStatement(UPDATE_STMT);
        statement.bindString(1, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindString(3, product.getCategory());
        statement.bindString(4, String.valueOf(product.getUnitPrice()));
        statement.bindString(5, String.valueOf(product.getQuantity()));
        statement.bindString(6, String.valueOf(product.getId()));

        long id = statement.executeUpdateDelete();
        return id > 0;

    }

    public boolean delete(long id) {
        int result = db.delete(DbSchema.ProductsTable.NAME, "id = ?", new String[]{id + ""});
        return result > 0;
    }
}
