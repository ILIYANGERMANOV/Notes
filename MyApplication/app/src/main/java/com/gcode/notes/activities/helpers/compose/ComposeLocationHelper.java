package com.gcode.notes.activities.helpers.compose;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.utils.LocationUtils;
import com.gcode.notes.extras.utils.callbacks.LocationUtilsCallbacks;

public class ComposeLocationHelper {
    public static void getLocation(final ComposeBaseActivity composeBaseActivity) {
        new LocationUtils(composeBaseActivity, new LocationUtilsCallbacks() {
            @Override
            public void onError() {
                MyLogger.log("Location utils error!");
                composeBaseActivity.mLocationObtained = false;
            }

            @Override
            public void onProviderDisabled() {
                //MyLogger.log("No provider enabled");
                composeBaseActivity.mLocationObtained = false;
            }

            @Override
            public void onLocationObtained(double latitude, double longitude) {
                composeBaseActivity.mLocationObtained = true;
                composeBaseActivity.mLatitude = latitude;
                composeBaseActivity.mLongitude = longitude;
            }

            @Override
            public void onPermissionMissing() {
                MyLogger.log("Location permission missing");
                composeBaseActivity.mLocationObtained = false;
            }
        }).getLocation();
    }
}
