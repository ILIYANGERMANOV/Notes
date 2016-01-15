package com.gcode.notes.tasks.async.encryption.callbacks;


import com.gcode.notes.data.base.ContentBase;

public interface EncryptTaskCallbacks {
    //TODO: remove useless setOldResult (use mIsInPrivateMode flag in saveToDbAndSetResult())
    void onEncryptedSuccessfully(ContentBase contentBase, boolean setOldResult);
}
