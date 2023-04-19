package hanu.a2_2001040001.MyCart.database;

public class DbSchema {
    class ProductsTable {
        public static final String NAME = "product_details";

        class Columns {
            public static final String ID = "id";

            public static final String THUMBNAIL = "thumbnail";

            public static final String NAME = "name";

            public static final String CATEGORY = "category";

            public static final String UNIT_PRICE = "unit_price";

            public static final String QUANTITY = "quantity";

        }
    }
}
