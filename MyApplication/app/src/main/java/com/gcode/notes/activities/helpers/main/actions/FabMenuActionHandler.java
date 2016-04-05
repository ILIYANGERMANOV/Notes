package com.gcode.notes.activities.helpers.main.actions;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.helpers.main.ui.FabMenuHelper;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.VoiceUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class FabMenuActionHandler {
    //TODO: REFACTOR AND OPTIMIZE (replace tags with constants)

    public static void handleItemClick(final MainActivity mainActivity, View itemView) {
        if (itemView.getTag() == null) return; //secure that view has tag

        String tag = (String) itemView.getTag(); //tag used to identify which item was clicked

        if (tag.equals(mainActivity.getString(R.string.fab_label_note))) {
            //text note clicked, start compose note activity
            Intent intent = new Intent(mainActivity, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_ZERO);
            mainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else if (tag.equals(mainActivity.getString(R.string.fab_label_list))) {
            //list clicked, start compose list activity
            Intent intent = new Intent(mainActivity, ComposeListActivity.class);
            mainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
        } else if (tag.equals(mainActivity.getString(R.string.fab_label_voice))) {
            //voice note clicked, prompt Text-to-Speech
            VoiceUtils.promptSpeechInput(mainActivity);
        } else if (tag.equals(mainActivity.getString(R.string.fab_label_camera))) {
            //camera clicked, build dialog for attaching image
            DialogBuilder.buildAddPictureDialog(mainActivity);
        } else {
            //unknown tag, log it and prevent further execution
            MyDebugger.log("FabMenuActionHelper handleItemClick() unknown tag!");
            return;
        }

        //!NOTE: startActivityForResult() and startActivity() doesn't terminate current code execution

        mainActivity.mIsFabMenuOpened = false; //set flag to false, cuz at this point menu should be close
        //if not set by starting activity and fast screen rotation the menu will remain open

        //delay it, cuz if user click before animation has finished fab menu will not close
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainActivity.getFabMenu().close(true); //click event consumed, close fab menu
                FabMenuHelper.setTouchListenerFlagsDown(); //so click not consumed bug won't appear
            }
        }, Constants.DELAY_SO_USER_CAN_SEE);
    }
}
