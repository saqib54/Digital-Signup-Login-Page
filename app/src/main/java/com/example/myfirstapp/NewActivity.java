package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewActivity extends AppCompatActivity {

    private RadioGroup rgDownloadReason, rgPurpose, rgImprovement;
    private Button btnSubmitSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knew);

        rgDownloadReason = findViewById(R.id.rgDownloadReason);
        rgPurpose = findViewById(R.id.rgPurpose);
        rgImprovement = findViewById(R.id.rgImprovement);
        btnSubmitSurvey = findViewById(R.id.btnSubmitSurvey);

        btnSubmitSurvey.setOnClickListener(v -> submitSurvey());
    }

    private void submitSurvey() {
        int selectedDownloadReason = rgDownloadReason.getCheckedRadioButtonId();
        int selectedPurpose = rgPurpose.getCheckedRadioButtonId();
        int selectedImprovement = rgImprovement.getCheckedRadioButtonId();

        if (selectedDownloadReason == -1 || selectedPurpose == -1 || selectedImprovement == -1) {
            Toast.makeText(this, "Please select an option for all categories.", Toast.LENGTH_SHORT).show();
        } else {

            RadioButton downloadReason = findViewById(selectedDownloadReason);
            RadioButton purpose = findViewById(selectedPurpose);
            RadioButton improvement = findViewById(selectedImprovement);

            String surveyResult = "Survey Results:\n" +
                    "Download Reason: " + downloadReason.getText() + "\n" +
                    "Purpose: " + purpose.getText() + "\n" +
                    "Improvement: " + improvement.getText();

            Toast.makeText(this, surveyResult, Toast.LENGTH_LONG).show();

            // Navigate to SignupActivity after showing the toast
            navigateToSignup();
        }
    }

    private void navigateToSignup() {
        // Create an intent to start the SignupActivity
        Intent intent = new Intent(NewActivity.this, MainActivity.class);

        // Clear the back stack so user can't go back to survey with back button
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish(); // Close this activity
    }
}