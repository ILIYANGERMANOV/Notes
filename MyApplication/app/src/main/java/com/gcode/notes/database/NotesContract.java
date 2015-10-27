package com.gcode.notes.database;

import android.provider.BaseColumns;

public final class NotesContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public NotesContract() {
    }

    public static abstract class ContentEntry implements BaseColumns {
        public static final String TABLE_NAME = "content";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MODE = "mode";
        public static final String COLUMN_NAME_TYPE = "content_type";
        public static final String COLUMN_NAME_TARGET_ID = "target_id";
        public static final String COLUMN_NAME_ATTRIBUTES = "attributes";
        public static final String COLUMN_NAME_REMINDER = "time_reminder";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
        public static final String COLUMN_NAME_LAST_MODIFIED_DATE = "last_modified_date";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_HAS_SOUND = "has_sound";
        public static final String COLUMN_NAME_HAS_PICTURE = "has_picture";
    }

    public static abstract class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "pictures";
        public static final String COLUMN_NAME_PATH = "picture_path";
        public static final String COLUMN_NAME_NOTE_ID = "note_id";
    }

    public static abstract class SoundEntry implements BaseColumns {
        public static final String TABLE_NAME = "sounds";
        public static final String COLUMN_NAME_PATH = "sound_path";
        public static final String COLUMN_NAME_NOTE_ID = "note_id";
    }

    public static abstract class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "lists";
        public static final String COLUMN_NAME_TASKS_SERIALIZED = "tasks_serialized";
    }
}
