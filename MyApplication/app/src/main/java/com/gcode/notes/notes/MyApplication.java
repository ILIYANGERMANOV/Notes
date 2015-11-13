package com.gcode.notes.notes;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.gcode.notes.database.DatabaseController;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    private static DatabaseController mDatabase;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
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
        mInstance = this;
        mDatabase = new DatabaseController(this);
        Fresco.initialize(getApplicationContext());
    }
}
