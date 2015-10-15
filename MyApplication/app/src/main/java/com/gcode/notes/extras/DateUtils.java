package com.gcode.notes.extras;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    //TODO: use user friendly date format for the specific region
    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        return dateToString(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static Date parseString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            MyDebugger.log("DATE PARSE EXCEPTION!");
            e.printStackTrace();
            return null;
        }
    }
}
