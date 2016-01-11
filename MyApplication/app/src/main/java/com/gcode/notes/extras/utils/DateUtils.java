package com.gcode.notes.extras.utils;


import android.content.Context;

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

    public static final int EXPIRATION_DAYS = 7;

    public static String getExpirationDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, EXPIRATION_DAYS);
        return formatDateInSQLiteFormat(calendar.getTime());
    }

    public static String formatDateTimeForDisplay(String sqliteDateString) {
        String localePattern;

        final DateFormat localeDateFormat = android.text.format.DateFormat.getDateFormat(MyApplication.getAppContext());
        final DateFormat localeTimeFormat = android.text.format.DateFormat.getTimeFormat(MyApplication.getAppContext());
        if (localeDateFormat instanceof SimpleDateFormat && localeTimeFormat instanceof SimpleDateFormat) {
            // getDateFormat() returns a SimpleDateFormat from which we can extract the pattern
            localePattern = ((SimpleDateFormat) localeDateFormat).toLocalizedPattern();
            localePattern += " " + ((SimpleDateFormat) localeTimeFormat).toLocalizedPattern();
        } else {
            //getting localeDateFormat failed, create default
            MyDebugger.log("getting localeDateFormat failed, use DEFAULT_DISPLAY_FORMAT");
            //Locale.US affects only machine readability
            localePattern = new SimpleDateFormat(DEFAULT_DISPLAY_FORMAT, Locale.US).toLocalizedPattern();
        }

        SimpleDateFormat sqliteSimpleDateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        Date date;
        try {
            //trying to parse date in database with SQL_LITE_DATE_FORMAT
            date = sqliteSimpleDateFormat.parse(sqliteDateString);
        } catch (ParseException e) {
            //parsing has failed, create new dummy date
            e.printStackTrace();
            MyDebugger.log("formatDateTimeForDisplay", e.getMessage());
            date = new Date();
        }
        sqliteSimpleDateFormat.applyPattern(localePattern);
        return sqliteSimpleDateFormat.format(date);
    }

    @SuppressWarnings("deprecation")
    public static String formatTime(Context context, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);

        Date dateToFormat;
        try {
            //try to get date object from calendar
            dateToFormat = calendar.getTime();
        } catch (IllegalArgumentException e) {
            //getting date object from calendar has failed, use deprecated Date() constructor
            MyDebugger.log("formatTime() illegalArgument exception", e.getMessage());
            dateToFormat = new Date();
            dateToFormat.setHours(hours); //deprecated
            dateToFormat.setMinutes(minutes); //deprecated
        }

        return android.text.format.DateFormat.getTimeFormat(context).format(dateToFormat); //get locale time format and apply it
    }

    @SuppressWarnings("deprecation")
    public static String formatDate(int year, int month, int day) {
        String dateFormat = "d MMMM";
        Calendar calendar = Calendar.getInstance();
        try {
            //try to get current year, and if reminder is for next year show it the date format
            if (year != calendar.get(Calendar.YEAR)) {
                dateFormat += ", y";
            }
        } catch (Exception ex) {
            //not very crucial exception, log it and continue
            MyDebugger.log("formatDate() exception while getting current year", ex.getMessage());
        }
        calendar.clear();
        calendar.set(year, month, day);

        Date date;
        try {
            //try to get date object from calendar
            date = calendar.getTime();
        } catch (IllegalArgumentException e) {
            //getting date object from calendar has failed, use deprecated Date() constructor
            MyDebugger.log("formatDate() illegalArgument exception", e.getMessage());
            date = new Date();
            date.setYear(year);
            date.setMonth(month);
            date.setDate(day);
        }

        return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(date).toLowerCase(); //format date with specific pattern
    }

    public static String getCurrentTimeSQLiteFormatted() {
        return formatDateInSQLiteFormat(getCurrentTime());
    }

    public static long getCurrentTimeAsMillis() {
        return getCurrentTime().getTime();
    }

    public static String formatDateInSQLiteFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        return dateFormat.format(date);
    }

    public static Date parseDateFromSQLiteFormat(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        Date date = null;
        try {
            //trying to parse date from SQLite format
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            //exception while parsing date, log it
            MyDebugger.log("ParseException in parseDateFromSQLiteFormat()", e.getMessage());
            e.printStackTrace();
        }
        return date;
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
