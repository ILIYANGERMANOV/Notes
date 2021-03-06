package com.gcode.notes.activities.compose;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.compose.ComposeBaseToolbarHelper;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.fragments.ComposeReminderFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class ComposeBaseActivity extends AppCompatActivity {
    public boolean mInPrivateMode;
    public boolean mIsOpenedInEditMode;
    public boolean mIsStarred;
    public boolean mNoteModeChanged;
    public Intent mResultIntent = new Intent();

    //getters for layout components---------------------------------------------------------------------------------------
    public boolean mLocationObtained;
    public double mLatitude;
    public double mLongitude;
    @Bind(R.id.compose_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.compose_title_edit_text)
    EditText mTitleEditText;
    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    //getters for layout components---------------------------------------------------------------------------------------
    public EditText getTitleEditText() {
        return mTitleEditText;
    }

    public ImageButton getStarImageButton() {
        return mStarImageButton;
    }

    public ComposeReminderFragment getComposeReminderFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.compose_note_reminder_fragment);
        if (fragment != null && fragment instanceof ComposeReminderFragment) {
            //fragment is not null and can be cast to ComposeReminderFragment so return it
            return ((ComposeReminderFragment) fragment);
        } else {
            //fragment is null or cannot be cast, log and return null
            MyDebugger.log("getComposeReminderFragment(): fragment null or cannot be cast to ComposeReminderFragment.");
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsOpenedInEditMode) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * Setups title edit text to not has horizontal scrolling and activity's toolbar
     */
    protected void setup() {
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(3);
        ComposeBaseToolbarHelper.setupToolbar(this, mToolbar);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.compose_star_image_button)
    public void starImageButtonClicked() {
        if (!mInPrivateMode) {
            //note is in normal mode toggle starred / not starred
            if (mIsStarred) {
                //activity is in starredState, change it to notStarredState
                setNotStarredState();
            } else {
                //activity is in notStarredState, change it to starredState
                setStarredState();
            }
            mNoteModeChanged = !mNoteModeChanged;
        }
    }

    public void setStarredState() {
        mIsStarred = true;
        mStarImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    public void setNotStarredState() {
        mIsStarred = false;
        mStarImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }

    public void setInPrivateMode() {
        mInPrivateMode = true;
        getStarImageButton().setVisibility(View.GONE);
    }
}
