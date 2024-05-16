package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.vutrannguyen_k214111980_k21411ca.adapter.ProductAdapter;
import com.vutrannguyen_k214111980_k21411ca.model.Product;
import com.vutrannguyen_k214111980_k21411ca.utilities.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    EditText searchField;
    Button searchButton;
    GridView searchResults;
    ProductAdapter adapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        searchResults = findViewById(R.id.search_results);

        databaseHelper = new DatabaseHelper(this);  // Initialize the DatabaseHelper

        List<Product> initialList = new ArrayList<>(); // Empty initial list
        adapter = new ProductAdapter(this, initialList);
        searchResults.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(searchField.getText().toString());
            }
        });
    }

    private void performSearch(String query) {
        List<Product> results = databaseHelper.searchProducts(query);  // Use the database helper to search products
        adapter.clear();
        adapter.addAll(results);
        adapter.notifyDataSetChanged();
    }
}


