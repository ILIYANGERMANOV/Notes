package com.gcode.notes.activities.helpers.main.ui.fab.builders;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.fab.FabHelper;
import com.gcode.notes.activities.helpers.main.ui.fab.listeners.FabMenuStateChangeListener;
import com.gcode.notes.extras.values.Tags;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

public class FloatingActionMenuBuilder {
    public static void buildFabMenu(FabHelper fabHelper, MainActivity mainActivity) {
        FabSubActionButtonBuilder fabActionBtnBuilder = new FabSubActionButtonBuilder(mainActivity, fabHelper);

        //builds floating action menu, attached to our fab
        MainActivity.mActionMenu = new FloatingActionMenu.Builder(mainActivity)
                .addSubActionView(fabActionBtnBuilder.buildSubActionBtn(R.drawable.ic_note_add_white_24dp, Tags.TAG_TEXT_NOTE))
                .addSubActionView(fabActionBtnBuilder.buildSubActionBtn(R.drawable.ic_list_white_24dp, Tags.TAG_LIST))
                .addSubActionView(fabActionBtnBuilder.buildSubActionBtn(R.drawable.ic_keyboard_voice_white_24dp, Tags.TAG_VOICE_NOTE))
                .addSubActionView(fabActionBtnBuilder.buildSubActionBtn(R.drawable.ic_photo_camera_white_24dp, Tags.TAG_CAMERA))
                .attachTo(mainActivity.getFab())
                .build();

        //sets floating action menu, listeners
        MainActivity.mActionMenu.setStateChangeListener(new FabMenuStateChangeListener(mainActivity));
    }
}
