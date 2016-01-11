package com.gcode.notes.activities.display;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.DisplayToolbarHelper;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.tasks.async.DecodeLocationTask;

import butterknife.Bind;

public class DisplayBaseActivity extends AppCompatActivity {
    //TODO: optimize and lower memory consumption
    public boolean mIsStarred;
    public boolean mNoteModeChanged;

    @Bind(R.id.display_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;
    @Bind(R.id.display_dates_text_view)
    TextView mDatesTextView;
    @Bind(R.id.display_reminder_text_view)
    TextView mReminderTextView;
    @Bind(R.id.display_location_button)
    Button mLocationButton;
    @Bind(R.id.display_action_image_button)
    ImageButton mActionImageButton;

    //getters for layout components----------------------------------------------------------------------------------------
    public TextView getDatesTextView() {
        return mDatesTextView;
    }

    public ImageButton getActionImageButton() {
        return mActionImageButton;
    }
    //getters for layout components----------------------------------------------------------------------------------------

    public void setStarredState() {
        mIsStarred = true;
        mActionImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    public void setNotStarredState() {
        mIsStarred = false;
        mActionImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }

    protected void setup() {
        DisplayToolbarHelper.setupToolbar(this, mToolbar); //setup activity's toolbar
    }

    public void displayBase(final ContentBase contentBase) {
        mTitleTextView.setText(contentBase.getTitle()); //display note's title
        mDatesTextView.setText(contentBase.getDateDetails()); //display Creation, Last modified and if has Expiration dates
        if (contentBase.hasReminder()) {
            //there is reminder, display it
            mReminderTextView.setVisibility(View.VISIBLE); //reminder text view is currently gone
            mReminderTextView.setText(getString(R.string.display_reminder_text_and_date,
                    DateUtils.formatDateTimeForDisplay(contentBase.getReminder())) //reminder is currently in SQLite format, format it for display
            );
        }
        if (contentBase.hasLocation()) {
            //there is location, display it
            mLocationButton.setVisibility(View.VISIBLE); //by default is gone
            new DecodeLocationTask(mLocationButton).execute(contentBase.getMyLocation());

            mLocationButton.setOnClickListener(new View.OnClickListener() {
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
