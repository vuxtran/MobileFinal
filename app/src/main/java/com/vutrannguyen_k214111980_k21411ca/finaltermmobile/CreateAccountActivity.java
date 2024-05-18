package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vutrannguyen_k214111980_k21411ca.model.Account;

public class CreateAccountActivity extends AppCompatActivity {
    Button btnCreateAccount;
    EditText edtEmail, edtPassword, edtConfirm, edtFullName, edtAddress, edtPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        addViews();
        addEvents();
    }

    private void addViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirm = findViewById(R.id.edtConfirm);
        edtFullName = findViewById(R.id.edtFullName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
    }

    private void addEvents() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirm.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();

        if (email.isEmpty() || !email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                long nextId = count + 1;
                String userId = "userId" + nextId;
                saveNewAccount(userId, nextId, email, password, fullName, address, phoneNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreateAccountActivity.this, "Failed to access database: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveNewAccount(String userId, long nextId, String email, String password, String fullName, String address, String phoneNumber) {
        Account newAccount = new Account(userId, (int) nextId, password, fullName, address, phoneNumber, email);
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.child(userId).setValue(newAccount)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                    navigateToLogin();
                })
                .addOnFailureListener(e -> Toast.makeText(CreateAccountActivity.this, "Failed to create account: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
