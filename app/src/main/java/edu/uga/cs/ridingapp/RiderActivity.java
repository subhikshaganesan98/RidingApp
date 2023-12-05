package edu.uga.cs.ridingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RiderActivity extends AppCompatActivity {

    private Button viewRideOffers;
    private Button postRideRequests;
    private Button viewAcceptedRides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        viewRideOffers = findViewById(R.id.button5);
        postRideRequests = findViewById(R.id.button6);
        viewAcceptedRides = findViewById(R.id.button7);

        viewRideOffers.setOnClickListener(new ViewRideOffersButtonClickListener());
        postRideRequests.setOnClickListener(new PostRideRequestButtonClickListener());
        viewAcceptedRides.setOnClickListener(new ViewAcceptedRidesButtonClickListener());
    }

    private class ViewRideOffersButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewRideOffersActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class PostRideRequestButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), NewRideRequest.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ViewAcceptedRidesButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), ReviewAcceptedRequestsActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}