package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vutrannguyen_k214111980_k21411ca.model.Account;

public class AccountActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etAddress, etPhoneNumber;
    private Button btnModifyAccount;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        addViews();
        getUserIdFromPreferences();
        loadAccountInformation();
        addEvents();
    }

    private void addViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnModifyAccount = findViewById(R.id.btnModifyAccount);
    }

    private void getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
    }

    private void loadAccountInformation() {
        if (userId == null) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Account").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                if (account != null) {
                    etFullName.setText(account.getFullName());
                    etEmail.setText(account.getEmail());
                    etAddress.setText(account.getAddress());
                    etPhoneNumber.setText(account.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AccountActivity.this, "Failed to load account information: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addEvents() {
        btnModifyAccount.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String address = etAddress.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();

            updateAccountInformation(fullName, email, address, phoneNumber);
        });
    }

    private void updateAccountInformation(String fullName, String email, String address, String phoneNumber) {
        // Assuming you are not changing the ID and password here
        Account updatedAccount = new Account(userId, Integer.parseInt(userId.replaceAll("[^0-9]", "")), null, fullName, address, phoneNumber, email);

        databaseReference.setValue(updatedAccount)
                .addOnSuccessListener(aVoid -> Toast.makeText(AccountActivity.this, "Account updated successfully!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(AccountActivity.this, "Failed to update account: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
