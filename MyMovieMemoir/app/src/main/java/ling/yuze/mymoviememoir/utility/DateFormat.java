package ling.yuze.mymoviememoir.utility;

public class DateFormat {
    public static String toCompleteString(int year, int month, int day) {
        String dateString = toDateString(year, month, day);
        String completeString = dateString + "T00:00:00+10:00";
        return completeString;
    }

    public static String toTimeString(int hour, int minute) {
        StringBuffer buffer = new StringBuffer("T");
        if (hour < 10)
            buffer.append("0").append(hour);
        else
            buffer.append(hour);
        buffer.append(":");
        if (minute < 10)
            buffer.append("0").append(minute);
        else
            buffer.append(minute);
        buffer.append(":00+10:00");

        return buffer.toString();
    }

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
}
