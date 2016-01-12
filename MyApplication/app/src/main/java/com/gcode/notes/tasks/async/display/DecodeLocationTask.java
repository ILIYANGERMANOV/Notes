package com.gcode.notes.tasks.async.display;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.gcode.notes.R;
import com.gcode.notes.data.base.MyLocation;
import com.gcode.notes.extras.utils.LocationUtils;
import com.gcode.notes.notes.MyApplication;

public class DecodeLocationTask extends AsyncTask<MyLocation, Void, String> {
    Button mLocationButton;

    public DecodeLocationTask(Button locationButton) {
        mLocationButton = locationButton;
    }

    @Override
    protected String doInBackground(MyLocation... params) {
        MyLocation myLocation = params[0];
        Context context = MyApplication.getAppContext();
        return context.getString(R.string.display_location_string,
                LocationUtils.getAddressFromLocation(context, myLocation.getLatitude(), myLocation.getLongitude()));
    }

    @Override
    protected void onPostExecute(String decodedLocation) {
        mLocationButton.setText(decodedLocation);
    }
}
