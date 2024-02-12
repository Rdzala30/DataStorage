package com.example.datastorage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, Password;
    TextView LoginText, ForgotPassWord, SignUpNow;
    Button LoginBtn;
    ImageView FacebookImg, GoogleImg, TweetImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailId = findViewById(R.id.emailId);
        Password = findViewById(R.id.Password);
        LoginText = findViewById(R.id.LoginText);
        ForgotPassWord = findViewById(R.id.ForgotPassWord);
        SignUpNow = findViewById(R.id.SignUpNow);
        LoginBtn = findViewById(R.id.LoginBtn);
        FacebookImg = findViewById(R.id.FacebookImg);
        GoogleImg = findViewById(R.id.GoogleImg);
        TweetImg = findViewById(R.id.TweetImg);

        // Check if the user is already logged in (session exists)
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        boolean isUserLoggedIn = sessionManager.checkSession();

        if (isUserLoggedIn) {
            // If the user is already logged in, go to the main activity
            goToMainActivity();
        } else {
            // Set up the UI and listeners when the user is not logged in

            // When Sign up Now button is clicked, navigate to the registration activity
            SignUpNow.setOnClickListener(v -> goToRegisterActivity());

            // When Forgot Password button is clicked, show a toast message about password recovery
            ForgotPassWord.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "We sent an Email with a link to get back into your account. ", Toast.LENGTH_SHORT).show());

            // When Login Button is clicked, check user credentials
            LoginBtn.setOnClickListener(v -> checkCredentials());

            // When Facebook, Google, or Twitter Image is clicked, show a toast message about an upcoming feature
            FacebookImg.setOnClickListener(v -> showUpcomingFeatureToast());
            GoogleImg.setOnClickListener(v -> showUpcomingFeatureToast());
            TweetImg.setOnClickListener(v -> showUpcomingFeatureToast());
        }
    }

    // Other methods...

    // Method to check the validity of entered email and password
    private void checkCredentials() {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());

        String email = emailId.getText().toString();
        String password = Password.getText().toString();

        // Check user credentials in the database
        UserTable userTable = database.userDao().getUserByUserEmailAndPassword(email, password);

        if (userTable != null) {
            // If credentials are valid, show a success toast and navigate to the main activity
            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

            // Save user information in the session
            SessionManager sessionManager = new SessionManager(getApplicationContext());
            sessionManager.createSession(userTable.getFullName(), email, userTable.getContact());

            goToMainActivity();
        } else {
            // If credentials are not valid, show a toast message
            Toast.makeText(getApplicationContext(), "Email Address or password is invalid! Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show a toast message about an upcoming feature
    private void showUpcomingFeatureToast() {
        Toast.makeText(getApplicationContext(), "Upcoming Feature", Toast.LENGTH_SHORT).show();
    }

    // Method to navigate to the main activity (Home Page)
    private void goToMainActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to navigate to the registration activity
    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
