package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vutrannguyen_k214111980_k21411ca.adapter.CategoryAdapter;
import com.vutrannguyen_k214111980_k21411ca.adapter.ProductAdapter;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.databinding.ActivityTotalProductBinding;
import com.vutrannguyen_k214111980_k21411ca.model.Category;
import com.vutrannguyen_k214111980_k21411ca.model.Product;
import com.vutrannguyen_k214111980_k21411ca.utilities.DatabaseHelper;

import java.util.List;

public class ProductActivity extends AppCompatActivity {
    ActivityTotalProductBinding binding;
    BottomNavigationView bottomNavigationmenu;
    private DatabaseHelper databaseHelper;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTotalProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        // Set up the custom action bar
        setupActionBar();

        loadCategories();
        addEvents();
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
    private void loadCategories() {
        try {
            List<Category> categories = databaseHelper.getAllCategories();
            if (categories != null && !categories.isEmpty()) {
                categoryAdapter = new CategoryAdapter(this, categories);
                binding.gridCategory.setAdapter(categoryAdapter);
                loadProducts(categories.get(0).getId());
                binding.gridCategory.setOnItemClickListener((parent, view, position, id) -> {
                    Category selectedCategory = categoryAdapter.getItem(position);
                    if (selectedCategory != null) {
                        loadProducts(selectedCategory.getId());
                    }
                });
            } else {
                Toast.makeText(this, "No categories found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load categories: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ProductActivity", "Error loading categories", e);
        }
    }

    private void loadProducts(String categoryId) {
        List<Product> products = databaseHelper.getProductsByCategory(categoryId);
        if (products != null && !products.isEmpty()) {
            productAdapter = new ProductAdapter(this, products);
            binding.gridProduct.setAdapter(productAdapter);
        } else {
            Toast.makeText(this, "No products found for this category", Toast.LENGTH_SHORT).show();
        }
    }
    private void addEvents() {
        bottomNavigationmenu = findViewById(R.id.bottomNavigationmenu);
        bottomNavigationmenu.setSelectedItemId(R.id.item_product);
        bottomNavigationmenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_product) {
                return true;
            } else if (itemId == R.id.item_home) {
                navigateToActivity(HomeActivity.class);
                return true;
            } else if (itemId == R.id.item_wishlist) {
                navigateToActivity(WishlistActivity.class);
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