package com.gcode.notes.extras.utils.callbacks;

public interface LocationUtilsCallbacks {
    void onError();

    void onProviderDisabled();

    void onLocationObtained(double latitude, double longitude);

    void onPermissionMissing();
}
