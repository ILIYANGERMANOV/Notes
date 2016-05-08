package com.gcode.notes.extras.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.callbacks.LocationUtilsCallbacks;
import com.gcode.notes.extras.values.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils implements LocationListener {
    //TODO: REFACTOR and optimize getAddressFromLocation()
    /**
     * flag for gps status
     */
    private boolean isGpsEnabled = false;

    /**
     * flag for network status
     */
    private boolean isNetworkEnabled = false;


    /**
     * location manager
     */
    private LocationManager mLocationManager;

    private LocationUtilsCallbacks mLocationUtilsCallbacks;

    public LocationUtils(Context context, LocationUtilsCallbacks locationUtilsCallbacks) {
        mLocationUtilsCallbacks = locationUtilsCallbacks;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static String getAddressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to be returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            MyDebugger.log("getAddressFromLocation() IOException", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            MyDebugger.log("getAddressFromLocation() IllegalArgument exception", e.getMessage());
            return null;
        }

        Address address = addresses.get(0); //reference for easier access

        String addressLine = address.getAddressLine(0); // If any additional address line present than only,
        //check with max available address lines by getMaxAddressLineIndex()
        String city = address.getLocality();
        String state = address.getAdminArea();
        String country = address.getCountryName();
        String postalCode = address.getPostalCode();
        String knownName = address.getFeatureName(); // Only if available else return NULL
//        MyDebugger.log("address", addressLine);
//        MyDebugger.log("city", city);
//        MyDebugger.log("state", state);
//        MyDebugger.log("country", country);
//        MyDebugger.log("postalCode", postalCode);
//        MyDebugger.log("knownName", knownName);
//        MyDebugger.log("admin area", address.getAdminArea());
//        MyDebugger.log("featured name", address.getFeatureName());
//        MyDebugger.log("subLocality", address.getSubLocality());
//        MyDebugger.log("premises", address.getPremises());

        //TODO: create smart location button text, not only from address (cuz when not in hometown is retarded)
        String result = "";
        if (addressLine != null) {
            result += addressLine;
        } else if (knownName != null && knownName.trim().length() > 0) {
            result += knownName;
        }
        if (city != null && result.length() < 25) {
            result += ", " + city;
        }
        if (result.length() < 25 && country != null) {
            result += ", " + country;
        }

        return result.trim();
    }

    public void getLocation() {
        if (mLocationManager == null) {
                /* mLocationManager is null, log it and call onError */
            MyDebugger.log("mLocationManger is null");
            mLocationUtilsCallbacks.onError();
            return;
        }

        try {
            /* getting status of the gps */
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            /* getting status of network provider */
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (IllegalArgumentException e) {
            /* internet or gps provider is null, log exception and return */
            MyDebugger.log("getLocation() provider is null exception", e.getMessage());
            mLocationUtilsCallbacks.onError();
            return;
        }

        try {
            Location lastKnownLocation;
            if (isGpsEnabled) {
                //gps is enabled, its last known location
                lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (isNetworkEnabled) {
                //network is enabled, get lat known from it
                lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                /* no location provider enabled, callback and return */
                mLocationUtilsCallbacks.onProviderDisabled();
                return;
            }

            if (lastKnownLocation != null && lastKnownLocation.getTime() <
                    DateUtils.getCurrentTimeAsMillis() - Constants.LOCATION_VALID_TIME) {
                //!NOTE: mLocationManager#getLastKnownLocation() is most cases returns null (documentation is wrong)
                //last known location is valid, use it
                MyDebugger.log("hurray using last known location");
                mLocationUtilsCallbacks.onLocationObtained(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                return;
            }

            obtainCurrentLocation();
        } catch (SecurityException e) {
            /* there is no suitable permission */
            mLocationUtilsCallbacks.onPermissionMissing();
        } catch (IllegalArgumentException e) {
            MyDebugger.log("getLocation() IllegalArgumentException", e.getMessage());
            mLocationUtilsCallbacks.onError();
        }
    }

    private void obtainCurrentLocation() throws SecurityException, IllegalArgumentException {
        Criteria criteria = new Criteria();
        if (isGpsEnabled) {
            /* if gps is enabled then get location using gps */
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            mLocationManager.requestSingleUpdate(criteria, this, null);
        } else if (isNetworkEnabled) {
            /* gps is not enabled, getting location from network provider */
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            mLocationManager.requestSingleUpdate(criteria, this, null);
        } else {
            /* no location provider enabled, callback */
            mLocationUtilsCallbacks.onProviderDisabled();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //there is no case in which location is null in this callback
        mLocationUtilsCallbacks.onLocationObtained(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        MyDebugger.log("provider enabled", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        MyDebugger.log("provider disabled", provider);
    }
}
