package com.gcode.notes.activities.display;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.DisplayToolbarHelper;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.display.DecodeLocationTask;

import butterknife.Bind;

public class DisplayBaseActivity extends AppCompatActivity {
    //TODO: Refactor, optimize and lower memory consumption
    public boolean mIsStarred;
    public boolean mNoteModeChanged;

    @Bind(R.id.display_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;
    @Bind(R.id.display_reminder_text_view)
    TextView mReminderTextView;
    @Bind(R.id.display_last_modified_text_view)
    TextView mLastModifiedTextView;
    @Bind(R.id.display_created_on_text_view)
    TextView mCreatedOnTextView;
    @Bind(R.id.display_expires_on_text_view)
    TextView mExpiresOnTextView;
    @Bind(R.id.display_location_layout)
    LinearLayout mLocationLayout;
    @Bind(R.id.display_location_text_view)
    TextView mLocationTextView;
    @Bind(R.id.display_action_image_button)
    ImageButton mActionImageButton;

    //getters for layout components----------------------------------------------------------------------------------------
    public View getLimitView() {
        return mLocationLayout;
    }

    public ImageButton getActionImageButton() {
        return mActionImageButton;
    }
    //getters for layout components----------------------------------------------------------------------------------------

    public void setStarredState() {
        mIsStarred = true;
        mActionImageButton.setImageResource(R.drawable.ic_star_shine);
    }

    public void setNotStarredState() {
        mIsStarred = false;
        mActionImageButton.setImageResource(R.drawable.ic_star);
    }

    protected void setup() {
        DisplayToolbarHelper.setupToolbar(this, mToolbar); //setup activity's toolbar
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (requestCode == Constants.COMPOSE_NOTE_REQUEST_CODE) {
            overridePendingTransition(R.anim.slide_in_right, 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    public void displayBase(final ContentBase contentBase) {
        mTitleTextView.setText(contentBase.getTitle()); //display note's title
        contentBase.displayDetails(mReminderTextView, mLastModifiedTextView,
                mCreatedOnTextView, mExpiresOnTextView);
        if (contentBase.hasLocation()) {
            //there is location, display it
            mLocationLayout.setVisibility(View.VISIBLE); //by default is gone
            new DecodeLocationTask(mLocationTextView).execute(contentBase.getMyLocation());

            mLocationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double latitude = contentBase.getLatitude();
                    double longitude = contentBase.getLongitude();
                    String label = getString(R.string.display_map_intent_label);
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }
}
