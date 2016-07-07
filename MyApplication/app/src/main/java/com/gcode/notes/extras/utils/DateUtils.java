package com.gcode.notes.extras.utils;


import android.content.Context;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    public static final int EXPIRATION_DAYS = 7;
    //TODO: REFACTOR and Optimize memory (simple date format is created more times than needed) AND REDUNDANCY
    private static final String SQL_LITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DISPLAY_FORMAT = "dd-MM-yyyy, HH:mm";

    public static String getExpirationDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, EXPIRATION_DAYS);
        return formatDateInSQLiteFormat(calendar.getTime());
    }

    public static String formatSQLiteDateForDisplay(String sqliteDateString) {
        SimpleDateFormat sqliteSimpleDateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        Date date = getDateFromSQLiteString(sqliteSimpleDateFormat, sqliteDateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //format date
        String formattedDate = formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        //format time
        formattedDate += ", " + formatTime(MyApplication.getAppContext(), calendar);

        return formattedDate;
    }

    public static String formatSQLiteDateForReminder(String sqliteDateString) {
        SimpleDateFormat sqliteSimpleDateFormat = new SimpleDateFormat(SQL_LITE_DATE_FORMAT, Locale.US);
        Date date = getDateFromSQLiteString(sqliteSimpleDateFormat, sqliteDateString);

        String dateFormat = "d MMMM";
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int remindersYear = calendar.get(Calendar.YEAR);
        try {
            //try to get current year, and if reminder is for next year show it the date format
            if (remindersYear != currentYear) {
                dateFormat += " y";
            }
        } catch (Exception ex) {
            //not very crucial exception, log it and continue
            MyLogger.log("formatDate() exception while getting current year", ex.getMessage());
        }
        dateFormat += ", HH:mm";

        return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(date).toLowerCase(); //format date with specific pattern
    }

    /**
     * @param context   app context used for getting preferred time format
     * @param hourOfDay Calendar.HOUR_OF_DAY
     * @param minutes   Calendar.MINUTE
     * @return locale formatted time
     */
    public static String formatTime(Context context, int hourOfDay, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minutes);
        return formatTime(context, calendar);
    }

    /**
     * @param context  app context used for getting preferred time format
     * @param calendar Calendar instance set to target time
     * @return locale formatted time
     */
    @SuppressWarnings("deprecation")
    public static String formatTime(Context context, Calendar calendar) {
        Date dateToFormat;
        try {
            //try to get date object from calendar
            dateToFormat = calendar.getTime();
        } catch (IllegalArgumentException e) {
            //getting date object from calendar has failed, use deprecated Date() constructor
            MyLogger.log("formatTime() illegalArgument exception", e.getMessage());
            dateToFormat = new Date();
            dateToFormat.setHours(calendar.get(Calendar.HOUR_OF_DAY)); //deprecated
            dateToFormat.setMinutes(calendar.get(Calendar.MINUTE)); //deprecated
        }

        return android.text.format.DateFormat.getTimeFormat(context).format(dateToFormat); //get locale time format and apply it
    }

    /**
     * @param year  Calendar.YEAR
     * @param month Calendar.MONTH_OF_YEAR
     * @param day   Calendar.DAY_OF_MONTH
     * @return locale formatted date (handling yesterday, today and tomorrow)
     */
    @SuppressWarnings("deprecation")
    public static String formatDate(int year, int month, int day) {
        String dateFormat = "d MMMM";
        Calendar calendar = Calendar.getInstance();
        try {
            int currentYear = calendar.get(Calendar.YEAR);
            if (month == calendar.get(Calendar.MONTH) && year == currentYear) {
                //the desired date is in the same month and year, check for yesterday, today or tommorrow
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                Context context = MyApplication.getAppContext();
                if (currentDay == day) {
                    //today
                    return context.getString(R.string.today);
                } else if (day == currentDay + 1) {
                    //tomorrow
                    return context.getString(R.string.tomorrow);
                } else if (day == currentDay - 1) {
                    //yesterday
                    return context.getString(R.string.yesterday);
                }
            }
            if (year != currentYear) {
                //if reminder is not for current year, add year to the date format
                dateFormat += ", y";
            }
        } catch (Exception ex) {
            //not very crucial exception, log it and continue
            MyLogger.log("DateUtils#formatDate() exception while getting current date or year", ex.getMessage());
        }
        calendar.clear();
        calendar.set(year, month, day);

        Date date;
        try {
            //try to get date object from calendar
            date = calendar.getTime();
        } catch (IllegalArgumentException e) {
            //getting date object from calendar has failed, use deprecated Date() constructor
            MyLogger.log("formatDate() illegalArgument exception", e.getMessage());
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
            MyLogger.log("ParseException in parseDateFromSQLiteFormat()", e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    private static Date getDateFromSQLiteString(SimpleDateFormat sqliteDateFormat, String sqliteDateString) {
        Date date;
        try {
            //trying to parse date in database with SQL_LITE_DATE_FORMAT
            date = sqliteDateFormat.parse(sqliteDateString);
        } catch (ParseException e) {
            //parsing has failed, create new dummy date
            e.printStackTrace();
            MyLogger.log("DUMMY CREATED: formatSQLiteDateForDisplay", e.getMessage());
            date = new Date();
        }
        return date;
    }

    private static Date getCurrentTime() {
        Date date;
        try {
            date = Calendar.getInstance().getTime();
        } catch (IllegalArgumentException ex) {
            MyLogger.log("getCurrentTimeSQLiteFormatted()", ex.getMessage());
            date = new Date();
        }
        return date;
    }
}
