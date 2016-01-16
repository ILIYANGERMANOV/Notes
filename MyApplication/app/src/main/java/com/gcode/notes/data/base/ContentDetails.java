package com.gcode.notes.data.base;

import com.gcode.notes.serialization.Serializer;

public class ContentDetails {
    private String creationDate;
    private String lastModifiedDate;
    private String expirationDate;

    MyLocation myLocation;

    public ContentDetails() {
        creationDate = null;
        lastModifiedDate = null;
        expirationDate = null;
        myLocation = null;
    }

    public ContentDetails(ContentDetails other) {
        creationDate = other.creationDate;
        lastModifiedDate = other.lastModifiedDate;
        expirationDate = other.expirationDate;
        if(other.myLocation != null) {
            myLocation = new MyLocation(other.myLocation);
        }
    }

    public ContentDetails(String creationDate, String lastModifiedDate,
                          String expirationDate, String myLocationSerialized) {
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.expirationDate = expirationDate;
        if (myLocationSerialized != null) {
            //there is myLocation set, parse it
            myLocation = Serializer.parseMyLocation(myLocationSerialized);
        }
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation location) {
        this.myLocation = location;
    }
}
