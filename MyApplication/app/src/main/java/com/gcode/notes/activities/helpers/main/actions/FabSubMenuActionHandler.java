package com.gcode.notes.activities.helpers.main.actions;

import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.VoiceUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.ui.helpers.DialogHelper;

public class FabSubMenuActionHandler {
    public static void handleItemClick(MainActivity mainActivity, View itemView) {
        if (itemView.getTag() == null) return; //secure that view has tag

        String tag = (String) itemView.getTag(); //tag used to identify which item was clicked
        switch (tag) {
            case Tags.TAG_TEXT_NOTE:
                //text note clicked, start compose note activity
                Intent intent = new Intent(mainActivity, ComposeNoteActivity.class);
                intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_ZERO);
                mainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_LIST:
                //list clicked, start compose list activity
                intent = new Intent(mainActivity, ComposeListActivity.class);
                mainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_VOICE_NOTE:
                //voice note clicked, prompt Text-to-Speech
                VoiceUtils.promptSpeechInput(mainActivity);
                break;
            case Tags.TAG_CAMERA:
                //camera clicked, build dialog for attaching image
                DialogHelper.buildAddPictureDialog(mainActivity);
                break;
            default:
                //unknown TAG, log it and prevent further execution
                MyDebugger.log("FabMenuActionHelper handleItemClick() unknown tag!");
                return;
        }

        //!NOTE: startActivityForResult() and startActivity() doesn't terminate current code execution
        if (MainActivity.mActionMenu != null) {
            //mActionMenu is ok, item click consumed, close mActionMenu
            MainActivity.mActionMenu.close(false);
        } else {
            //mActionMenu is null, log it
            MyDebugger.log("FloatingActionButtonHelper: onClick(): mActionMenu is null.");
        }
    }
}
