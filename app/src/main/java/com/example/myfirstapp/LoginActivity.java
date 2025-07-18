package com.example.myfirstapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnShowData = findViewById(R.id.btnShowData);
        Button btnGoToSurvey = findViewById(R.id.btnGoToSurvey);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Pre-fill email if passed from registration
        String prefilledEmail = getIntent().getStringExtra("email");
        if (prefilledEmail != null) {
            etEmail.setText(prefilledEmail);
        }

        btnLogin.setOnClickListener(v -> validateLogin());
        btnShowData.setOnClickListener(v -> showSavedData());
        btnGoToSurvey.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, NewActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void validateLogin() {
        String email = etEmail.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim();

        String savedEmail = sharedPreferences.getString("email", "").toLowerCase();
        String savedPassword = sharedPreferences.getString("password", "");

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
        } else if (!email.equals(savedEmail)) {
            showError("Incorrect email. Try: " + savedEmail);
        } else if (!password.equals(savedPassword)) {
            showError("Incorrect password");
        } else {
            showSuccess();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showSuccess() {
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }

    private void showSavedData() {
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String gender = sharedPreferences.getString("gender", "N/A");
        String userType = sharedPreferences.getString("userType", "N/A");

        String data = "Saved Registration Data:\n\n" +
                "Name: " + fullName + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Gender: " + gender + "\n" +
                "Type: " + userType;

        new AlertDialog.Builder(this)
                .setTitle("Your Registration Info")
                .setMessage(data)
                .setPositiveButton("OK", null)
                .show();
    }
}