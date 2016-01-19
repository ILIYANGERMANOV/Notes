package com.gcode.notes.controllers.visible;


import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.visible.callbacks.AuthenticationCallbacks;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.tasks.async.encryption.DecryptAllNotesTask;
import com.gcode.notes.tasks.async.encryption.DecryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.CryptTaskCallbacks;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptNotesTaskCallbacks;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

import java.util.ArrayList;

public class PrivateController extends VisibleController implements
        DecryptNotesTaskCallbacks, CryptTaskCallbacks, AuthenticationCallbacks {
    //TODO: REFACTOR AND OPTIMIZE (IMPORTANT)

    ArrayList<ContentBase> mNewContent;
    boolean mScrollToTop;

    public PrivateController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mMainActivity.getString(R.string.private_label));
    }

    @Override
    public void setNewContent(ArrayList<ContentBase> newContent, boolean scrollToTop) {
        //it is called by LoadNewContentTask() when it is ready, override to prevent default behaviour
        AuthenticationUtils.getInstance(mMainActivity, this).authenticate();
        mNewContent = newContent;
        mScrollToTop = scrollToTop;
    }

    @Override
    public void addItem(ContentBase item) {
        //TODO: prevent double encrypt, decrypt (not very important)
        //it is called by AddItemFromDbToMainTask() when it is ready, override to prevent default behaviour
        new DecryptNoteTask(mMainActivity, this).execute(item);
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        if (item.getMode() != Constants.MODE_PRIVATE) {
            new RemoveItemFromMainTask(mMainActivity.getString(R.string.note_moved_to_all_notes)).execute(item);
        }
    }

    @Override
    public void onItemChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_PRIVATE) {
            updateItem(item);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_PRIVATE) {
            new AddItemFromDbToMainTask().execute();
        }
    }

    @Override
    public void onNotesDecryptedSuccessfully(ArrayList<ContentBase> decryptedNotes, boolean scrollToTop) {
        super.setNewContent(decryptedNotes, scrollToTop);
    }

    @Override
    public void onTaskCompletedSuccessfully(ContentBase contentBase) {
        super.addItem(contentBase);
    }

    @Override
    public void onAuthenticated(String password) {
        MyDebugger.log("authenticated", password);
        new DecryptAllNotesTask(mMainActivity, this, mNewContent, mScrollToTop).execute();
    }

    @Override
    public void onPasswordTriesEnded() {
        MyDebugger.log("onPasswordTriesEnded()");
        //delete private notes
        MyUtils.saveToPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
        MyUtils.saveToPreferences(Keys.PREF_PASSWORD, Constants.NO_PASSWORD);
        onExitPrivate();
    }

    @Override
    public void onExitPrivate() {
        MyDebugger.log("onExitPrivate()");
    }
}