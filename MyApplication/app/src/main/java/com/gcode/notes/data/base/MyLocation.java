package com.gcode.notes.data.base;

public class MyLocation {
    private double latitude;
    private double longitude;

    public MyLocation(MyLocation other) {
        latitude = other.latitude;
        longitude = other.longitude;
    }

    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
    }
}
