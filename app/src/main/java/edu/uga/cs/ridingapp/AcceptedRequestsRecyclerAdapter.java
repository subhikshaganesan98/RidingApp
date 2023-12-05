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

public class AcceptedRequestsRecyclerAdapter extends RecyclerView.Adapter<AcceptedRequestsRecyclerAdapter.AcceptedRequestHolder> {

    public static final String DEBUG_TAG = "AcceptedRequestsRecyclerAdapter";

    private List<AcceptedRequest> acceptedRequestList;
    private Context context;

    public AcceptedRequestsRecyclerAdapter(List<AcceptedRequest> acceptedRequestList, Context context) {
        this.acceptedRequestList = acceptedRequestList;
        this.context = context;
    }

    class AcceptedRequestHolder extends RecyclerView.ViewHolder {

        TextView driverName;
        TextView riderName;
        TextView date;
        TextView time;
        TextView pickup;
        TextView dropoff;
        TextView userPoints;

        Button confirmRequestButton;

        public AcceptedRequestHolder(View itemView) {
            super(itemView);

            riderName = itemView.findViewById(R.id.accepted_rideRequests_riderName_TextView);
            driverName = itemView.findViewById(R.id.accepted_rideRequests_driverName_TextView);
            date = itemView.findViewById(R.id.accepted_rideRequests_date_textView);
            time = itemView.findViewById(R.id.accepted_rideRequests_time_TextView);
            pickup = itemView.findViewById(R.id.accepted_rideRequests_pickup_textView);
            dropoff = itemView.findViewById(R.id.accepted_rideRequests_destination_TextView);
            userPoints = itemView.findViewById(R.id.accepted_rideRequests_userPoints_TextView);

            confirmRequestButton = itemView.findViewById(R.id.accepted_rideRequests_accept_button);
        }
    }

    @NonNull
    @Override
    public AcceptedRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_accepted_requests, parent, false);
        return new AcceptedRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptedRequestHolder holder, int position) {
        AcceptedRequest acceptedRequest = acceptedRequestList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + acceptedRequest);

        String driverName = acceptedRequest.getDriverName();
        String riderName = acceptedRequest.getRiderName();
        String date = acceptedRequest.getDate();
        String time = acceptedRequest.getTime();
        String pickup = acceptedRequest.getPickup();
        String dropoff = acceptedRequest.getDropoff();
        int userPoints = acceptedRequest.getUserPoints();

        holder.driverName.setText(driverName);
        holder.riderName.setText(riderName);
        holder.date.setText(date);
        holder.time.setText(time);
        holder.pickup.setText(pickup);
        holder.dropoff.setText(dropoff);
        holder.userPoints.setText(String.valueOf(userPoints));

        // Add a click listener for the confirm button
        holder.confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation logic here
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedRequestList.size();
    }
}
