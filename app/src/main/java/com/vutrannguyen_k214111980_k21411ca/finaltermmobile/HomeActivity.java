package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setupActionBar();
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
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.cart) {
            // Add your cart handling logic here
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
