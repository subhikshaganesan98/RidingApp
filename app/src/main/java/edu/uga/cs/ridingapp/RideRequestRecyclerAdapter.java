//package edu.uga.cs.ridingapp;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class RideRequestRecyclerAdapter extends RecyclerView.Adapter<RideRequestRecyclerAdapter.RideRequestHolder> {
//
//    public static final String DEBUG_TAG = "RideRequestRecyclerAdapter";
//
//    private List<RideRequest> rideRequestList;
//    private Context context;
//
//    public RideRequestRecyclerAdapter(List<RideRequest> rideRequestList, Context context) {
//        this.rideRequestList = rideRequestList;
//        this.context = context;
//    }
//
//    class RideRequestHolder extends RecyclerView.ViewHolder {
//
//        TextView riderName;
//        TextView date;
//        TextView time;
//        TextView pickup;
//        TextView dropoff;
//
//        public RideRequestHolder(View itemView) {
//            super(itemView);
//
//            riderName = itemView.findViewById(R.id.editText1);
//            date = itemView.findViewById(R.id.date);
//            time = itemView.findViewById(R.id.time);
//            pickup = itemView.findViewById(R.id.pickup);
//            dropoff = itemView.findViewById(R.id.dropoff);
//        }
//    }
//
//    @NonNull
//    @Override
//    public RideRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_request, parent, false);
//        return new RideRequestHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RideRequestHolder holder, int position) {
//        RideRequest rideRequest = rideRequestList.get(position);
//
//        Log.d(DEBUG_TAG, "onBindViewHolder: " + rideRequest);
//
//        String key = rideRequest.getKey();
//        String riderName = rideRequest.getRiderName();
//        String date = rideRequest.getDate();
//        String time = rideRequest.getTime();
//        String pickup = rideRequest.getPickup();
//        String dropoff = rideRequest.getDropoff();
//
//        holder.riderName.setText(riderName);
//        holder.date.setText(date);
//        holder.time.setText(time);
//        holder.pickup.setText(pickup);
//        holder.dropoff.setText(dropoff);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditRideRequestDialogFragment editRideRequestFragment =
//                        EditRideRequestDialogFragment.newInstance(holder.getAdapterPosition(), key, riderName, date, time, pickup, dropoff);
//                editRideRequestFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return rideRequestList.size();
//    }
//}
