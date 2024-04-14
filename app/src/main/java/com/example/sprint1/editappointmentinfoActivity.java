package com.example.sprint1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class editappointmentinfoActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Spinner spinnerTime;
    private Spinner spinnerDermatologist;
    private Button dateSelector;
    private Button buttonUpdate;
    private int reservationId;
    private int userId;
    private DBHelper dbHelper;
    private reservationModel reservation;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editappointmentinfo);

        spinnerTime = findViewById(R.id.timeDropDown);
        spinnerDermatologist = findViewById(R.id.dermatologists);
        dateSelector = findViewById(R.id.dateSelector);
        buttonUpdate = findViewById(R.id.buttonAdd);

        Intent intent = getIntent();
        if (intent != null) {
            reservationId = intent.getIntExtra("RESERVATION_ID", 0);
            userId = intent.getIntExtra("userId", 0);
        }

        dbHelper = new DBHelper(this);
        reservation = dbHelper.getReservationById(reservationId);

        if (reservation != null) {
            populateTimes();
            populateDermatologists();
            setListeners();
        }

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent2;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    // Start HomeActivity
                    intent2 = new Intent(editappointmentinfoActivity.this, HomeActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navRes) {
                    // Start AppointmentActivity
                    intent2 = new Intent(editappointmentinfoActivity.this, AppointmentActivity.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navprofile) {
                    // Start ProfileActivity
                    intent2 = new Intent(editappointmentinfoActivity.this, profile.class);
                    intent2.putExtra("userID", userId);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });
    }

    private void populateTimes() {
        String[] times = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
    }

    private void populateDermatologists() {
        String[] dermatologistsArray = {"Dr. Smith", "Dr. Johnson", "Dr. Williams", "Dr. Brown"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dermatologistsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDermatologist.setAdapter(adapter);
    }

    private void setListeners() {
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointment();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Toast.makeText(editappointmentinfoActivity.this, "Selected date: " + selectedDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateAppointment() {
        String selectedTime = spinnerTime.getSelectedItem().toString();
        String selectedDoctor = spinnerDermatologist.getSelectedItem().toString();

        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        reservation.set_reservation_time(selectedTime);
        reservation.set_reservation_doctor(selectedDoctor);
        reservation.set_reservation_date(selectedDate.getTime().toString());

        dbHelper.updateReservation(reservation); // Call the void method

        Toast.makeText(editappointmentinfoActivity.this, "Appointment updated successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(editappointmentinfoActivity.this, AppointmentActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
        finish();
    }
}