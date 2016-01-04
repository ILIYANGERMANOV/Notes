package com.gcode.notes.activities.helpers.main.ui.fab.listeners;

import android.animation.ObjectAnimator;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.extras.values.Constants;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

public class FabOnClickListener implements View.OnClickListener {
    //TODO: REFACTOR
    //TODO: fix onClick() spam problems (prelolipop click events isn't handled, lolipop random option is selected)
    public FloatingActionMenu mActionMenu = MainActivity.mActionMenu; //reference for easier access

    @Override
    public void onClick(View v) {
        if (mActionMenu == null) {
            //mAction menu is null, log it and prevent further execution
            MyDebugger.log("FabOnClickListener mActionMenu is null");
            return;
        }

        if (v.getTranslationY() > Constants.FAB_THRESHOLD_TRANSLATION_Y) {
            //fab had passed threshold translation y (its too down), move it to original position
            //so fab action menu animation won't look weird
            if (MyUtils.isLollipop()) {
                //its lollipop or newer version, use translationY for better user experience
                ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), 0);
                anim.start();
            }
            v.setTranslationY(0); //restores fab original position
        }
        mActionMenu.toggle(true); //open/closes fab
    }
}
