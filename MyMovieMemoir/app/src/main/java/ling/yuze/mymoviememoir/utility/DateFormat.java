package ling.yuze.mymoviememoir.utility;

import java.util.Calendar;

public class DateFormat {
    public static String toDateString(int year, int month, int day) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(year).append("-");
        if (month < 10)
            buffer.append("0").append(month);
        else
            buffer.append(month);
        buffer.append("-");
        if (day < 10)
            buffer.append("0").append(day);
        else
            buffer.append(day);

        return buffer.toString();
    }

    public static String toTimeString(int hour, int minute) {
        StringBuffer buffer = new StringBuffer();
        if (hour < 10)
            buffer.append("0").append(hour);
        else
            buffer.append(hour);
        buffer.append(":");
        if (minute < 10)
            buffer.append("0").append(minute);
        else
            buffer.append(minute);
        return buffer.toString();
    }



    public static int compareDate(String d1, String d2) {
        if (Integer.parseInt(d1.substring(0, 4)) > Integer.parseInt(d2.substring(0, 4)))
            return 1;
        else if (Integer.parseInt(d1.substring(0, 4)) < Integer.parseInt(d2.substring(0, 4)))
            return -1;
        else {
            if (Integer.parseInt(d1.substring(5, 7)) > Integer.parseInt(d2.substring(5, 7)))
                return 1;
            else if (Integer.parseInt(d1.substring(5, 7)) < Integer.parseInt(d2.substring(5, 7)))
                return -1;
            else {
                if (Integer.parseInt(d1.substring(8, 10)) > Integer.parseInt(d2.substring(8, 10)))
                    return 1;
                if (Integer.parseInt(d1.substring(8, 10)) < Integer.parseInt(d2.substring(8, 10)))
                    return -1;
            }
        }
        return 0;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        return toDateString(year, month, day);
    }

    public static String getCurrentDatetime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String date = toDateString(year, month, day);
        String time = toTimeString(hour, minute);

        return date + " " + time;
    }
}
