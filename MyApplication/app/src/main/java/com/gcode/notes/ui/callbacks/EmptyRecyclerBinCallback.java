package com.gcode.notes.ui.callbacks;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class EmptyRecyclerBinCallback extends MaterialDialog.ButtonCallback {
    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        if (MyApplication.getWritableDatabase().emptyRecyclerBin()) {
            BaseController controller = BaseController.getInstance();
            if (controller.getControllerId() == Constants.CONTROLLER_BIN) {
                BinController binController = (BinController) controller;
                binController.setContent();
            }
        } else {
            MyDebugger.log("Failed to empty recycler bin");
        }
        dialog.cancel();
    }
}
