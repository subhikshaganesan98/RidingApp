package edu.uga.cs.ridingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RideOfferRecyclerAdapter extends RecyclerView.Adapter<RideOfferRecyclerAdapter.RideOfferHolder> {

    public static final String DEBUG_TAG = "RideOfferRecyclerAdapter";

    private List<RideOffer> rideOfferList;
    private Context context;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

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
        Button acceptButton;

        public RideOfferHolder(View itemView) {
            super(itemView);

            driverName = itemView.findViewById(R.id.driverName_TextView);
            date = itemView.findViewById(R.id.date_textView);
            time = itemView.findViewById(R.id.time_TextView);
            pickup = itemView.findViewById(R.id.pickup_textView);
            dropoff = itemView.findViewById(R.id.destination_TextView);

            acceptButton = itemView.findViewById(R.id.accept_button);
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

        String key = rideOffer.getKey();
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

        holder.acceptButton.setOnClickListener(new AcceptButtonClickListener(position));

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
                        EditRideOfferDialogFragment.newInstance(holder.getAdapterPosition(), key, driverName, date, time, pickup, dropoff);
                editRideOfferFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
            }
        });
    }

    private class AcceptButtonClickListener implements View.OnClickListener {
        int position; // retrieves ride offer from list
        public AcceptButtonClickListener (int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            RideOffer rideOffer = rideOfferList.get(position);

            database = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            Log.d(DEBUG_TAG, "User Rider Email is: " + user.getEmail());
            //create a new Accepted Ride from rideOffer values
            AcceptedOffer acceptedRide = new AcceptedOffer(rideOffer.getDriverName(), user.getEmail(), rideOffer.getDate(), rideOffer.getTime(), rideOffer.getPickup(),
                    rideOffer.getDropoff(), 50, false);

            // Add a new element (AcceptedRides) to the list of ride offers in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("AcceptedOffers");

            myRef.push().setValue(acceptedRide)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            Log.d("Accepted Ride Offer created for ", acceptedRide.getDriverName());

                            // Log success
                            Log.d("RideOfferRecylcerAdapter", "Accepted Ride offer created successfully");


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Log failure
                            Log.d("RideOfferRecylcerAdapter", "Failed to create a accepted ride offer");
                        }
                    });

            // delete Unaccpeted RideOffer from the RideOffers List


            Log.d(DEBUG_TAG, "Deleting Unaccpeted ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");

            // remove the deleted ride offer from the list (internal list in the App)
            rideOfferList.remove(position);

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride offer after ArrayList at: " + position + "(" + rideOffer.getDriverName() + ")");

            // Update the recycler view to remove the deleted ride offer from that view
            notifyItemRemoved(position);

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride offer after RecyclerAdapter at: " + position + "(" + rideOffer.getDriverName() + ")");

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride offer key: " + rideOffer.getKey());

            // Delete this ride offer in Firebase.
            // Note that we are using a specific key (one child in the list)
            DatabaseReference ref = database
                    .getReference()
                    .child("RideOffers")
                    .child(rideOffer.getKey());

            Log.d(DEBUG_TAG, "Deleted Unaccpeted ride offer after Database Ref at: " + position + "(" + rideOffer.getDriverName() + ")");

            // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
            // to maintain ride offers.
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "deleted unaccepted ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to delete ride offer at: " + position + "(" + rideOffer.getDriverName() + ")");

                }
            });

        }
    }



    @Override
    public int getItemCount() {
        return rideOfferList.size();
    }
}
