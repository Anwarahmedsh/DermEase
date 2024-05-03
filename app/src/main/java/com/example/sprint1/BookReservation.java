package com.example.sprint1;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookReservation extends AppCompatActivity {
    ImageButton returnHome;
    Button selectDateButton;
    Button addButton;
    Spinner selectTimeSpinner;
    Spinner selectDermatologist;
    Calendar selectedDate;
    DBHelper dbHelper;
    reservationModel reservation;
    String serviceType;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reservation);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userID",0);
        serviceType = intent.getStringExtra("service");

        dbHelper = new DBHelper(this);
        selectDateButton = findViewById(R.id.dateSelector);
        selectTimeSpinner = findViewById(R.id.timeSpinner);
        addButton = findViewById(R.id.buttonAdd);
        selectDermatologist = findViewById(R.id.dermatologistSpinner);
        returnHome= findViewById(R.id.home_btn);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(BookReservation.this, HomeActivity.class);
                intent.putExtra("userID", userId);
                startActivity(intent);
            }
        });


        setupTimeSpinner();
        setupDermatologistSpinner();

        // Set a click listener on the button to show the date picker dialog
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void setupTimeSpinner() {
        // Array of time options (9am to 6pm)
        String[] timeOptions = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timeOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        selectTimeSpinner.setAdapter(adapter);

        // Set a listener to handle item selections
        selectTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected time
                String selectedTime = (String) parentView.getItemAtPosition(position);
                Toast.makeText(BookReservation.this, "Selected time: " + selectedTime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void setupDermatologistSpinner() {
        // Array of dermatologist options
        String[] dermatologistOptions = {"Dr. Smith", "Dr. Johnson", "Dr. Williams", "Dr. Brown"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dermatologistOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        selectDermatologist.setAdapter(adapter);
    }

    // Method to show the date picker dialog
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

                        // You can use the selected date here as needed
                        // For example, you might display it in a TextView or use it to perform further processing
                        Toast.makeText(BookReservation.this, "Selected date: " + selectedDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

        datePickerDialog.show();

    }

    private void submitForm() {

        // Get the selected time
        String selectedTime = (String) selectTimeSpinner.getSelectedItem();
        String selectedDermatologist = (String) selectDermatologist.getSelectedItem();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        if (selectedDate == null) {
            Toast.makeText(BookReservation.this, "Please select a date,time and dermatologist", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedTime == null) {
            Toast.makeText(BookReservation.this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDermatologist == null) {
            Toast.makeText(BookReservation.this, "Please select a dermatologist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format the Calendar object into a string
        String dateString = dateFormat.format(selectedDate.getTime());

        //returns the reservation that has the same date time and dermatologist
        reservationModel temp= dbHelper.getReservationByDTD(dateString,selectedTime,selectedDermatologist);

        //it won't let the user reserve a reservation that had been already reserved
        if(temp!=null){
            Toast.makeText(BookReservation.this, "Sorry, this time isn't available with this dermatologist please select another one", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(BookReservation.this, "Appointment added:\nDate: " + dateString + "\nTime: " + selectedTime + "\nDermatologist: " + selectedDermatologist, Toast.LENGTH_LONG).show();
        reservation=new reservationModel(userId,NULL, dateString,selectedTime,selectedDermatologist,serviceType);
        if(!dbHelper.insertReservationData(reservation)){

            Toast.makeText(BookReservation.this, "reservation failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(BookReservation.this, AppointmentActivity.class);
        intent.putExtra("userID", userId);// Put the username as an extra
        startActivity(intent);
    }
}