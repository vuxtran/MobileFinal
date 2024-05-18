package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class OrderMethodActivity extends AppCompatActivity {

    private static final String TAG = "OrderMethodActivity";
    private RecyclerView recyclerViewCartItems;
    private EditText etVoucher;
    private TextView txtShippingCost, txtTotalCost;
    private Spinner spinnerPaymentMethod;

    private Button btnOrder;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private DatabaseReference firebaseDatabase;
    private final double SHIPPING_COST = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_method);

        recyclerViewCartItems = findViewById(R.id.rv_CartItems);
        etVoucher = findViewById(R.id.et_Voucher);
        txtShippingCost = findViewById(R.id.txt_ShippingCost);
        txtTotalCost = findViewById(R.id.txt_TotalCost);
        spinnerPaymentMethod = findViewById(R.id.spinner_PaymentMethod);

        btnOrder = findViewById(R.id.btn_Order);

        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList);
        recyclerViewCartItems.setAdapter(cartAdapter);

        fetchCartDataFromFirebase();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyVoucherAndCalculateTotal();
            }
        });
    }

    private void fetchCartDataFromFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Cart");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Cart cart = snapshot.getValue(Cart.class);
                        if (cart != null) {
                            cartList.add(cart);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing cart item: ", e);
                        Log.e(TAG, "DataSnapshot: " + snapshot.toString());
                    }
                }
                cartAdapter.notifyDataSetChanged();
                calculateTotalCost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void applyVoucherAndCalculateTotal() {
        String voucherCode = etVoucher.getText().toString().trim();
        double totalCost = calculateTotalCost();
        if ("HE50".equalsIgnoreCase(voucherCode)) {
            totalCost *= 0.5;
        }
        totalCost += SHIPPING_COST;  // Adding shipping cost
        txtTotalCost.setText(formatVietnameseCurrency(totalCost));
    }

    private double calculateTotalCost() {
        double totalCost = 0;
        for (Cart cart : cartList) {
            totalCost += cart.getSalePrice();
        }
        txtTotalCost.setText(formatVietnameseCurrency(totalCost + SHIPPING_COST));  // Adding shipping cost
        return totalCost;
    }

    private String formatVietnameseCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###'đ'");
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }
}
