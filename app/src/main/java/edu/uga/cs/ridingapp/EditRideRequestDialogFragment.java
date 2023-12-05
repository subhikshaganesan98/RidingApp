package edu.uga.cs.ridingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditRideRequestDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // update an existing ride request
    public static final int DELETE = 2; // delete an existing ride request

    private EditText riderNameView;
    private EditText dateView;
    private EditText timeView;
    private EditText pickupView;
    private EditText dropoffView;

    int position;

    String key;
    String riderName;
    String date;
    String time;
    String pickup;
    String dropoff;

    public interface EditRideRequestDialogListener {
        void updateRideRequest(int position, RideRequest rideRequest, int action);
    }

    public static EditRideRequestDialogFragment newInstance(int position, String key, String riderName, String date, String time, String pickup, String dropoff) {
        EditRideRequestDialogFragment dialog = new EditRideRequestDialogFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putString("riderName", riderName);
        args.putString("date", date);
        args.putString("time", time);
        args.putString("pickup", pickup);
        args.putString("dropoff", dropoff);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        key = getArguments().getString("key");
        position = getArguments().getInt("position");
        riderName = getArguments().getString("riderName");
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        pickup = getArguments().getString("pickup");
        dropoff = getArguments().getString("dropoff");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_ride_offer_dialog, getActivity().findViewById(R.id.root));

        riderNameView = layout.findViewById(R.id.rideOfferDialog_editText1);
        dateView = layout.findViewById(R.id.rideOfferDialog_editText2);
        timeView = layout.findViewById(R.id.rideOfferDialog_editText3);
        pickupView = layout.findViewById(R.id.rideOfferDialog_editText4);
        dropoffView = layout.findViewById(R.id.rideOfferDialog_editText5);

        riderNameView.setText(riderName);
        dateView.setText(date);
        timeView.setText(time);
        pickupView.setText(pickup);
        dropoffView.setText(dropoff);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setTitle("Edit Ride Request");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("SAVE", new SaveButtonClickListener());

        builder.setNeutralButton("DELETE", new DeleteButtonClickListener());

        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String editedRiderName = riderNameView.getText().toString();
            String editedDate = dateView.getText().toString();
            String editedTime = timeView.getText().toString();
            String editedPickup = pickupView.getText().toString();
            String editedDropoff = dropoffView.getText().toString();

            RideRequest rideRequest = new RideRequest(editedRiderName, editedDate, editedTime, editedPickup, editedDropoff);
            rideRequest.setKey(key);
            Log.d("EditRideRequest", "ride request key: " + rideRequest.getKey());

            EditRideRequestDialogListener listener = (EditRideRequestDialogListener) getActivity();
            listener.updateRideRequest(position, rideRequest, SAVE);

            dialog.dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            RideRequest rideRequest = new RideRequest(riderName, date, time, pickup, dropoff);
            rideRequest.setKey(key);

            EditRideRequestDialogListener listener = (EditRideRequestDialogListener) getActivity();
            listener.updateRideRequest(position, rideRequest, DELETE);

            dialog.dismiss();
        }
    }
}
