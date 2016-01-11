package com.gcode.notes.data.base;

public class MyLocation {
    private boolean isSet;
    private double latitude;
    private double longitude;

    public MyLocation() {
        isSet = false;
    }

    public MyLocation(double latitude, double longitude) {
        isSet = true;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isSet() {
        return isSet;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        isSet = true;
    }
}
