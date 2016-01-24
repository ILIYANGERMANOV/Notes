package com.gcode.notes.tasks.async.encryption.callbacks;


import com.gcode.notes.data.base.ContentBase;

public interface DecryptTaskCallbacks {
    void onDecryptedSuccessfully(ContentBase contentBase);
}
