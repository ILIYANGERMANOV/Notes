package com.gcode.notes.motions;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.extras.values.Constants;

public class MyTransitionHelper {
    public static void startSharedElementTransitionForResult(Activity activity, View sharedElementView,
                                                             Intent intent, int requestCode) {

        // Get the transition name from the string
        String transitionName = activity.getString(R.string.main_to_display_transition_name);

        // Define the view that the animation will start from
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        sharedElementView,   // Starting view
                        transitionName    // The String
                );

        //Start the Intent
        ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle());
    }
}
