package data_works;

import android.util.Log;

import java.util.logging.Filter;

/**
 * Created by Emil
 * This class will hold in memory 1 scheduled activity for 1 complex
 * Updated
 * Added hasRentals, cityActivity,complex_desc and complex_type attributes to AsapTimeSlot; Added to12HourTime function;
 * Updated
 * Added complex number
 */
public class AsapTimeSlot implements Comparable{

    public String complex_name;
    public String complex_desc;     //Added description placeholder for future image/description concept
    public String complex_type;     //Added to indicate type or complex (eg. indoor or outdoor)
    public String complex_number;   //Added complex number
    public String activity;
    public String cityActivity;     //Activity will be displayed by city-given name as it is more descriptive than our classification
    public String timeStart;
    public String timeEnd;
    public String day;
    public String month;
    public String id;
    public String lat;
    public String lon;
    public Boolean hasWashroom;
    public Boolean hasChangeRoom;
    public Boolean hasRentals;      //Added flag indicating skate rentals availability
    private long timestampStart;
    private long timestampEnd;
    public long timestampToLeaveAt; // the time to live at to travel and to get there in time

    public AsapTimeSlot(String complex_name, String complex_desc, String complex_type, String complex_number, String activity, String cityActivity, String timeStart, String timeEnd, String day, String month, String id, String lat, String lon, Boolean hasWashroom, Boolean hasChangeRoom, Boolean hasRentals, long timestampStart, long timestampEnd, long timestampToLeaveAt) {
        this.complex_name = complex_name;
        this.complex_desc = complex_desc;
        this.complex_type = complex_type;
        this.complex_number = complex_number;
        this.activity = activity;
        this.cityActivity = cityActivity;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.day = day;
        this.month = month;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.hasWashroom = hasWashroom;
        this.hasChangeRoom = hasChangeRoom;
        this.hasRentals = hasRentals;
        this.setTimestampStart(timestampStart);
        this.setTimestampEnd(timestampEnd);
        this.timestampToLeaveAt = timestampToLeaveAt;
    }

    public AsapTimeSlot() {
    }

    @Override
    public int compareTo(Object o) {

        AsapTimeSlot f = (AsapTimeSlot)o;

        if (timestampToLeaveAt > f.timestampToLeaveAt) {
            return 1;
        }
        else if (timestampToLeaveAt <  f.timestampToLeaveAt) {
            return -1;
        }
        else {
            return 0;
        }

    }

    //returns given Input in 12-hour Format
    public static String to12HourTime(String inputTime){
        inputTime = inputTime.trim();
        String hourString = inputTime.substring(0,inputTime.length()-2);
        String minuteString = inputTime.substring(inputTime.length()-2, inputTime.length());
        int hour = Integer.valueOf(hourString);
        String AMPM = "AM";
        if (hour > 12){
            hour = hour - 12;
            AMPM = "PM";
        }else{
            if (hour == 12) AMPM ="PM";
        }
        return String.valueOf(hour) + ":" + minuteString + " " + AMPM;
    }

    public String getTimePeriodString(){
        String result = "";
        Integer startHour = Integer.parseInt(this.timeStart.substring(0, this.timeStart.length()-2));
        Integer endHour = Integer.parseInt(this.timeEnd.substring(0, this.timeEnd.length()-2));
        if (startHour < 12){
            result = "morning";
            if(endHour >= 12){
                result = result + "afternoon";
            }
            if(endHour > 17){
                result = result + "evening";
            }
        }else if (startHour >= 12 && startHour < 17){
            result = "afternoon";
            if (endHour > 17){
                result = result + "evening";
            }
        }else result = "evening";
        return result;
    }


    public boolean matchesLocation(){
        if (this.complex_type.toLowerCase().contains("indoor")) {
            if (FilterHolder.hasIndoor) return true;
        }else{
            if (FilterHolder.hasOutdoor) return true;
        }
        return false;
    }

    public boolean matchesAmenities(){
        boolean washroomCheck = true;
        boolean changeroomCheck = true;
        boolean rentalsCheck = true;

        if (FilterHolder.hasWashroom){
            washroomCheck = this.hasWashroom;
        }

        if (FilterHolder.hasChangeroom){
            changeroomCheck = this.hasChangeRoom;
        }

        if (FilterHolder.hasRentals){
            rentalsCheck = this.hasRentals;
        }

        return washroomCheck && changeroomCheck && rentalsCheck;
    }

    public boolean matchesTime(){
        String timePeriod = this.getTimePeriodString();
        if (FilterHolder.hasMorning){
            if (timePeriod.contains("morning")) return true;
        }
        if (FilterHolder.hasAfternoon){
            if (timePeriod.contains("afternoon")) return true;
        }
        if (FilterHolder.hasEvening){
            if (timePeriod.contains("evening")) return true;
        }
        return false;
    }

    // not used, just backup
    public int compareTo2(Object o) {

        AsapTimeSlot f = (AsapTimeSlot)o;

        if (getTimestampStart() > f.getTimestampStart()) {
            return 1;
        }
        else if (getTimestampStart() < f.getTimestampStart()) {
            return -1;
        }
        else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return this.complex_name;
    }

    public long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }
}
