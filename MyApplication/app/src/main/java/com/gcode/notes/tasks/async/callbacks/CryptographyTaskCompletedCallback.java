package com.gcode.notes.tasks.async.callbacks;


import com.gcode.notes.data.base.ContentBase;

public interface CryptographyTaskCompletedCallback {
    void onTaskCompletedSuccessfully(ContentBase contentBase);
}
