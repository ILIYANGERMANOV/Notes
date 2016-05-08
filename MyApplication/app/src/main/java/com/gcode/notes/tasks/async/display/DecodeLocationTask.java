package com.gcode.notes.tasks.async.display;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.gcode.notes.data.base.MyLocation;
import com.gcode.notes.extras.utils.LocationUtils;
import com.gcode.notes.notes.MyApplication;

public class DecodeLocationTask extends AsyncTask<MyLocation, Void, String> {
    TextView mLocationTextView;

    public DecodeLocationTask(TextView locationTextView) {
        mLocationTextView = locationTextView;
    }

    @Override
    protected String doInBackground(MyLocation... params) {
        MyLocation myLocation = params[0];
        Context context = MyApplication.getAppContext();
        return LocationUtils.getAddressFromLocation(context, myLocation.getLatitude(), myLocation.getLongitude());
    }

    @Override
    protected void onPostExecute(String decodedLocation) {
        mLocationTextView.setText(decodedLocation);
    }
}
