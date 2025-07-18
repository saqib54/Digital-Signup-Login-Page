package com.example.myfirstapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etEmail, etAddress, etPassword, etConfirmPassword;
    private CheckBox checkBox;
    private RadioGroup rgUserType, rgGender;
    private Button btnSubmit;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initializeViews();
        setupSubmitButton();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        checkBox = findViewById(R.id.checkBox);
        rgUserType = findViewById(R.id.rgUserType);
        rgGender = findViewById(R.id.rgGender);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set password fields to hide input
        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                saveRegistrationData();
                showSuccessPopup();
            }
        });
    }

    private void saveRegistrationData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("fullName", etFullName.getText().toString().trim());
        editor.putString("email", etEmail.getText().toString().trim().toLowerCase());
        editor.putString("phone", etPhone.getText().toString().trim());
        editor.putString("password", etPassword.getText().toString().trim());
        editor.putString("address", etAddress.getText().toString().trim());

        int genderId = rgGender.getCheckedRadioButtonId();
        if (genderId != -1) {
            editor.putString("gender", ((RadioButton)findViewById(genderId)).getText().toString());
        }

        int userTypeId = rgUserType.getCheckedRadioButtonId();
        if (userTypeId != -1) {
            editor.putString("userType", ((RadioButton)findViewById(userTypeId)).getText().toString());
        }

        editor.apply();
        Log.d("REG_DATA", "Saved Email: " + sharedPreferences.getString("email", ""));
    }

    private void showSuccessPopup() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Registration Successful")
                .setMessage("You will be redirected to login in 10 seconds")
                .setCancelable(false)
                .create();

        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();
            navigateToLogin();
        }, 10000);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("email", etEmail.getText().toString().trim());
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Full Name Validation (letters and spaces only)
        String fullName = etFullName.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full Name is required");
            isValid = false;
        } else if (!fullName.matches("[a-zA-Z ]+")) {
            etFullName.setError("Only letters and spaces allowed");
            isValid = false;
        }

        // Phone Validation (exactly 11 digits)
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            isValid = false;
        } else if (!phone.matches("\\d{11}")) {
            etPhone.setError("Must be exactly 11 digits");
            isValid = false;
        }

        // Email Validation
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        // Address Validation
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            etAddress.setError("Address is required");
            isValid = false;
        }

        // Password Validation (min 8 chars, at least 1 uppercase)
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
            isValid = false;
        } else if (!password.matches(".*[A-Z].*")) {
            etPassword.setError("Must contain at least 1 capital letter");
            isValid = false;
        }

        // Confirm Password Validation
        String confirmPassword = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        // Gender Validation
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // User Type Validation
        if (rgUserType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select User Type", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Terms Checkbox Validation
        if (!checkBox.isChecked()) {
            Toast.makeText(this, "You must accept Terms & Conditions", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}