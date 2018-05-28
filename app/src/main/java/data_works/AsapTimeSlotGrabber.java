package data_works;

import android.app.Activity;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import estha_helpers.Stopwatch;
import internet.UpdateSchedules;
import skating_schedule.Schedule;

/**
 * Created by Emil
 * Updated
 * Grabbing additional attributes hasRentals, cityActivity,complex_desc and complex_type. Updated month string case statement
 */
public class AsapTimeSlotGrabber {

    public static Set<AsapTimeSlotPerDay> cachedAsapTimeSlotsPerDay;

    static class AsapTimeSlotPerDay {
        // this class is meant to save the asap time slots for each day;
        Set<AsapTimeSlot> asapTimeSlots;
        DateTime dateTime;

        AsapTimeSlotPerDay(DateTime dateTime, Set<AsapTimeSlot> asapTimeSlots) {
            this.dateTime = dateTime;
            this.asapTimeSlots = asapTimeSlots;
        }
    }


    private static Set<AsapTimeSlot> getAsapTimeSlotFromCache(DateTime searchedDateTime) {
        if (null == cachedAsapTimeSlotsPerDay) cachedAsapTimeSlotsPerDay = new HashSet<>();
        for (AsapTimeSlotPerDay asapTimeSlotPerDay : cachedAsapTimeSlotsPerDay) {
            if (asapTimeSlotPerDay.dateTime.equals(searchedDateTime))
                return asapTimeSlotPerDay.asapTimeSlots;
        }
        return null;
    }


