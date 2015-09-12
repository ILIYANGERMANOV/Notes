package com.gcode.notes.notes;

import android.app.Application;
import android.content.Context;

import com.gcode.notes.database.DatabaseController;

public class MyApplication extends Application {
    private static MyApplication sInstance;

    private static DatabaseController mDatabase;

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DatabaseController getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DatabaseController(getAppContext());
        }
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new DatabaseController(this);
    }
}
