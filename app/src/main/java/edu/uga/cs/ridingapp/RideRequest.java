package edu.uga.cs.ridingapp;

public class RideRequest {
    private String key;
    private String riderName;
    private String time;
    private String date;
    private String pickup;
    private String dropoff;
    private boolean accepted;

    public RideRequest() {
        this.riderName = null;
        this.time = null;
        this.date = null;
        this.pickup = null;
        this.dropoff = null;
        this.accepted = false;
    }

    public RideRequest(String riderName, String time, String date, String pickup, String dropoff) {
        this.riderName = riderName;
        this.time = time;
        this.date = date;
        this.pickup = pickup;
        this.dropoff = dropoff;
    }

    public String getKey() {
        return key;
    }

    public String getRiderName() {
        return riderName;
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

    // setter methods
    public void setKey(String key) {
        this.key = key;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
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

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
