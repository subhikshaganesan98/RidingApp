package edu.uga.cs.ridingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RideOffers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offers);

        TextView date_textView = findViewById(R.id.date_textView);
        date_textView.setText("Hello");
    }
}