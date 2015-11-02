package com.gcode.notes.activities.helpers.main;


import android.content.Intent;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class MainResultHandler {
    public static void handleResult(int requestCode, Intent data) {
        switch (requestCode) {
            case Constants.LIST_FROM_DISPLAY_RES_CODE:
                handleListFromDisplay(data);
                break;
            case Constants.COMPOSE_NOTE_REQUEST_CODE:
                handleComposeResult(data);
                break;
            case Constants.NOTE_FROM_DISPLAY_RES_CODE:
                handleNoteFromDisplay(data);
                break;
        }
    }

    private static void handleComposeResult(Intent data) {
        if (data.getBooleanExtra(Constants.NOTE_ADDED_SUCCESSFULLY, false)) {
            int mode = data.getIntExtra(Constants.COMPOSE_NOTE_MODE, Constants.ERROR);
            if (mode != Constants.ERROR) {
                BaseController.getInstance().onItemAdded(mode);
            } else {
                MyDebugger.log("onActivityResult() mode ERROR!");
            }
        }
    }

    private static void handleNoteFromDisplay(Intent data) {
        String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
        if (serializedNoteData != null) {
            NoteData noteData = Serializer.parseNoteData(serializedNoteData);
            if (noteData != null) {
                notifyControllerForChanges(data, noteData);
            } else {
                MyDebugger.log("NOTE_FROM_DISPLAY noteData is null!");
            }
        } else {
            MyDebugger.log("NOTE_FROM_DISPLAY serializedNoteData is null!");
        }
    }

    private static void handleListFromDisplay(Intent data) {
        String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
        if (serializedListData != null) {
            ListData listData = Serializer.parseListData(serializedListData);
            if (listData != null) {
                notifyControllerForChanges(data, listData);
            } else {
                MyDebugger.log("LIST_FROM_DISPLAY listData is null!");
            }
        } else {
            MyDebugger.log("LIST_FROM_DISPLAY serializedListData is null!");
        }
    }

    private static void notifyControllerForChanges(Intent data, ContentBase contentBase) {
        BaseController controller = BaseController.getInstance();
        controller.onItemChanged(contentBase);
        if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
            controller.onItemModeChanged(contentBase);
        }
    }
}
