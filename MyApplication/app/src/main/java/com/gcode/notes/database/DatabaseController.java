package com.gcode.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.database.NotesContract.ContentEntry;
import com.gcode.notes.database.NotesContract.ListEntry;
import com.gcode.notes.database.NotesContract.NoteEntry;
import com.gcode.notes.database.NotesContract.PictureEntry;
import com.gcode.notes.database.NotesContract.SoundEntry;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.DateUtils;
import com.gcode.notes.extras.MyDebugger;

import java.net.URI;
import java.util.ArrayList;

public class DatabaseController {
    private NotesDbHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DatabaseController(Context context) {
        mHelper = new NotesDbHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public ArrayList<ContentBase> getNotesForMode(int mode) {
        ArrayList<ContentBase> mNotesList = new ArrayList<ContentBase>();

        String[] selectionArgs = {
                Integer.toString(mode)
        };

        Cursor cursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_CONTENT_FOR_MODE, selectionArgs);

        while (cursor.moveToNext()) {
            ContentBase currentItem = null;

            MyDebugger.log("Cursor moved to first!");
            if (cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TYPE)) == Constants.TYPE_NOTE) {
                //NoteData
                MyDebugger.log(cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CREATION_DATE)));
                MyDebugger.log("hasAttributes", cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ATTRIBUTES)));
                currentItem = new NoteData(
                        cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_MODE)),
                        cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_PRIORITY)),
                        cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_ATTRIBUTES)) != 0,
                        cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_REMINDER)),
                        DateUtils.parseString(cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CREATION_DATE))),
                        cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_EXPIRATION_DATE))
                );

                currentItem.setType(Constants.TYPE_NOTE);

                if (currentItem.hasAttributes()) {
                    //get attributes
                    MyDebugger.log("has attributes");
                    Cursor notesCursor = mDatabase.rawQuery(Queries.SELECT_ALL_FROM_NOTES_FOR_ID,
                            new String[]{
                                    Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                            }
                    );

                    if (notesCursor.moveToFirst()) {
                        if (notesCursor.getInt(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_HAS_PICTURE)) != 0) {
                            //TODO: get picture path
                            Cursor pictureCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_PICTURES_FOR_NOTE_ID,
                                    new String[]{
                                            Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                                    }
                            );

                            if (pictureCursor.moveToFirst()) {
                                ((NoteData) currentItem).setImageURI(URI.create(
                                        pictureCursor.getString(pictureCursor.getColumnIndex(PictureEntry.COLUMN_NAME_PATH))
                                ));
                                MyDebugger.log("picture set");
                            }
                            pictureCursor.close();
                        }
                        if (notesCursor.getInt(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_HAS_SOUND)) != 0) {
                            //TODO: get audio path
                            Cursor soundCursor = mDatabase.rawQuery(Queries.SELECT_PATH_FROM_SOUNDS_FOR_NOTE_ID,
                                    new String[]{
                                            Integer.toString(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TARGET_ID)))
                                    }
                            );

                            if (soundCursor.moveToFirst()) {
                                ((NoteData) currentItem).setAudioURI(URI.create(
                                        soundCursor.getString(soundCursor.getColumnIndex(SoundEntry.COLUMN_NAME_PATH))
                                ));
                                MyDebugger.log("sound set");
                            }

                            soundCursor.close();
                        }

                        ((NoteData) currentItem).setDescription(notesCursor.getString(notesCursor.getColumnIndex(NoteEntry.COLUMN_NAME_DESCRIPTION)));
                        notesCursor.close();
                    } else {
                        MyDebugger.log("notesCursor.moveToFirst() with title", currentItem.getTitle());
                    }
                }
            } else {
                //TODO: add ListData
            }

            mNotesList.add(currentItem);
        }

        cursor.close();

        return mNotesList;
    }

    public long insertNote(ContentBase contentBase) {
        if (!contentBase.isValidTitle()) {
            contentBase.setTitle(generateTitle());
        }
        mDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();

        if (contentBase.hasAttributes()) {
            if (contentBase.getType() == Constants.TYPE_NOTE) {
                //TODO: insert note
                NoteData noteData = (NoteData) contentBase;

                contentValues.put(NoteEntry.COLUMN_NAME_DESCRIPTION, noteData.getDescription());
                contentValues.put(NoteEntry.COLUMN_NAME_HAS_PICTURE, noteData.hasAttachedImage());
                contentValues.put(NoteEntry.COLUMN_NAME_HAS_SOUND, noteData.hasAttachedAudio());

                if (mDatabase.insert(NoteEntry.TABLE_NAME, null, contentValues) == -1) {
                    //TODO: handle error
                    contentBase.setAttributes(false);
                    MyDebugger.log("ERROR INSERTING ATTRIBUTES!");
                }

                if (noteData.hasAttachedImage()) {
                    insertPicture(noteData.getImageURI());
                }
                if (noteData.hasAttachedAudio()) {
                    insertAudio(noteData.getAudioURI());
                }
            } else {
                //TODO: insert list
            }
        }

        contentValues.clear();

        if (contentBase.hasAttributes()) {
            MyDebugger.log("attribute inserted successfully!");
            String tableName = contentBase.getType() == Constants.TYPE_NOTE ? NoteEntry.TABLE_NAME : ListEntry.TABLE_NAME;
            contentValues.put(ContentEntry.COLUMN_NAME_TARGET_ID, getLastRowFromTable(tableName));
        }

        contentValues.put(ContentEntry.COLUMN_NAME_TITLE, contentBase.getTitle());
        contentValues.put(ContentEntry.COLUMN_NAME_MODE, contentBase.getMode());
        contentValues.put(ContentEntry.COLUMN_NAME_PRIORITY, contentBase.getPriority());
        contentValues.put(ContentEntry.COLUMN_NAME_TYPE, contentBase.getType());
        contentValues.put(ContentEntry.COLUMN_NAME_ATTRIBUTES, contentBase.hasAttributes());
        contentValues.put(ContentEntry.COLUMN_NAME_REMINDER, contentBase.getReminderString());
        contentValues.put(ContentEntry.COLUMN_NAME_CREATION_DATE, DateUtils.getCurrentTime());
        contentValues.putNull(ContentEntry.COLUMN_NAME_EXPIRATION_DATE);

        long newlyInsertedRow = mDatabase.insert(ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_EXPIRATION_DATE, contentValues);

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();

        return newlyInsertedRow;
    }

    private void insertPicture(URI imageURI) {
        //TODO: insert picture
        ContentValues contentValues = new ContentValues();
        contentValues.put(PictureEntry.COLUMN_NAME_PATH, imageURI.toString());
        contentValues.put(PictureEntry.COLUMN_NAME_NOTE_ID, getNoteId());

        mDatabase.insert(PictureEntry.TABLE_NAME, null, contentValues);
    }

    private void insertAudio(URI audioURI) {
        //TODO: insert audio
        ContentValues contentValues = new ContentValues();
        contentValues.put(SoundEntry.COLUMN_NAME_PATH, audioURI.toString());
        contentValues.put(SoundEntry.COLUMN_NAME_NOTE_ID, getNoteId());

        mDatabase.insert(SoundEntry.TABLE_NAME, null, contentValues);
    }

    private String generateTitle() {
        return "Note " + Integer.toString(getNoteId());
    }

    public int getNoteId() {
        int lastRowId = getLastRowFromTable(ContentEntry.TABLE_NAME);
        return lastRowId != 0 ? lastRowId + 1 : 0;
    }

    public int getLastRowFromTable(String tableName) {
        int targetId = 0;
        Cursor cursor = mDatabase.rawQuery(Queries.selectLastRowForTable(tableName), null);
        if (cursor.moveToFirst()) {
            targetId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        }
        cursor.close();
        return targetId;
    }
}
