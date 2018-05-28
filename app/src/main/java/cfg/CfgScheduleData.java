package cfg;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Handles Schedule data manipulation
 */

public class CfgScheduleData extends SharedPreferencesManager{

    private static final String SPK_SCHEDULE_DATA_MONTH = "update_data_month_SPK";
    private static final String SPK_SCHEDULE_DATA_DATESTAMP = "update_data_datestamp_SPK";
    private static final String SPK_SCHEDULE_DATA_JSON = "update_data_json_SPK";
    private static final String SPK_SCHEDULE_DATA_TOGGLE = "update_data_toggle_SPK";
    private static final String SPK_SCHEDULE_DATA_LASTUPDATE = "update_data_lastupdate_SPK";

    private static final String SCHEDULE_DATA_NODATA = "noData";

    public static void setToggle(Boolean toggle) {
        editor().putBoolean(SPK_SCHEDULE_DATA_TOGGLE, toggle).commit();
    }

    public static Boolean getToggle(){
        Boolean toggle = prefs().getBoolean(SPK_SCHEDULE_DATA_TOGGLE, true);
        return toggle;
    }

    public static void setLatestUpdateAttempt(){
        Date currentDateTime = Calendar.getInstance().getTime();
        SimpleDateFormat outboundFormat = new SimpleDateFormat("ddMMMyy", Locale.CANADA);
        outboundFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate = outboundFormat.format(currentDateTime).toLowerCase();
        editor().putString(SPK_SCHEDULE_DATA_LASTUPDATE, currentDate).commit();
    }

    public static String getLatestUpdateAttempt(){
        String lastUpdate = prefs().getString(SPK_SCHEDULE_DATA_LASTUPDATE, "01jan15");
        return lastUpdate;
    }

    public static int setUpdateData() {
        Date currentDateTime = Calendar.getInstance().getTime();
        SimpleDateFormat outboundFormat = new SimpleDateFormat("ddMMMyy", Locale.CANADA);
        outboundFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.CANADA);
        monthFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat monthFormatNumber = new SimpleDateFormat("M", Locale.CANADA);
        monthFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate = outboundFormat.format(currentDateTime).toLowerCase();
        String currentMonth = monthFormat.format(currentDateTime).toLowerCase();
        int currentMonthNumber = Integer.parseInt(monthFormatNumber.format(currentDateTime));
        editor().putString(SPK_SCHEDULE_DATA_DATESTAMP, currentDate).commit();
        editor().putString(SPK_SCHEDULE_DATA_MONTH, currentMonth).commit();
        //editor().putString(SPK_SCHEDULE_DATA_JSON, updateData).commit();
        return currentMonthNumber;
    }

    public static String getUpdateDataJson(){
        String result = prefs().getString(SPK_SCHEDULE_DATA_JSON, SCHEDULE_DATA_NODATA);
        return result;
    }

    public static String getUpdateDataDate(){
        String storedMonthString = prefs().getString(SPK_SCHEDULE_DATA_MONTH, SCHEDULE_DATA_NODATA);
        Log.e("TAG FOR MONTHS", storedMonthString);
        if (!storedMonthString.equals(SCHEDULE_DATA_NODATA)){
            Date currentDateTime = Calendar.getInstance().getTime();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.CANADA);
            monthFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentMonth = monthFormat.format(currentDateTime).toLowerCase();
            if (!currentMonth.equals(storedMonthString)) return "01jan15";
        }
        String storedDateString = prefs().getString(SPK_SCHEDULE_DATA_DATESTAMP, "01jan15");
        Log.e("TAG FOR DATE", storedDateString);
        return storedDateString;
    }
}
