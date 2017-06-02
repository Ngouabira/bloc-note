package cg.rcksoft.utils;


import java.text.SimpleDateFormat;

/**
 * Created by Ricken BAZOLO on 5/31/2017.
 */

public class DateUtils {

    public static String getHour(long timeStamp) {
        SimpleDateFormat hour = new SimpleDateFormat("HH:mm");
        return hour.format(timeStamp);
    }

    public static String getDate(long timeStamp) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MMM/yyyy");
        return date.format(timeStamp);
    }

    public static String getNumberString(int number) {
        if (number >= 0 && number <= 9) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }
}
