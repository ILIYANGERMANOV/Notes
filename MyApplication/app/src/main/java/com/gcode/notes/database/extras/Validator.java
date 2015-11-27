package com.gcode.notes.database.extras;


import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.data.main.ContentBase;

public class Validator {
    public static void validateTitle(SQLiteDatabase mDatabase, ContentBase contentBase) {
        if (!contentBase.hasValidTitle()) {
            contentBase.setTitle(generateItemTitle(mDatabase));
        }
    }

    private static String generateItemTitle(SQLiteDatabase mDatabase) {
        int noteNumber = Selector.getFirstOrNextIdFromContent(mDatabase);
        return "Note " + Integer.toString(noteNumber != 0 ? noteNumber : 1);
    }
}
