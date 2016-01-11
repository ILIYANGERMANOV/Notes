package com.gcode.notes.database.extras;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gcode.notes.R;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class Validator {
    public static void validateTitle(SQLiteDatabase database, ContentBase contentBase, boolean existingNote) {
        if (!contentBase.hasValidTitle()) {
            //contentBase doesn't have valid title, generate new one
            contentBase.setTitle(generateTitle(database, contentBase.getType(), existingNote));
        }
    }

    private static String generateTitle(SQLiteDatabase database, int type, boolean existingNote) {
        Context context = MyApplication.getAppContext();
        String title = type == Constants.TYPE_NOTE ? context.getString(R.string.note) : context.getString(R.string.list);

        //if it is new note (not existing) it will be inserted after generating title so its note number should be next
        //if it is existing note its note number should the current count of notes, cuz it won't be inserted
        //after generating title
        long noteNumber = !existingNote ? Selector.getNextNumberForType(database, type) :
                Selector.getCountFromContentTableForType(database, type);

        return title + noteNumber;
    }
}
