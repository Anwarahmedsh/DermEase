package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpText;
    private final int SPLASH_DISPLAY_LENGTH = 2700; // Duration of wait

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize the login button and sign-up text and set their click listeners
        // Make sure to add these after the splash screen has finished
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the splash screen, set the content and interaction
                setContentView(R.layout.activity_main); // Use the layout with your login and sign-up options

                loginButton = findViewById(R.id.button4);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Intent to start the LoginActivity
                        Intent loginIntent = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(loginIntent);
                    }
                });

                signUpText = findViewById(R.id.textView3);
                signUpText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Intent to start the signupActivity
                        Intent signUpIntent = new Intent(MainActivity.this, signupActivity.class);
                        startActivity(signUpIntent);
                    }
                });
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
