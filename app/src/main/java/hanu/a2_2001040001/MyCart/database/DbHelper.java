package hanu.a2_2001040001.MyCart.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "product_details.db";
        private static final int DB_VERSION = 1;


        public DbHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
           ;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DbSchema.ProductsTable.NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbSchema.ProductsTable.Columns.THUMBNAIL + " TEXT, " +
                    DbSchema.ProductsTable.Columns.NAME + " VARCHAR(255), " +
                    DbSchema.ProductsTable.Columns.CATEGORY + " VARCHAR(20), " +
                    DbSchema.ProductsTable.Columns.UNIT_PRICE + " INT, " +
                    DbSchema.ProductsTable.Columns.QUANTITY + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductsTable.NAME);

            onCreate(db);
        }
    }

