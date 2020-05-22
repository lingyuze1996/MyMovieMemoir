package ling.yuze.mymoviememoir.utility;

public class DateFormat {
    public static String toString(int year, int month, int day) {
        String s = year + "-";
        if (month < 10)
            s += "0" + month;
        else
            s += month;
        s += "-";
        if (day < 10)
            s += "0" + day;
        else
            s += day;
        s += "T00:00:00+10:00";
        return s;
    }
}
