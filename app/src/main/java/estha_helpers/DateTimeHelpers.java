package estha_helpers;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;

/**
 * Created by Emil
 */
public class DateTimeHelpers {

    public static DateTime setTimeToZero(DateTime datetime) {
        DateTime selectedDate = new DateTime(
                datetime.getYear(),
                datetime.getMonthOfYear(), //Value 0 for monthOfYear must be in the range [1,12]
                datetime.getDayOfMonth(),
                0, 0, 0
        );
        return selectedDate;
    }

    public static String getHumanReadableDate_northAmerica(DateTime dt) {
        int day = dt.getDayOfMonth();
        int month = dt.getMonthOfYear();
        int year = dt.getYear();

        return getHumanReadableDate_northAmerica(day, month,year);
    }

    public static String getHumanReadableDate_northAmerica(int day, int month, int year) {
        //response = "" + month + "/" + day + "  " + year;

        String monthName = new DateFormatSymbols().getMonths()[month - 1];
        String response = monthName.substring(0, 3) + " " + day;

        return response;
    }


    public static String getHumanReadableDate_northAmerica_2rows(DateTime dt) {
        int day = dt.getDayOfMonth();
        int month = dt.getMonthOfYear();
        int year = dt.getYear();

        return getHumanReadableDate_northAmerica_2rows(day, month,year);
    }

    public static String getHumanReadableDate_northAmerica_2rows(int day, int month, int year) {
        //response = "" + month + "/" + day + "  " + year;

        String monthName = new DateFormatSymbols().getMonths()[month - 1];
        //String response = day +"\n"+monthName.substring(0, 3);
        String response =  monthName.substring(0, 3) + "\n" + day;

        return response;
    }


}
