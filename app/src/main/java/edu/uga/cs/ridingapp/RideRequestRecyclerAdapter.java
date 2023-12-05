package edu.uga.cs.ridingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RideRequestRecyclerAdapter extends RecyclerView.Adapter<RideRequestRecyclerAdapter.RideRequestHolder> {

    public static final String DEBUG_TAG = "RideRequestRecyclerAdapter";

    private List<RideRequest> rideRequestList;
    private Context context;

    public RideRequestRecyclerAdapter(List<RideRequest> rideRequestList, Context context) {
        this.rideRequestList = rideRequestList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class RideRequestHolder extends RecyclerView.ViewHolder {

        TextView riderName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;

        public RideRequestHolder(View itemView) {
            super(itemView);

            riderName = itemView.findViewById(R.id.driverName_TextView);
            date = itemView.findViewById(R.id.date_textView);
            time = itemView.findViewById(R.id.time_TextView);
            pickup = itemView.findViewById(R.id.pickup_textView);
            dropoff = itemView.findViewById(R.id.destination_TextView);
        }
    }

    @NonNull
    @Override
    public RideRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ride_offers, parent, false);
        return new RideRequestHolder(view);
    }

    // This method fills in the values of the Views to show a RideOffer
    @Override
    public void onBindViewHolder(RideRequestHolder holder, int position) {
        RideRequest rideRequest = rideRequestList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + rideRequest);

        String riderName = rideRequest.getRiderName();
        String date = rideRequest.getDate();
        String time = rideRequest.getTime();
        String pickup = rideRequest.getPickup();
        String dropoff = rideRequest.getDropoff();

        holder.riderName.setText(riderName);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.pickup.setText(pickup);
        holder.dropoff.setText(dropoff);

        // We can attach an OnClickListener to the itemView of the holder;
        // itemView is a public field in the Holder class.
        // It will be called when the user taps/clicks on the whole item, i.e., one of
        // the ride offers shown.
        // This will indicate that the user wishes to edit (modify or delete) this item.
        // We create and show an EditRideOfferDialogFragment.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRideRequestDialogFragment editRideRequestDialogFragment =
                        EditRideRequestDialogFragment.newInstance(holder.getAdapterPosition(), riderName, date, time, pickup, dropoff);
                editRideRequestDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rideRequestList.size();
    }
}
