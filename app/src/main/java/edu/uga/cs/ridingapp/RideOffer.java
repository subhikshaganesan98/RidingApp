package edu.uga.cs.ridingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RideOffer {
    private String key;
    private String driverName;
    private String time;
    private String date;
    private String pickup;
    private String dropoff;

    private boolean accepted;

    public RideOffer() {
        this.driverName = null;
        this.time = null;
        this.date = null;
        this.pickup = null;
        this.dropoff = null;
        this.accepted = false;
    }

    public RideOffer(String driverName, String time, String date, String pickup, String dropoff) {
        this.driverName = driverName;
        this.time = time;
        this.date = date;
        this.pickup = pickup;
        this.dropoff = dropoff;

    }

    // Getter methods
    public String getKey() {
        return key;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDropoff() {
        return dropoff;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    // Setter methods
    public void setKey(String key) {
        this.key = key;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public void setAccepted(boolean accepted){
        this.accepted = accepted;
    }
}

