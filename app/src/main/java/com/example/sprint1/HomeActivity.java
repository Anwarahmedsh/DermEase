package com.example.sprint1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    Button bookNowButton1;
    Button bookNowButton2;
    TextView service1;
    TextView service2;

    private BottomNavigationView bottomNavigationView;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bookNowButton1 = findViewById(R.id.bookNowBtn);
        bookNowButton2 = findViewById(R.id.bookNowBtn2);
        service1=findViewById(R.id.TextService1);
        service2=findViewById(R.id.TextService2);
        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.navHome);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userID",0);


        bookNowButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the "Book Reservation" page
                Intent intent = new Intent(HomeActivity.this, BookReservation.class);
                intent.putExtra("userID", userId);// Put the username as an extra
                intent.putExtra("service", service1.getText().toString());
                startActivity(intent);
            }
        });

        bookNowButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the "Book Reservation" page
                Intent intent = new Intent(HomeActivity.this, BookReservation.class);
                intent.putExtra("userID", userId);// Put the username as an extra
                intent.putExtra("service", service2.getText().toString());
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent2;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    // Start HomeActivity
                    intent2 = new Intent(HomeActivity.this, HomeActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navRes) {
                    // Start AppointmentActivity
                    intent2 = new Intent(HomeActivity.this, AppointmentActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navprofile) {
                    // Start ProfileActivity
                    intent2 = new Intent(HomeActivity.this, profile.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });

    }
}