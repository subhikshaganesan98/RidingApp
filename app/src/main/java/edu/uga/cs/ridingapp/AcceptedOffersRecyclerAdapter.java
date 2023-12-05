package edu.uga.cs.ridingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AcceptedOffersRecyclerAdapter extends RecyclerView.Adapter<AcceptedOffersRecyclerAdapter.AcceptedOfferHolder> {

    public static final String DEBUG_TAG = "AcceptedOffersRecyclerAdapter";

    private List<AcceptedOffer> acceptedOffersList;
    private Context context;

    public AcceptedOffersRecyclerAdapter(List<AcceptedOffer> acceptedOffersList, Context context) {
        this.acceptedOffersList = acceptedOffersList;
        this.context = context;
    }

    class AcceptedOfferHolder extends RecyclerView.ViewHolder {

        TextView driverName;
        TextView riderName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;
        TextView userPoints;

        Button confirmOfferButton;

        public AcceptedOfferHolder(View itemView) {
            super(itemView);

            riderName = itemView.findViewById(R.id.accepted_rideOffers_riderName_TextView);
            driverName = itemView.findViewById(R.id.accepted_rideOffers_driverName_TextView);
            date = itemView.findViewById(R.id.accepted_rideOffers_date_textView);
            time = itemView.findViewById(R.id.accepted_rideOffers_time_TextView);
            pickup = itemView.findViewById(R.id.accepted_rideOffers_pickup_textView);
            dropoff = itemView.findViewById(R.id.accepted_rideOffers_destination_TextView);
            userPoints = itemView.findViewById(R.id.accepted_rideOffers_userPoints_TextView);

            confirmOfferButton = itemView.findViewById(R.id.accepted_rideOffers_accept_button);
        }
    }

    @NonNull
    @Override
    public AcceptedOfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_accepted_offers, parent, false);
        return new AcceptedOfferHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptedOfferHolder holder, int position) {
        AcceptedOffer acceptedOffer = acceptedOffersList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + acceptedOffer);

        String driverName = acceptedOffer.getDriverName();
        String riderName = acceptedOffer.getRiderName();
        String date = acceptedOffer.getDate();
        String time = acceptedOffer.getTime();
        String pickup = acceptedOffer.getPickup();
        String dropoff = acceptedOffer.getDropoff();
        int userPoints = acceptedOffer.getUserPoints();

        holder.driverName.setText(driverName);
        holder.riderName.setText(riderName);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.pickup.setText(pickup);
        holder.dropoff.setText(dropoff);
        holder.userPoints.setText(String.valueOf(userPoints));

        // Add a click listener for the confirm button
        holder.confirmOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation logic here
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedOffersList.size();
    }
}
