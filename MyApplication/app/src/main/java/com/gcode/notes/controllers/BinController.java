package com.gcode.notes.controllers;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.motions.MyAnimator;
import com.gcode.notes.tasks.async.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.LoadContentTask;

public class BinController extends BaseController {

    public BinController(Context context, Toolbar toolbar, RecyclerView recyclerView,
                         FloatingActionButton fab, AppBarLayout appBarLayout,
                         SimpleItemTouchHelperCallback simpleItemTouchHelperCallback) {

        super(context, toolbar, recyclerView, fab, appBarLayout, simpleItemTouchHelperCallback);
    }

    @Override
    public void setContent(boolean notForFirstTime) {
        super.setContent(notForFirstTime);
        mToolbar.setTitle("Bin");
        new LoadContentTask(notForFirstTime).execute();
        mSimpleItemTouchHelperCallback.setLongPressDragEnabled(false);
    }

    @Override
    protected void onSetContentAnimation() {
        super.onSetContentAnimation();
        mFab.setVisibility(View.GONE);
        if (mPreviousControllerId != Constants.CONTROLLER_BIN) {
            MyAnimator.startAnimationOnView(mContext, mFab, R.anim.collapse_anim);
        }

        //postDelayed used to prevent button flashing when it was translated out of the screen,
        //happens cuz view#setVisibility doesn't take effect immediately
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.setTranslationY(0); //reset fab position to prevent issue happening,
                //when user is in bin before NoteDeletedSnackbar is appearing
                // FAB is going too upper when back to other controllers (its pushed up by the snackbar)
            }
        }, Constants.MEDIUM_DELAY);
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_DELETED_NORMAL || mode == Constants.MODE_DELETED_IMPORTANT) {
            new AddItemFromDbToMainTask().execute();
        }
    }
}
