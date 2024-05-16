package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageViewProduct;
    TextView textViewName, textViewPrice, textViewSalePrice, textViewDescription, textViewInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setupActionBar();


        imageViewProduct = findViewById(R.id.imv_ProductThumbDetail);
        textViewName = findViewById(R.id.txt_ProductsDetailName);
        textViewPrice = findViewById(R.id.txt_ProductPrice);
        textViewSalePrice = findViewById(R.id.txt_ProductsSalePrice);
        textViewDescription = findViewById(R.id.txt_ProductsDetailDescription);
        textViewInventory = findViewById(R.id.txt_ProductsDetailQuantity);

        // Get data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("ProductName");
            String description = intent.getStringExtra("ProductDescription");
            double price = intent.getDoubleExtra("ProductPrice", 0);
            double salePrice = intent.getDoubleExtra("ProductSalePrice", 0);
            byte[] thumbnail = intent.getByteArrayExtra("ProductThumbnail");
            int inventory = intent.getIntExtra("ProductInventory", 10);

            // Set data to views
            textViewName.setText(name);
            textViewDescription.setText(description);
            textViewPrice.setText(formatVietnameseCurrency(price));
            textViewSalePrice.setText(formatVietnameseCurrency(salePrice));
            textViewInventory.setText(String.valueOf(inventory));

            if (thumbnail != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
                imageViewProduct.setImageBitmap(bitmap);
            }
        }
    }

    private void setupActionBar() {
        LinearLayout customActionBar = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(customActionBar, params);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            // Add your search handling logic here
            return true;
        } else if (itemId == R.id.cart) {
            // Add your cart handling logic here
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Helper method to format the currency in Vietnamese format
    private String formatVietnameseCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount) + "đ";
    }
}