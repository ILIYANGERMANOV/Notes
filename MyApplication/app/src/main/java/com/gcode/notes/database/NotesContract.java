package com.gcode.notes.database;

import android.provider.BaseColumns;

public final class NotesContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public NotesContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class NoteEntry implements BaseColumns {

    }

    public static abstract class ListEntry implements BaseColumns {

    }
}
