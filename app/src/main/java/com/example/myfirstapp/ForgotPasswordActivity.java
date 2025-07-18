package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etOtp, etNewPassword, etConfirmPassword;
    private TextInputLayout tilOtp, tilNewPassword, tilConfirmPassword;
    private Button btnSendOtp, btnResetPassword;
    private String generatedOtp;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tilOtp = findViewById(R.id.tilOtp);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnSendOtp.setOnClickListener(v -> sendOtp());
        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void sendOtp() {
        String email = etEmail.getText().toString().trim();
        String savedEmail = sharedPreferences.getString("email", "").toLowerCase();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }

        if (!email.equals(savedEmail)) {
            etEmail.setError("Email not registered");
            return;
        }

        // Generate random 6-digit OTP
        generatedOtp = String.format("%06d", new Random().nextInt(999999));

        // In real app, send OTP to email (here we'll just show it for testing)
        Toast.makeText(this, "OTP sent: " + generatedOtp, Toast.LENGTH_LONG).show();

        // Show OTP and password fields
        tilOtp.setVisibility(View.VISIBLE);
        tilNewPassword.setVisibility(View.VISIBLE);
        tilConfirmPassword.setVisibility(View.VISIBLE);
        btnSendOtp.setVisibility(View.GONE);
        btnResetPassword.setVisibility(View.VISIBLE);
    }

    private void resetPassword() {
        String otp = etOtp.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (otp.isEmpty() || !otp.equals(generatedOtp)) {
            etOtp.setError("Invalid OTP");
            return;
        }

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords don't match");
            return;
        }

        // Save new password
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", newPassword);
        editor.apply();

        Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();

        // Go back to login
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}