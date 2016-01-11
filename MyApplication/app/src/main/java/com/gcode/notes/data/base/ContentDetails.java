package com.gcode.notes.data.base;

import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ContentDetails {
    private String creationDate;
    private String lastModifiedDate;
    private String expirationDate;

    MyLocation location;

    public ContentDetails() {
        creationDate = Constants.NO_DATE;
        lastModifiedDate = Constants.NO_DATE;
        expirationDate = Constants.NO_DATE;
        location = new MyLocation();
    }

    public ContentDetails(String creationDate, String lastModifiedDate,
                          String expirationDate, String myLocationSerialized) {
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.expirationDate = expirationDate;
        if (myLocationSerialized.equals(Constants.NO_LOCATION)) {
            //there is no location set, construct new one
            location = new MyLocation();
        } else {
            //there is location set, parse it
            location = Serializer.parseMyLocation(myLocationSerialized);
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
        return location;
    }
}
