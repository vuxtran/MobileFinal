package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vutrannguyen_k214111980_k21411ca.model.Account;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtForgot, txtSignup;

    CheckBox chkSaveInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                login(userName, password);

            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login(final String userName, final String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Account");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean loginSuccessful = false;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        assert account != null;
                        if (account.getUserName().equals(userName) && account.getPassWord().equals(password)) {
                            loginSuccessful = true;
                            break;
                        }
                    }
                    if (loginSuccessful) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Intent to navigate to another activity after successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed: Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnCreateAccount);
        txtForgot = findViewById(R.id.txtForgot);
        txtSignup = findViewById(R.id.txtSignup);
        chkSaveInfor = findViewById(R.id.chkSaveInfor);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (chkSaveInfor.isChecked()) {
            String userName = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            saveLoginInformation(userName, password);
        } else {
            clearLoginInformation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoginInformation();
    }
    private void loadLoginInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedUserName = sharedPreferences.getString("userName", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean isSaved = sharedPreferences.getBoolean("saveLogin", false);

        if (isSaved) {
            edtEmail.setText(savedUserName);
            edtPassword.setText(savedPassword);
            chkSaveInfor.setChecked(true);
        }
    }
    private void saveLoginInformation(String userName, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.putString("password", password);
        editor.putBoolean("saveLogin", true);
        editor.apply();
    }

    private void clearLoginInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userName");
        editor.remove("password");
        editor.putBoolean("saveLogin", false);
        editor.apply();
    }
}