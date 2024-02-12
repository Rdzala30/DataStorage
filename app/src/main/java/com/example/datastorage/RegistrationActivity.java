package com.example.datastorage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    // UI elements
    EditText emailId, password, fullName, contact;
    TextView loginNow;
    Button signUpBtn;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI elements
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.Password);
        fullName = findViewById(R.id.FullName);
        contact = findViewById(R.id.Contact);
        loginNow = findViewById(R.id.LoginNow);
        signUpBtn = findViewById(R.id.SignUpBtn);
        sessionManager = new SessionManager(getApplicationContext());

        // When Login button is clicked, navigate to the login activity
        loginNow.setOnClickListener(v -> goToLoginActivity());

        // When Sign up Button is clicked, check user credentials for registration
        signUpBtn.setOnClickListener(v -> checkCredentials());
    }

    // Other methods and code...

    // Method to navigate to the login activity
    private void goToLoginActivity() {
        sessionManager.logoutSession();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to check the validity of entered email, password, and contact for registration
    private void checkCredentials() {
        String email = emailId.getText().toString();
        String password = this.password.getText().toString();
        String userName = fullName.getText().toString();
        String contactNumber = contact.getText().toString();

        if (!email.contains("@") || !email.contains(".")) {
            // Show an error if the email is not valid
            showError(emailId, "Email Address is not valid !");
        } else if (password.isEmpty() || password.length() < 7) {
            // Show an error if the password is not valid
            showError(this.password, "Password must be 7 characters or more!");
        } else if (userName.isEmpty()) {
            // Show an error if the User Name is empty
            showError(fullName, "Username cannot be left blank! Please enter.");
        } else if (contactNumber.length() != 10) {
            // Show an error if the contact is not valid
            showError(contact, "Contact must contain 10 digits!");
        } else {
            AppDatabase database = AppDatabase.getInstance(RegistrationActivity.this);
            UserTable ifExist = database.userDao().ifUserEmailIsTaken(email);

            if (ifExist == null) {
                // If credentials are valid, show a success toast and navigate to the login activity
                Toast.makeText(getApplicationContext(), "Register Successfully  ", Toast.LENGTH_SHORT).show();

                // Registration successful, navigate to the next activity
                database.userDao().insertUser(email, password, userName, contactNumber);
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                UserTable userTable = database.userDao().getUserByUserEmailAndPassword(email, password);
                String name = sessionManager.saveName(userTable.getFullName());
                String contact = sessionManager.saveContact(userTable.getContact());
                sessionManager.createSession(name, email, contact);
                goToLoginActivity();
            } else {
                // User already exists, show an error message
                Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to show an error message for an input field
    private void showError(EditText input, String errorMessage) {
        input.setError(errorMessage);
        input.requestFocus();
    }
}