    public static Set<AsapTimeSlot> getAsapTimeSlotSet(Activity activity_context, DateTime chosenDateTime) {
        if (null != chosenDateTime) {
            //ignoring hour and minute from chosen date
            chosenDateTime = new DateTime(chosenDateTime.getYear(), chosenDateTime.getMonthOfYear(), chosenDateTime.getDayOfMonth(), 0, 0);

            //caching method
            if ((null != getAsapTimeSlotFromCache(chosenDateTime)) && (UpdateSchedules.isUpdated)) { /*08-10-15 Added check for update of cache schedules*/
                UpdateSchedules.isUpdated = false;
                return getAsapTimeSlotFromCache(chosenDateTime);
            }
        }

        Log.d("AsapActivity", "Attempting to grab schedules");
        Schedule schedule = ScheduleGrabber.getSchedule(activity_context, chosenDateTime);

        Set<AsapTimeSlot> asapTimeSlots = new HashSet<>();

        Stopwatch stopwatch = new Stopwatch();

        for (Iterator<LinkedTreeMap> linkedTreeMapIterator = schedule.getData().getLocations().getLocation().iterator(); linkedTreeMapIterator.hasNext(); ) {
            LinkedTreeMap location = linkedTreeMapIterator.next();
//            location.get("id");
//            location.get("name");
//            location.get("desc");
//            location.get("type");
            LinkedTreeMap coordinates = (LinkedTreeMap) location.get("coordinates");
            LinkedTreeMap facilityFeatures = (LinkedTreeMap) location.get("facilityFeatures");
            if (location != null)
                for (Iterator<LinkedTreeMap> activityIterator = schedule.getData().getSchedules().getActivity().iterator(); activityIterator.hasNext(); ) {
                    LinkedTreeMap activity = activityIterator.next();
//                activity.get("type");
//                activity.get("name");

                    ArrayList months = new ArrayList();
                    try {
                        months = (ArrayList) activity.get("month");
                    } catch (Exception e) {
                        Log.d("No activity this month", activity.get("name").toString());
                    }

                    for (Iterator monthsIterator = months.iterator(); monthsIterator.hasNext(); ) {
                        LinkedTreeMap month = (LinkedTreeMap) monthsIterator.next();
//                    month.get("id"); // Jan
                        if (month != null)
                            if (null != chosenDateTime && getMonthNumberFromName((String) month.get("id")) == chosenDateTime.getMonthOfYear()) {
                                ArrayList days = (ArrayList) month.get("day");
                                if (days != null)
                                    for (Iterator daysIterator = days.iterator(); daysIterator.hasNext(); ) {
                                        LinkedTreeMap day = (LinkedTreeMap) daysIterator.next();
//                            day.get("id"); // 01
                                        if (null != chosenDateTime && Integer.parseInt(day.get("id").toString()) == chosenDateTime.getDayOfMonth()) {
                                            LinkedTreeMap dailySchedule;
                                            try {
                                                ArrayList dailySchedules = (ArrayList) day.get("scheduleEntry");
                                                //Log.d("",day.get("id").toString());
                                                for (Iterator dailySchedulesIterator = dailySchedules.iterator(); dailySchedulesIterator.hasNext(); ) {
                                                    dailySchedule = (LinkedTreeMap) dailySchedulesIterator.next();

                                                    //only considering schedule if it is FOR a specific complex
                                                    String complexId = location.get("id").toString().trim();
                                                    String dayScheduleId = dailySchedule.get("id").toString().trim();
                                                    if (complexId.equalsIgnoreCase(dayScheduleId)) {

                                                        dailySchedule.get("id"); // 31
                                                        dailySchedule.get("timeStart"); // 10
                                                        dailySchedule.get("timeEnd"); // 22
                                                        dailySchedule.get("realName");

                                                        AsapTimeSlot ats = new AsapTimeSlot();
                                                        ats.complex_name = (String) location.get("name");
                                                        ats.complex_desc = (String) location.get("desc");
                                                        ats.complex_type = (String) location.get("type");
                                                        ats.complex_number = (String) location.get("phone");
                                                        //ats.activity = (String) activity.get("type") + " " + activity.get("name");
                                                        ats.activity = activity.get("name").toString().trim();
                                                        ats.cityActivity = (String) dailySchedule.get("realName"); //city-named Activity
                                                        ats.day = (String) day.get("id");
                                                        ats.month = (String) month.get("id");
                                                        ats.timeStart = (String) dailySchedule.get("timeStart");
                                                        ats.timeEnd = (String) dailySchedule.get("timeEnd");
                                                        ats.id = complexId;
                                                        ats.lat = coordinates.get("lat").toString();
                                                        ats.lon = coordinates.get("lng").toString();
                                                        ats.hasChangeRoom = facilityFeatures.get("hasChangeRoom").toString().equals("yes");
                                                        ats.hasWashroom = facilityFeatures.get("hasWashroom").toString().equals("yes");
                                                        ats.hasRentals = facilityFeatures.get("hasRentals").toString().equals("yes");
                                                        ats.setTimestampStart((new DateTime(
                                                                getCalendarYearFromSeasonYear(getMonthNumberFromName(ats.month), chosenDateTime),
                                                                getMonthNumberFromName(ats.month),
                                                                Integer.parseInt(ats.day),
                                                                getHourFromJsonTimeText(ats.timeStart),
                                                                getMinutesFromJsonTimeText(ats.timeStart)
                                                                //, DateTimeZone.forID("America/Montreal")
                                                        ).getMillis()));
                                                        ats.setTimestampEnd((new DateTime(
                                                                getCalendarYearFromSeasonYear(getMonthNumberFromName(ats.month), chosenDateTime),
                                                                getMonthNumberFromName(ats.month),
                                                                Integer.parseInt(ats.day),
                                                                getHourFromJsonTimeText(ats.timeEnd),
                                                                getMinutesFromJsonTimeText(ats.timeEnd)
                                                                //,DateTimeZone.forID("America/Montreal")
                                                        ).getMillis()));
                                                        ats.timestampToLeaveAt = ats.getTimestampStart();

                                                        asapTimeSlots.add(ats);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.d("Add time slot", e.toString());
                                                // there is no schedule this day
                                            }
                                        }
                                    }
                            }
                    }
                }
        }

        stopwatch.end("For loops");

        cachedAsapTimeSlotsPerDay.add(new
                        AsapTimeSlotPerDay(chosenDateTime, asapTimeSlots)
        );

        return asapTimeSlots;
    }


    public static int getMonthNumberFromName(String monthName) {
        int startsFrom = 1;
        switch (monthName.trim().toLowerCase()) {
            case "jan":
                return 0 + startsFrom;
            case "feb":
                return 1 + startsFrom;
            case "mar":
                return 2 + startsFrom;
            case "apr":
                return 3 + startsFrom;
            case "may":
                return 4 + startsFrom;
            case "jun":
                return 5 + startsFrom;
            case "jul":
                return 6 + startsFrom;
            case "aug":
                return 7 + startsFrom;
            case "sep":
                return 8 + startsFrom;
            case "oct":
                return 9 + startsFrom;
            case "nov":
                return 10 + startsFrom;
            case "dec":
                return 11 + startsFrom;
        }
        return 0;
    }

    //returns the calendar year from the "season year" aka a hardcoded "season 2017/2018"
    public static int getCalendarYearFromSeasonYear(int scheduledMonth, DateTime dt) {
        //DateTime dt = DateTime.now();

        // getMonthOfYear is 1 based
        // ----currentMonth-----------scheduledIn----(months)
        //       12 (2017)               12                    =>     2017  =>  0
        //       12 (2017)               01                    =>     2018  => +1
        //       01 (2018)               12                    =>     2017  => -1
        //       01 (2018)               01                    =>     2018  =>  0
        if (dt.getMonthOfYear() >= 6 && scheduledMonth >= 6) return dt.getYear();
        if (dt.getMonthOfYear() <= 6 && scheduledMonth <= 6) return dt.getYear();
        if (dt.getMonthOfYear() <= 6 && scheduledMonth >= 6) return dt.getYear() - 1;
        if (dt.getMonthOfYear() >= 6 && scheduledMonth <= 6) return dt.getYear() + 1;
        return 0;
    }

    public static int getHourFromJsonTimeText(String timeText) {
        timeText = timeText.trim();
        if (timeText.length() == 3) return Integer.parseInt(timeText.substring(0, 1).trim());
        if (timeText.length() == 4) return Integer.parseInt(timeText.substring(0, 2).trim());
        return 0;
    }

    public static int getMinutesFromJsonTimeText(String timeText) {
        //Log.d("",timeText);
        timeText = timeText.trim();
        if (timeText.length() == 3) return Integer.parseInt(timeText.substring(1, 3).trim());
        if (timeText.length() == 4) return Integer.parseInt(timeText.substring(2, 4).trim());
        return 0;
    }
}
