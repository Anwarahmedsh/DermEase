package com.example.sprint1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.navHome);

        // Retrieve user ID from intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userID", 0); // 0 is the default value if "userID" is not found

        // Now you have the userID, you can use it to fetch user information from the database and display it
        DBHelper dbHelper = new DBHelper(this);
        userModel user = dbHelper.getUserById(userId);

        if (user != null) {
            // Assuming you have TextViews in your profile.xml layout for displaying user information
            TextView usernameTextView = findViewById(R.id.name);
            TextView emailTextView = findViewById(R.id.email);
            TextView phoneTextView = findViewById(R.id.phone);

            // Set user information to TextViews
            usernameTextView.setText(user.get_user_name());
            emailTextView.setText(user.get_user_email());
            phoneTextView.setText(user.get_user_phone());
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent2;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    // Start HomeActivity
                    intent2 = new Intent(profile.this, HomeActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navRes) {
                    // Start AppointmentActivity
                    intent2 = new Intent(profile.this, AppointmentActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navprofile) {
                    // Start ProfileActivity
                    intent2 = new Intent(profile.this, profile.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });
    }
}

