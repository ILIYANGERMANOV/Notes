package com.gcode.notes.tasks.async.encryption.callbacks;


import com.gcode.notes.data.base.ContentBase;

public interface EncryptTaskCallbacks {
    void onEncryptedSuccessfully(ContentBase contentBase);
}
