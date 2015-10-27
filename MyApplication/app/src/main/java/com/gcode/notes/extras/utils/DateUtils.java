package com.gcode.notes.extras.utils;


import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    private static final String SQL_LITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DISPLAY_FORMAT = "dd-MM-yyyy HH:mm";

    public static final String LAST_MODIFIED = "Last modified: ";
    public static final String CREATION_DATE = "Created on: ";
    public static final String EXPIRATION_DATE = "Expires on: ";

    public static final int EXPIRATION_DAYS = 7;

    public static String getExpirationDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, EXPIRATION_DAYS);
        return parseDateInSQLiteFormat(calendar.getTime());
    }

    public static String formatDateForDisplay(String sqliteDateString) {
        String localePattern;

        final DateFormat localeDateFormat = android.text.format.DateFormat.getDateFormat(MyApplication.getAppContext());
        final DateFormat localeTimeFormat = android.text.format.DateFormat.getTimeFormat(MyApplication.getAppContext());
        if (localeDateFormat instanceof SimpleDateFormat && localeTimeFormat instanceof SimpleDateFormat) {
            // getDateFormat() returns a SimpleDateFormat from which we can extract the pattern
            localePattern = ((SimpleDateFormat) localeDateFormat).toPattern();
            localePattern += " " + ((SimpleDateFormat) localeTimeFormat).toPattern();
        } else {
            //getting localeDateFormat failed, create default
            MyDebugger.log("getting localeDateFormat failed, use DEFAULT_DISPLAY_FORMAT");
            //Locale.US affects only machine readability
            localePattern = new SimpleDateFormat(DEFAULT_DISPLAY_FORMAT, Locale.US).toPattern();
        }

        SimpleDateFormat sqliteSimpleDateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        Date date;
        try {
            date = sqliteSimpleDateFormat.parse(sqliteDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            MyDebugger.log("formatDateForDisplay", e.getMessage());
            date = new Date();
        }
        sqliteSimpleDateFormat.applyPattern(localePattern);
        return sqliteSimpleDateFormat.format(date);
    }

    public static String getCurrentTimeSQLiteFormatted() {
        return parseDateInSQLiteFormat(getCurrentTime());
    }

    private static String parseDateInSQLiteFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        return dateFormat.format(date);
    }

    private static Date getCurrentTime() {
        Date date;
        try {
            date = Calendar.getInstance().getTime();
        } catch (IllegalArgumentException ex) {
            MyDebugger.log("getCurrentTimeSQLiteFormatted()", ex.getMessage());
            date = new Date();
        }
        return date;
    }
}
