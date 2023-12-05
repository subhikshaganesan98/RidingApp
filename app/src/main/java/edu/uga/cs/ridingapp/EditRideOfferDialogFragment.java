package edu.uga.cs.ridingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditRideOfferDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // update an existing ride offer
    public static final int DELETE = 2; // delete an existing ride offer

    private EditText driverNameView;
    private EditText dateView;
    private EditText timeView;
    private EditText pickupView;
    private EditText dropoffView;

    int position;

    String key;
    String driverName;
    String date;
    String time;
    String pickup;
    String dropoff;

    public interface EditRideOfferDialogListener {
        void updateRideOffer(int position, RideOffer rideOffer, int action);
    }

    public static EditRideOfferDialogFragment newInstance(int position, String driverName, String date, String time, String pickup, String dropoff) {
        EditRideOfferDialogFragment dialog = new EditRideOfferDialogFragment();

        //
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("driverName", driverName);
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

        position = getArguments().getInt("position");
        driverName = getArguments().getString("driverName");
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        pickup = getArguments().getString("pickup");
        dropoff = getArguments().getString("dropoff");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_ride_offer_dialog, getActivity().findViewById(R.id.root));

        driverNameView = layout.findViewById(R.id.rideOfferDialog_editText1);
        dateView = layout.findViewById(R.id.rideOfferDialog_editText2);
        timeView = layout.findViewById(R.id.rideOfferDialog_editText3);
        pickupView = layout.findViewById(R.id.rideOfferDialog_editText4);
        dropoffView = layout.findViewById(R.id.rideOfferDialog_editText5);

        driverNameView.setText(driverName);
        dateView.setText(date);
        timeView.setText(time);
        pickupView.setText(pickup);
        dropoffView.setText(dropoff);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setTitle("Edit Ride Offer");

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
            String editedDriverName = driverNameView.getText().toString();
            String editedDate = dateView.getText().toString();
            String editedTime = timeView.getText().toString();
            String editedPickup = pickupView.getText().toString();
            String editedDropoff = dropoffView.getText().toString();

            RideOffer rideOffer = new RideOffer(editedDriverName, editedDate, editedTime, editedPickup, editedDropoff);

            EditRideOfferDialogListener listener = (EditRideOfferDialogListener) getActivity();
            listener.updateRideOffer(position, rideOffer, SAVE);

            dialog.dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            RideOffer rideOffer = new RideOffer(driverName, date, time, pickup, dropoff);
            rideOffer.setKey(key);

            EditRideOfferDialogListener listener = (EditRideOfferDialogListener) getActivity();
            listener.updateRideOffer(position, rideOffer, DELETE);

            dialog.dismiss();
        }
    }
}
