package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class loginActivity extends AppCompatActivity {
    // Declare your EditText and Button variables
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize your views and DBHelper
        usernameEditText = findViewById(R.id.usernamelogin);
        passwordEditText = findViewById(R.id.passwordlogin);
        loginButton = findViewById(R.id.loginbutton);
        dbHelper = new DBHelper(this);

        // Set an OnClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(loginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check the credentials with the database
        if (dbHelper.checkUsernamePassword(username, password)) {
            // Credentials are correct
            Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            // Proceed to another activity or do further processing
        } else {
            // Credentials are incorrect
            Toast.makeText(loginActivity.this, "Login Failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
    }
}