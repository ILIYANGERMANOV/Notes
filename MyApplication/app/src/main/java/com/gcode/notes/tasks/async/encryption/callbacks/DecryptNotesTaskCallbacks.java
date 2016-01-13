package com.gcode.notes.tasks.async.encryption.callbacks;

import com.gcode.notes.data.base.ContentBase;

import java.util.ArrayList;

public interface DecryptNotesTaskCallbacks {
    void onNotesDecryptedSuccessfully(ArrayList<ContentBase> decryptedNotes, boolean scrollToTop);
}
