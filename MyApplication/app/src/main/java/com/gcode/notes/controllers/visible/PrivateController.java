package com.gcode.notes.controllers.visible;


import android.view.MenuItem;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.visible.callbacks.AuthenticationCallbacks;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.tasks.async.delete.DeletePrivateNotesTask;
import com.gcode.notes.tasks.async.encryption.DecryptAllNotesTask;
import com.gcode.notes.tasks.async.encryption.DecryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptAllNotesTaskCallbacks;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptTaskCallbacks;

import java.util.ArrayList;

public class PrivateController extends VisibleController implements
        DecryptAllNotesTaskCallbacks, AuthenticationCallbacks {
    //TODO: REFACTOR AND OPTIMIZE (IMPORTANT)

    boolean mScrollToTop;

    public PrivateController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        mScrollToTop = scrollToTop;
        mRecyclerView.setVisibility(View.INVISIBLE);
        mToolbar.setTitle(R.string.authentication_label);
        AuthenticationUtils.getInstance(mMainActivity, this).authenticate();
    }

    @Override
    public void setNewContent(ArrayList<ContentBase> newContent, boolean scrollToTop) {
        //it is called by LoadNewContentTask() when it is ready, override to prevent default behaviour
        new DecryptAllNotesTask(mMainActivity, this, newContent, mScrollToTop).execute();
    }

    @Override
    public void addItemAsFirst(ContentBase item) {
        //TODO: prevent double encrypt, decrypt (not very important)
        //it is called by AddItemFromDbToMainTask() when it is ready, override to prevent default behaviour
        new DecryptNoteTask(mMainActivity, new DecryptTaskCallbacks() {
            @Override
            public void onDecryptedSuccessfully(ContentBase contentBase) {
                PrivateController.super.addItemAsFirst(contentBase);
            }
        }).execute(item);
    }

    @Override
    public void onAddNote(ContentBase contentBase) {
        new DecryptNoteTask(mMainActivity, new DecryptTaskCallbacks() {
            @Override
            public void onDecryptedSuccessfully(ContentBase contentBase) {
                PrivateController.super.onAddNote(contentBase);
            }
        }).execute(contentBase);
    }

    @Override
    public void onNotesDecryptedSuccessfully(ArrayList<ContentBase> decryptedNotes, boolean scrollToTop) {
        mRecyclerView.setVisibility(View.VISIBLE);
        super.setNewContent(decryptedNotes, scrollToTop);
    }

    @Override
    public void onAuthenticated(String password) {
        super.setContent(mScrollToTop);
        mToolbar.setTitle(mMainActivity.getString(R.string.private_label));
    }

    @Override
    public void onPasswordTriesEnded() {
        MyDebugger.log("onPasswordTriesEnded()");

        //delete all private notes
        new DeletePrivateNotesTask().execute();

        //reset password
        MyUtils.saveToPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
        MyUtils.saveToPreferences(Keys.PREF_PASSWORD, Constants.NO_PASSWORD);

        onExitPrivate();
    }

    @Override
    public void onExitPrivate() {
        mRecyclerView.setVisibility(View.VISIBLE);
        MenuItem menuItem = mMainActivity.getDrawer().getMenu().findItem(mMainActivity.mPreviousSelectedId);
        mMainActivity.mDrawerOptionExecutor.onNavigationItemSelected(menuItem);
    }

    @Override
    public boolean shouldHandleMode(int mode) {
        return mode == Constants.MODE_PRIVATE;
    }
}
