package com.gcode.notes.data.note.base;

import com.gcode.notes.extras.values.Constants;

public class ContentDetails {
    //TODO: add location
    String creationDate;
    String lastModifiedDate;
    String expirationDate;

    public ContentDetails() {
        this.creationDate = Constants.NO_DATE;
        this.lastModifiedDate = Constants.NO_DATE;
        this.expirationDate = Constants.NO_DATE;
    }

    public ContentDetails(String creationDate, String lastModifiedDate, String expirationDate) {
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.expirationDate = expirationDate;
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

}
