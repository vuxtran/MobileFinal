package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.databinding.ActivityAccountBinding;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.databinding.ActivityWishlistBinding;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    BottomNavigationView bottomNavigationmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {
        bottomNavigationmenu = findViewById(R.id.bottomNavigationmenu);
        bottomNavigationmenu.setSelectedItemId(R.id.item_account);
        bottomNavigationmenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_account) {
                return true;
            } else if (itemId == R.id.item_home) {
                navigateToActivity(HomeActivity.class);
                return true;
            } else if (itemId == R.id.item_product) {
                navigateToActivity(ProductActivity.class);
                return true;
            } else if (itemId == R.id.item_wishlist) {
                navigateToActivity(WishlistActivity.class);
                return true;
            }
            return false;
        });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}