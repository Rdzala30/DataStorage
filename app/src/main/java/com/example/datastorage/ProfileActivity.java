package com.example.datastorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private Button updateNameBtn;
    private SessionManager sessionManager;
    private EditText newNameTxt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        textViewWelcome = findViewById(R.id.textViewWelcome);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewContact = findViewById(R.id.textViewContact);
        Button logOutBtn = findViewById(R.id.logOutBtnId);
        TextView editText = findViewById(R.id.editTextViewId);
        newNameTxt = findViewById(R.id.newNameId);
        updateNameBtn = findViewById(R.id.updateBtnId);

        // Initialize session manager
        sessionManager = new SessionManager(getApplicationContext());

        // Retrieve user information from the session
        String name = sessionManager.getSessionDetails("key_session_name");
        String email = sessionManager.getSessionDetails("key_session_email");
        String number = sessionManager.getSessionDetails("key_session_contact");

        if (name != null) {
            // User is logged in, welcome the user
            textViewWelcome.setText("Welcome, " + name);
            textViewEmail.setText(email);
            textViewContact.setText("Contact: " + number);
        } else {
            // User is not logged in, redirect to login activity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close this activity so the user cannot go back
            return;
        }

        // Show new name EditText and update button when "Edit" is clicked
        editText.setOnClickListener(view -> {
            newNameTxt.setVisibility(View.VISIBLE);
            updateNameBtn.setVisibility(View.VISIBLE);
        });

        // Update name button click listener
        updateNameBtn.setOnClickListener(view -> {
            updateUserName(newNameTxt.getText().toString());
            AppDatabase database = AppDatabase.getInstance(ProfileActivity.this.getApplicationContext());
            UserTable userTable = database.userDao().getUserByUserEmail(email);
            String newName = sessionManager.saveName(userTable.getFullName());
            String contact = sessionManager.saveContact(userTable.getContact());
            sessionManager.createSession(newName, email, contact);
            newNameTxt.setVisibility(View.INVISIBLE);
            updateNameBtn.setVisibility(View.INVISIBLE);
        });

        // Logout button click listener
        logOutBtn.setOnClickListener(v -> {
            // Clear session
            sessionManager.logoutSession();
            Toast.makeText(ProfileActivity.this, "Log Out Successful", Toast.LENGTH_SHORT).show();
            // Redirect to login activity
            goToLoginActivity();
            finish(); // Close this activity so the user cannot go back
        });
    }

    // Method to navigate to the Login activity
    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void updateUserName(final String newName) {
        // Get the user's email from the session
        final String userEmail = sessionManager.getSessionDetails("key_session_email");
        AppDatabase database = AppDatabase.getInstance(ProfileActivity.this);

        // Check if the user's email is available
        if (userEmail != null) {
            // Execute the update query in a background thread
            new Thread(() -> {
                // Update the user's name in the database
                database.userDao().updateUserName(newName, userEmail);

                // Update the displayed name in the UI
                runOnUiThread(() -> {
                    // Update the welcome message with the new name
                    textViewWelcome.setText("Welcome, " + newName);

                    // Show a message indicating successful update
                    Toast.makeText(ProfileActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                });
            }).start();
        } else {
            // Handle case where user email is not available (should not occur in a logged-in state)
            Toast.makeText(ProfileActivity.this, "Error: User email not found", Toast.LENGTH_SHORT).show();
        }
    }
}
