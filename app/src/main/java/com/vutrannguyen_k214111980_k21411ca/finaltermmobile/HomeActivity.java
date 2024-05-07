package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addEvents();
    }

    private void addEvents() {
        bottomNavigationmenu = findViewById(R.id.bottomNavigationmenu);
        bottomNavigationmenu.setSelectedItemId(R.id.item_home);
        bottomNavigationmenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_home) {
                return true;
            } else if (itemId == R.id.item_product) {
                navigateToActivity(ProductActivity.class);
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
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


}
