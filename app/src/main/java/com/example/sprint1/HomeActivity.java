package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button bookNowButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bookNowButton = findViewById(R.id.bookNowBtn);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the "Book Reservation" page
                Intent intent = new Intent(HomeActivity.this, BookReservation.class);
                startActivity(intent);
            }
        });
    }
}