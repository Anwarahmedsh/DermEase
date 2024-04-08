package com.example.sprint1;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signupActivity extends AppCompatActivity {

        private EditText usernameEditText, phoneNumEditText, passwordEditText, emailEditText;
        private Button signUpButton;
        private DBHelper dbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            usernameEditText = findViewById(R.id.usernamesignup);
            phoneNumEditText = findViewById(R.id.phonenumsignup);
            passwordEditText = findViewById(R.id.passwordsignup);
            emailEditText = findViewById(R.id.EmaileditTextText);
            signUpButton = findViewById(R.id.signupbutton);

            dbHelper = new DBHelper(this);

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUpUser();
                }
            });
        }

        private void signUpUser() {
            String username = usernameEditText.getText().toString().trim();
            String phoneNumber = phoneNumEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(signupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUsername(username)) {
                Toast.makeText(signupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Since userID is auto-incremented, we can pass any integer value here, it will be ignored by SQLite
            userModel newUser = new userModel(NULL, username, email, phoneNumber, password);

            boolean isInserted = dbHelper.insertData(newUser);
            if (isInserted) {
                Toast.makeText(signupActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                // Redirect user to login page or main activity
            } else {
                Toast.makeText(signupActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
