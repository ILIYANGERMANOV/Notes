package com.gcode.notes.extras;


import android.view.Menu;

public class Constants {
    public static final String PREFERENCES_FILE = "notes_settings";
    public static final int FAB_THRESHOLD_TRANSLATION_Y = 15;
    public static final int FAB_MAX_TRANSLATION_Y = 200;
    public static final int GRID_COLUMNS_COUNT = 2;

    public static final int COMPOSE_NOTE_REQUEST_CODE = 1;

    public static final int ERROR = 666;

    public static final String NOTE_ADDED_SUCCESSFULLY = "note_added_successfully";
    public static final String COMPOSE_NOTE_MODE = "compose_note_mode";

    public static final String DEBUG_TAG = "ddq";

    public static final int TYPE_NOTE = 1;
    public static final int TYPE_LIST = 2;

    public static final int MODE_NORMAL = 1;
    public static final int MODE_IMPORTANT = 2;
    public static final int MODE_PRIVATE = 3;
    public static final int MODE_DELETED_NORMAL = 4;
    public static final int MODE_DELETED_IMPORTANT = 5;

    public static final String CONTROLLER_ID = "controller_id";

    public static final int CONTROLLER_ALL_NOTES = 1;
    public static final int CONTROLLER_IMPORTANT = 2;
    public static final int CONTROLLER_PRIVATE = 3;
    public static final int CONTROLLER_BIN = 4;

    public static final int DATABASE_ERROR = -1;

    public static final int MENU_EMPTY_BIN = Menu.FIRST + 3;

    public static final String NO_REMINDER = "no_reminder";
    public static final String NO_AUDIO = "no_audio_attached";

    public static final String EXTRA_LIST_DATA = "list_data_extra";
    public static final String EXTRA_NOTE_DATA = "note_data_extra";
    //TODO: add picture, sound extra
    public static final String EXTRA_LIST_DATA_ITEMS = "list_data_items_extra";
    public static final String EXTRA_TICKED_LIST_DATA_ITEMS = "ticked_list_data_items_extra";

    public static final int CALLED_FROM_MAIN = 1;
    public static final int CALLED_FROM_DISPLAY = 2;
}
