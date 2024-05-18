package com.vutrannguyen_k214111980_k21411ca.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.vutrannguyen_k214111980_k21411ca.model.Category;
import com.vutrannguyen_k214111980_k21411ca.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Perfume.db";
    private static String DB_PATH = "";
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 3);
        this.mContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        copyDatabase();
    }

    private void copyDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            try {
                copyDBFile();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create Cart table here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if necessary
    }

    public void addProductToWishlist(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ProductID", product.getId());
        values.put("ProductName", product.getName());
        values.put("ProductDescription", product.getDescription());
        values.put("ProductPrice", product.getPrice());
        values.put("SalePrice", product.getSalePrice());
        values.put("Thumbnail", product.getThumbnail());
        values.put("Inventory", product.getInventory());
        db.insert("Wishlist", null, values);
        db.close();
    }

    public ArrayList<Product> getWishlist() {
        ArrayList<Product> wishlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM Wishlist", null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("ProductID");
                int nameIndex = cursor.getColumnIndex("ProductName");
                int descriptionIndex = cursor.getColumnIndex("ProductDescription");
                int priceIndex = cursor.getColumnIndex("ProductPrice");
                int salePriceIndex = cursor.getColumnIndex("SalePrice");
                int thumbnailIndex = cursor.getColumnIndex("Thumbnail");
                int inventoryIndex = cursor.getColumnIndex("Inventory");

                if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || priceIndex == -1 ||
                        salePriceIndex == -1 || thumbnailIndex == -1 || inventoryIndex == -1) {
                    Log.e("DatabaseHelper", "Column name is incorrect or does not exist in the result set.");
                    return wishlist; // Return an empty list or handle the error appropriately
                }

                do {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String description = cursor.getString(descriptionIndex);
                    double price = cursor.getDouble(priceIndex);
                    double salePrice = cursor.getDouble(salePriceIndex);
                    byte[] thumbnail = cursor.getBlob(thumbnailIndex);
                    int inventory = cursor.getInt(inventoryIndex);

                    wishlist.add(new Product(id, name, description, price, salePrice, "", thumbnail, inventory));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return wishlist;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT CategoryID, CategoryName, CategoryImage FROM CATEGORY", null);
        int idIndex = cursor.getColumnIndex("CategoryID");
        int nameIndex = cursor.getColumnIndex("CategoryName");
        int thumbnailIndex = cursor.getColumnIndex("CategoryImage");

        if (idIndex == -1 || nameIndex == -1 || thumbnailIndex == -1) {
            Log.e("DatabaseHelper", "One or more column names are incorrect or the table doesn't exist.");
            cursor.close();
            return categories;
        }

        if (cursor.moveToFirst()) {
            do {
                String categoryId = cursor.getString(idIndex);
                String categoryName = cursor.getString(nameIndex);
                byte[] thumbnail = cursor.getBlob(thumbnailIndex);
                categories.add(new Category(categoryId, categoryName, thumbnail));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT WHERE CategoryID = ?", new String[]{categoryId});

        int idIndex = cursor.getColumnIndex("ProductID");
        int nameIndex = cursor.getColumnIndex("ProductName");
        int descriptionIndex = cursor.getColumnIndex("Description");
        int priceIndex = cursor.getColumnIndex("ProductPrice");
        int salePriceIndex = cursor.getColumnIndex("SalePrice");
        int categoryIndex = cursor.getColumnIndex("CategoryID");
        int thumbnailIndex = cursor.getColumnIndex("Thumbnail");
        int inventoryIndex = cursor.getColumnIndex("Inventory");

        // Ensure all indices are valid
        if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || priceIndex == -1 ||
                salePriceIndex == -1 || categoryIndex == -1 || thumbnailIndex == -1 || inventoryIndex == -1) {
            Log.e("DatabaseHelper", "One or more column names are incorrect or the table doesn't exist.");
            cursor.close();
            return products; // Return an empty list or handle the error appropriately
        }

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descriptionIndex);
                double price = cursor.getDouble(priceIndex);
                double salePrice = cursor.getDouble(salePriceIndex);
                String category = cursor.getString(categoryIndex);
                byte[] thumbnail = cursor.getBlob(thumbnailIndex);
                int inventory = cursor.getInt(inventoryIndex);

                products.add(new Product(id, name, description, price, salePrice, category, thumbnail, inventory));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT WHERE ProductName LIKE ? OR Description LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"});

        // Getting column indices safely
        int idIndex = cursor.getColumnIndex("ProductID");
        int nameIndex = cursor.getColumnIndex("ProductName");
        int descriptionIndex = cursor.getColumnIndex("Description");
        int priceIndex = cursor.getColumnIndex("ProductPrice");
        int salePriceIndex = cursor.getColumnIndex("SalePrice");
        int categoryIndex = cursor.getColumnIndex("CategoryID");
        int thumbnailIndex = cursor.getColumnIndex("Thumbnail");
        int inventoryIndex = cursor.getColumnIndex("Inventory");

        // Check if any index is -1
        if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || priceIndex == -1 ||
                salePriceIndex == -1 || categoryIndex == -1 || thumbnailIndex == -1 || inventoryIndex == -1) {
            Log.e("DatabaseHelper", "One or more columns are missing in the result set.");
            cursor.close();
            return products; // Early return with empty list
        }

        while (cursor.moveToNext()) {
            Product product = new Product(
                    cursor.getInt(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(descriptionIndex),
                    cursor.getDouble(priceIndex),
                    cursor.getDouble(salePriceIndex),
                    cursor.getString(categoryIndex),
                    cursor.getBlob(thumbnailIndex),
                    cursor.getInt(inventoryIndex)
            );
            products.add(product);
        }
        cursor.close();
        db.close();
        return products;
    }

    // Method to get all products from SQLite
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT", null);

        int idIndex = cursor.getColumnIndex("ProductID");
        int nameIndex = cursor.getColumnIndex("ProductName");
        int descriptionIndex = cursor.getColumnIndex("Description");
        int priceIndex = cursor.getColumnIndex("ProductPrice");
        int salePriceIndex = cursor.getColumnIndex("SalePrice");
        int categoryIndex = cursor.getColumnIndex("CategoryID");
        int thumbnailIndex = cursor.getColumnIndex("Thumbnail");
        int inventoryIndex = cursor.getColumnIndex("Inventory");

        if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || priceIndex == -1 ||
                salePriceIndex == -1 || categoryIndex == -1 || thumbnailIndex == -1 || inventoryIndex == -1) {
            Log.e("DatabaseHelper", "One or more column names are incorrect or the table doesn't exist.");
            cursor.close();
            return productList; // Return an empty list or handle the error appropriately
        }

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descriptionIndex);
                double price = cursor.getDouble(priceIndex);
                double salePrice = cursor.getDouble(salePriceIndex);
                String categoryId = cursor.getString(categoryIndex);
                byte[] thumbnail = cursor.getBlob(thumbnailIndex);
                int inventory = cursor.getInt(inventoryIndex);

                Product product = new Product(id, name, description, price, salePrice, categoryId, thumbnail, inventory);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
}
