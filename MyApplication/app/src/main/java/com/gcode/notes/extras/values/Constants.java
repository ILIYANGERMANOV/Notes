package com.gcode.notes.extras.values;


import android.view.Menu;

public class Constants {
    public static final String PREFERENCES_FILE = "notes_settings";
    public static final int FAB_THRESHOLD_TRANSLATION_Y = 15;
    public static final int FAB_MAX_TRANSLATION_Y = 200;
    public static final int GRID_COLUMNS_COUNT = 2;

    public static final int COMPOSE_NOTE_REQUEST_CODE = 1;
    public static final int LIST_FROM_DISPLAY_REQUEST_CODE = 2;
    public static final int NOTE_FROM_DISPLAY_REQUEST_CODE = 3;
    public static final int REQUEST_TAKE_PHOTO = 4;
    public static final int REQUEST_OPEN_GALLERY = 5;
    public static final int OPEN_PHOTO_IN_GALLERY_REQ_CODE = 6;
    public static final int SPEECH_INPUT_REQ_CODE = 7;

    //TODO: fix bug on note with id 7833
    public static final int ERROR = 7833;

    public static final int NO_VALUE = 1;

    //must be positive and greater than max list items + 1
    public static final int NO_FOCUS = 512;

    public static final String NOTE_ADDED_SUCCESSFULLY = "note_added_successfully";
    public static final String NOTE_UPDATED_SUCCESSFULLY = "note_updated_successfully";
    public static final String COMPOSE_NOTE_MODE = "compose_note_mode";

    public static final int TYPE_NOTE = 1;
    public static final int TYPE_LIST = 2;

    public static final int MODE_NORMAL = 1;
    public static final int MODE_IMPORTANT = 2;
    public static final int MODE_PRIVATE = 3;
    public static final int MODE_DELETED_NORMAL = 4;
    public static final int MODE_DELETED_IMPORTANT = 5;

    public static final int CONTROLLER_ALL_NOTES = 1;
    public static final int CONTROLLER_IMPORTANT = 2;
    public static final int CONTROLLER_PRIVATE = 3;
    public static final int CONTROLLER_BIN = 4;

    public static final int DATABASE_ERROR = -1;

    public static final int MENU_EMPTY_BIN = Menu.FIRST + 3;

    public static final String NO_LOCATION = "no_location";
    public static final String NO_DATE = "no_date";
    public static final String NO_REMINDER = "no_reminder";
    public static final String NO_AUDIO = "no_audio_attached";

    public static final int SETUP_FROM_ZERO = 1;
    public static final int SETUP_FROM_EDIT_MODE = 2;
    public static final int SETUP_FROM_SCREEN_ROTATION = 3;
    public static final int SETUP_FROM_PHOTO = 4;
    public static final int SETUP_FROM_AUDIO = 5;

    //extras for activities
    public static final String EXTRA_LIST_DATA = "list_data_extra";
    public static final String EXTRA_NOTE_DATA = "note_data_extra";
    public static final String EXTRA_SETUP_FROM = "setup_from_extra";
    public static final String EXTRA_PHOTO_URI = "photo_uri_extra";
    public static final String EXTRA_AUDIO_PATH = "audio_path_extra";
    public static final String EXTRA_RECOGNIZED_SPEECH_TEXT = "recognized_speech_text_extra";
    public static final String EXTRA_DELETED_AUDIO = "deleted_audio_extra";
    public static final String EXTRA_LAST_FOCUSED = "last_focused_extra";
    public static final String EXTRA_IS_OPENED_IN_EDIT_MODE = "is_opened_in_edit_mode_extra";
    public static final String EXTRA_IS_STARRED = "is_starred_extra";
    public static final String EXTRA_NOTE_MODE_CHANGED = "note_mode_changed_extra";
    public static final String EXTRA_IS_DONE_HIDDEN = "is_done_hidden_extra";

    //extras for fragments
    public static final String EXTRA_IS_REMINDER_SET = "is_reminder_set_extra";
    public static final String EXTRA_YEAR = "year_extra";
    public static final String EXTRA_MONTH = "month_extra";
    public static final String EXTRA_DAY = "day_extra";
    public static final String EXTRA_HOUR = "hour_extra";
    public static final String EXTRA_MINUTE = "minute_extra";

    //extras for reminder notification
    public static final String EXTRA_FROM_REMINDER_NOTIFICATION = "from_reminder_notification_extra";
    public static final String EXTRA_TYPE = "type_extra";


    public static final int MAX_LIST_ITEMS_TO_DISPLAY = 5;
    public static final int MAX_DESCRIPTION_LINES_TO_DISPLAY = 13;
    public static final int MAX_LIST_INPUT_ITEM_LINES = 10;
    public static final int MAX_IMAGES_TO_DISPLAY = 2;

    public static final String NOTES_FOLDER_NAME = "Notes";
    public static final String RECORDS_FOLDER_NAME = "Voice records";

    public static final int MAX_TRIES_TO_RESOLVE = 3;

    public static final int MINIMUM_DELAY = 20;
    public static final int SHORT_DELAY = 50;
    public static final int MEDIUM_DELAY = 150;
    public static final int LONG_DELAY = 250;

    public static final int DELAY_SO_USER_CAN_SEE = 300;

    public static final long LOCATION_VALID_TIME = 900000;
}
