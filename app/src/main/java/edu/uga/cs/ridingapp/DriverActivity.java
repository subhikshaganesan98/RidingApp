package edu.uga.cs.ridingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverActivity extends AppCompatActivity {

    private Button postRideRequest;
    private Button viewRideRequest;
    private Button viewAcceptedRides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        viewRideRequest = findViewById(R.id.button8);
        postRideRequest = findViewById(R.id.button9);
        viewAcceptedRides = findViewById(R.id.button10);

        viewRideRequest.setOnClickListener(new ViewRideRequestButtonClickListener());
        postRideRequest.setOnClickListener(new PostRideRequestButtonClickListener());
        viewAcceptedRides.setOnClickListener(new ViewAcceptedRidesButtonClickListener());

    }

    private class PostRideRequestButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), NewRideOffer.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ViewRideRequestButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), ReviewRideRequestsActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ViewAcceptedRidesButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), NewRideOffer.class);
            view.getContext().startActivity(intent);
        }
    }


}