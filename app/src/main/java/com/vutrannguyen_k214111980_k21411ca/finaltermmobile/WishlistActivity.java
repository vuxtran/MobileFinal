package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vutrannguyen_k214111980_k21411ca.adapter.ProductAdapter;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.databinding.ActivityTotalProductBinding;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.databinding.ActivityWishlistBinding;
import com.vutrannguyen_k214111980_k21411ca.model.Product;
import com.vutrannguyen_k214111980_k21411ca.utilities.DatabaseHelper;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    ActivityWishlistBinding binding;
    BottomNavigationView bottomNavigationmenu;
    ImageButton btnBack;

    ListView listViewProduct;
    ArrayList<Product> wishlist;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setupActionBar();
        addEvents();

        dbHelper = new DatabaseHelper(this);
        listViewProduct = findViewById(R.id.lv_Product);

        // Retrieve wishlist items from database
        wishlist = dbHelper.getWishlist();

        // Set the adapter for the ListView
        ProductAdapter adapter = new ProductAdapter(this, wishlist);
        listViewProduct.setAdapter(adapter);
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
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.cart) {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void addEvents() {
        bottomNavigationmenu = findViewById(R.id.bottomNavigationmenu);
        bottomNavigationmenu.setSelectedItemId(R.id.item_wishlist);
        bottomNavigationmenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_wishlist) {
                return true;
            } else if (itemId == R.id.item_home) {
                navigateToActivity(HomeActivity.class);
                return true;
            } else if (itemId == R.id.item_product) {
                navigateToActivity(ProductActivity.class);
                return true;
            } else if (itemId == R.id.item_account) {
                navigateToActivity(AccountActivity.class);
                return true;
            }
            return false;
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}