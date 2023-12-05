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

public class RideOfferRecyclerAdapter extends RecyclerView.Adapter<RideOfferRecyclerAdapter.RideOfferHolder> {

    public static final String DEBUG_TAG = "RideOfferRecyclerAdapter";

    private List<RideOffer> rideOfferList;
    private Context context;

    public RideOfferRecyclerAdapter(List<RideOffer> rideOfferList, Context context) {
        this.rideOfferList = rideOfferList;
        this.context = context;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class RideOfferHolder extends RecyclerView.ViewHolder {

        TextView driverName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;

        public RideOfferHolder(View itemView) {
            super(itemView);

            driverName = itemView.findViewById(R.id.driverName_TextView);
            date = itemView.findViewById(R.id.date_textView);
            time = itemView.findViewById(R.id.time_TextView);
            pickup = itemView.findViewById(R.id.pickup_textView);
            dropoff = itemView.findViewById(R.id.destination_TextView);
        }
    }

    @NonNull
    @Override
    public RideOfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ride_offers, parent, false);
        return new RideOfferHolder(view);
    }

    // This method fills in the values of the Views to show a RideOffer
    @Override
    public void onBindViewHolder(RideOfferHolder holder, int position) {
        RideOffer rideOffer = rideOfferList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + rideOffer);

        String driverName = rideOffer.getDriverName();
        String date = rideOffer.getDate();
        String time = rideOffer.getTime();
        String pickup = rideOffer.getPickup();
        String dropoff = rideOffer.getDropoff();

        holder.driverName.setText(driverName);
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
                EditRideOfferDialogFragment editRideOfferFragment =
                        EditRideOfferDialogFragment.newInstance(holder.getAdapterPosition(), driverName, date, time, pickup, dropoff);
                editRideOfferFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rideOfferList.size();
    }
}
