package com.gcode.notes.adapters.main.viewholders.listeners;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.note.bin.DisplayNoteBinActivity;
import com.gcode.notes.activities.display.note.editable.DisplayNoteNormalActivity;
import com.gcode.notes.activities.display.note.editable.DisplayNotePrivateActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class NoteItemOnClickListener extends BaseItemListener {
    NoteData mNoteData;

    public NoteItemOnClickListener(Activity activity, NoteData noteData) {
        super(activity);
        mNoteData = noteData;
    }

    @Override
    public void onClick(View v) {
        if (mDisabled)
            return; //if disabled prevent further execution (stop click event)

        Intent intent;
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
            case Constants.CONTROLLER_IMPORTANT:
                intent = new Intent(mActivity, DisplayNoteNormalActivity.class);
                break;
            case Constants.CONTROLLER_PRIVATE:
                intent = new Intent(mActivity, DisplayNotePrivateActivity.class);
                break;
            case Constants.CONTROLLER_BIN:
                intent = new Intent(mActivity, DisplayNoteBinActivity.class);
                break;
            default:
                MyDebugger.log("NoteItemOnClickListener", "invalid controller id");
                return;
        }

        intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
        mActivity.startActivityForResult(intent, Constants.DISPLAY_NOTE_REQUEST_CODE);
    }
}