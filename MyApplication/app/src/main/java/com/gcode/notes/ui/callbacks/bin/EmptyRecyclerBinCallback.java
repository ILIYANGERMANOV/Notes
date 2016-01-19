package com.gcode.notes.ui.callbacks.bin;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.bin.BinController;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class EmptyRecyclerBinCallback implements MaterialDialog.SingleButtonCallback {
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (MyApplication.getWritableDatabase().emptyRecyclerBin()) {
            BaseController controller = BaseController.getInstance();
            if (controller.getControllerId() == Constants.CONTROLLER_BIN) {
                BinController binController = (BinController) controller;
                binController.setContent(false);
            }
        } else {
            MyDebugger.log("Failed to empty recycler bin");
        }
    }
}
