package com.gcode.notes.activities.helpers.compose.base;

import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.extras.values.Constants;

public class ComposeBaseStartStateHelper {
    protected void setupFromZero(ComposeBaseActivity composeBaseActivity) {
        composeBaseActivity.mIsOpenedInEditMode = false;
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
                composeBaseActivity.setNotStarredState();
                break;
            case Constants.CONTROLLER_IMPORTANT:
                composeBaseActivity.setStarredState();
                break;
            case Constants.CONTROLLER_PRIVATE:
                //TODO: PRIVATE: set mStarImageButton to sth
                break;
            default:
                break;
        }
    }

    protected void setupFromEditMode(ComposeBaseActivity composeBaseActivity, ContentBase contentBase) {
        composeBaseActivity.mIsOpenedInEditMode = true;
        composeBaseActivity.getTitleEditText().setText(contentBase.getTitle());
        if (contentBase.isImportant()) {
            composeBaseActivity.setStarredState();
        }
        if (contentBase.hasReminder()) {
            //TODO: implement real reminder
            //composeBaseActivity.getReminderTextView().setText(contentBase.getReminder());
        }
    }
}
