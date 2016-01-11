package com.gcode.notes.adapters.viewholders.main.listeners;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.note.DisplayNoteBinActivity;
import com.gcode.notes.activities.display.note.DisplayNoteNormalActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class NoteItemOnClickListener extends BaseItemListener implements View.OnClickListener {
    NoteData mNoteData;

    public NoteItemOnClickListener(Activity activity, NoteData noteData) {
        super(activity);
        mNoteData = noteData;
    }

    @Override
    public void onClick(View v) {
        if(mDisabled) return; //if disabled prevent further execution (stop click event)

        Intent intent = null;
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
            case Constants.CONTROLLER_IMPORTANT:
                intent = new Intent(mActivity, DisplayNoteNormalActivity.class);
                break;
            case Constants.CONTROLLER_PRIVATE:
                //TODO: private
                break;
            case Constants.CONTROLLER_BIN:
                intent = new Intent(mActivity, DisplayNoteBinActivity.class);
                break;
            default:
                MyDebugger.log("NoteItemOnClickListener", "invalid controller id");
                return;
        }
        if (intent != null) {
            intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
            //MyTransitionHelper.startSharedElementTransitionForResult(mActivity, v, intent, Constants.NOTE_FROM_DISPLAY_REQUEST_CODE); transitions disabled due to bug
            mActivity.startActivityForResult(intent, Constants.NOTE_FROM_DISPLAY_REQUEST_CODE);
        } else {
            MyDebugger.log("NoteItemOnClickListener", "intent is null");
        }
    }
}