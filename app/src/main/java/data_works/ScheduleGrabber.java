package data_works;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cfg.Cfg;
import estha_helpers.FileToString;
import estha_helpers.Stopwatch;
import internet.UpdateSchedules;
import miluca.skatetime.R;
import skating_schedule.Schedule;

/**
 * Created by Emil
 */
public class ScheduleGrabber {


    public static Schedule getSchedule(Activity activity_context, DateTime justToday) {
        Schedule schedule = null;

        int currentMonth;
        if (null != justToday) currentMonth = justToday.getMonthOfYear(); else  currentMonth = DateTime.now().getMonthOfYear();
        //currentMonth = DateTime.now().getMonthOfYear();
        Log.e("currentMonth", currentMonth + " and " + UpdateSchedules.dataForMonth);
        String updateDate = Cfg.ScheduleData.getUpdateDataDate();

        if (!updateDate.equals("01jan15") && (UpdateSchedules.dataForMonth != 13) && (currentMonth == UpdateSchedules.dataForMonth)){
            String updateFilename = "skating_times_current";
            try {
                Log.e("gettingScheduleData", "Trying Now");
                FileInputStream fin = activity_context.openFileInput(updateFilename);
                Log.e("openedScheduleData", "Opened");

                StringBuilder sb = new StringBuilder();
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                } catch(OutOfMemoryError om){
                    om.printStackTrace();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                String result = sb.toString();

                Log.e("gotScheduleData", result);

                fin.close();
                //scheduleJson = Cfg.ScheduleData.getUpdateDataJson();
                //Log.e("gotScheduleData", cachedSchedule);
                //Gson gson = new Gson();
                //String jsonResult = gson.toJson(result);
                schedule = new Gson().fromJson(result, Schedule.class);
            } catch (Exception e) {
                Log.e("cannotParseCache","");
                e.printStackTrace();
                Toast.makeText(activity_context, "Could not retrieve Updated Results", Toast.LENGTH_LONG).show();
                schedule = getScheduleDataFromRaw(activity_context,justToday);
                schedule.getData().getLocations().getLocation();
            }
        } else {
            schedule = getScheduleDataFromRaw(activity_context,justToday);
            schedule.getData().getLocations().getLocation();

        }

        Log.e("ReloadedSchedule", "MADE IT HERE");
        return schedule;
    }

    private static Schedule getScheduleDataFromRaw(Activity activity_context, DateTime justToday) {
        String scheduleJson = "";
        Schedule rawSchedule = null;
        try {
            Stopwatch stopwatch = new Stopwatch();

            //getting the specific resource file wrt month; this is for faster loading and parsing
            int resource_file;
            if (null == justToday) resource_file = R.raw.skating_times;
            else

                // THIS IS 1 BASED NUMBERING !!!!! i.e. Jan = 1
                switch (justToday.getMonthOfYear()) {
                    case 1:
                        resource_file = R.raw.skating_times_jan;
                        break;
                    case 2:
                        resource_file = R.raw.skating_times_feb;
                        break;
                    case 3:
                        resource_file = R.raw.skating_times_mar;
                        break;
                    case 4:
                        resource_file = R.raw.skating_times_apr;
                        break;
                    case 5:
                        resource_file = R.raw.skating_times_may;
                        break;
                    case 6:
                        resource_file = R.raw.skating_times_jun;
                        break;
                    case 7:
                        resource_file = R.raw.skating_times_jul;
                        break;
                    case 8:
                        resource_file = R.raw.skating_times_aug;
                        break;
                    case 9:
                        resource_file = R.raw.skating_times_sep;
                        break;
                    case 10:
                        resource_file = R.raw.skating_times_oct;
                        break;
                    case 11:
                        resource_file = R.raw.skating_times_nov;
                        break;
                    case 12:
                        resource_file = R.raw.skating_times_dec;
                        break;
                    default:
                        resource_file = R.raw.skating_times;
                        break;
                }

            //loading the resource JSON
            Log.d("Schedule grabber", "Grabbing Schedules NOW");
            InputStream scheduleJsonInputStream = activity_context.getResources().openRawResource(resource_file);

            //stopwatch.split("open raw resource");

            scheduleJson = FileToString.convertStreamToString(scheduleJsonInputStream);
            //stopwatch.split("file to string");

            rawSchedule = new Gson().fromJson(scheduleJson, Schedule.class);
            //stopwatch.split("parse Json");
            stopwatch.end("get Schedule");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawSchedule;
    }


}
