package cfg;

import org.joda.time.DateTime;

/**
 * Handles the SF for dateChosen to be displayed in the app;
 * this can be a timestamp in the day for SkateNow
 * ot a timestamp at 00:00 in one day for show a specific day;
 * [ms]
 */
public class CfgDateChoice extends SharedPreferencesManager {

    private static final String SPK_DATE_CHOSEN = "date_chosen_SPK_timestamp";

    public static void setDateChosen(DateTime dateTime) {
        long timestamp = dateTime.getMillis();
        setDateChosen(timestamp);
    }

    public static void setDateChosen(long timestamp) {
        editor().putLong(SPK_DATE_CHOSEN, timestamp).commit();
    }

    public static void setDateChosenNow() {
        setDateChosen(DateTime.now());
    }

    public static DateTime getDateChosenOrNow() {
        Long timestamp = prefs().getLong(SPK_DATE_CHOSEN, DateTime.now().getMillis());
        return new DateTime(timestamp);
    }

    public static Boolean isDateChosenSkateNow() {
        // because whole days start from 00:00
        return (Cfg.DateChoice.getDateChosenOrNow().getHourOfDay() > 0 || Cfg.DateChoice.getDateChosenOrNow().getMinuteOfHour() > 0);
    }
}
