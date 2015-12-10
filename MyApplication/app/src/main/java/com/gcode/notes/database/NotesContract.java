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
        public static final String COLUMN_NAME_HAS_ATTRIBUTES = "attributes";
        public static final String COLUMN_NAME_REMINDER = "time_reminder";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
        public static final String COLUMN_NAME_LAST_MODIFIED_DATE = "last_modified_date";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTOS_PATHS = "photos_paths";
        public static final String COLUMN_NAME_AUDIO_PATH = "audio_path";
    }

    public static abstract class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "lists";
        public static final String COLUMN_NAME_TASKS_SERIALIZED = "tasks_serialized";
    }
}
