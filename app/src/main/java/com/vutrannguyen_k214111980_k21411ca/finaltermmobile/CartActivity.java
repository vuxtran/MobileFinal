package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vutrannguyen_k214111980_k21411ca.adapter.CartAdapter;
import com.vutrannguyen_k214111980_k21411ca.model.Cart;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    private RecyclerView recyclerViewCart;
    private TextView txtCost, txtTotalPayment;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private DatabaseReference firebaseDatabase;
    Button btnOrder;

    ImageView imv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerViewCart = findViewById(R.id.rv_GioHang);
        txtCost = findViewById(R.id.txt_Cost);
        txtTotalPayment = findViewById(R.id.txt_Totalcost);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList);
        recyclerViewCart.setAdapter(cartAdapter);

        btnOrder = findViewById(R.id.btn_DatHang);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderIntent = new Intent(CartActivity.this, OrderMethodActivity.class);
                startActivity(orderIntent);
            }
        });

        imv_back = findViewById(R.id.imv_Back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchCartDataFromFirebase();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Cart cart = cartList.get(position);
                removeItemFromFirebase(cart);
                cartList.remove(position);
                cartAdapter.notifyItemRemoved(position);
                updateTotalCost();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewCart);
    }

    private void fetchCartDataFromFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Cart");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartList.clear();
                double totalCost = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Cart cart = snapshot.getValue(Cart.class);
                        if (cart != null) {
                            cartList.add(cart);
                            totalCost += cart.getSalePrice();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing cart item: ", e);
                        Log.e(TAG, "DataSnapshot: " + snapshot.toString());
                    }
                }

                cartAdapter.notifyDataSetChanged();

                // Display the total cost
                txtCost.setText(formatVietnameseCurrency(totalCost));
                txtTotalPayment.setText(formatVietnameseCurrency(totalCost));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void removeItemFromFirebase(Cart cart) {
        DatabaseReference cartItemRef = firebaseDatabase.child(String.valueOf(cart.getProductId())); // Assuming you have a productId in your Cart model
        cartItemRef.removeValue().addOnSuccessListener(aVoid -> Log.d(TAG, "Item removed from Firebase"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to remove item from Firebase", e));
    }

    private void updateTotalCost() {
        double totalCost = 0;
        for (Cart cart : cartList) {
            totalCost += cart.getSalePrice();
        }
        txtCost.setText(formatVietnameseCurrency(totalCost));
        txtTotalPayment.setText(formatVietnameseCurrency(totalCost));
    }

    private String formatVietnameseCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount) + "đ";
    }
}
