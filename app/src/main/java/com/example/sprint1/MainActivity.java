package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpText;
    private final int SPLASH_DISPLAY_LENGTH = 2700; // Duration of wait (2.7 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the ActionBar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Manage full-screen for API level 30 (Android R) and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars()); // Hide the status bar
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                // This will allow the system bars to temporarily show when the user swipes from the edge of the screen.
            }
        } else {
            // Deprecated method for full-screen, used in API level < 30
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Delay a Runnable post for the splash screen duration
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, set the content to the view with login and sign-up options
                // Note: This assumes that your login and sign-up buttons are in the activity_main layout
                // If they are in a different layout, use setContentView with that layout file instead

                // Set click listeners for the login button and sign-up TextView
                loginButton = findViewById(R.id.button4);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Navigate to the LoginActivity
                        Intent loginIntent = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(loginIntent);
                    }
                });

                signUpText = findViewById(R.id.textView3);
                signUpText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Navigate to the signupActivity
                        Intent signUpIntent = new Intent(MainActivity.this, signupActivity.class);
                        startActivity(signUpIntent);
                    }
                });
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
