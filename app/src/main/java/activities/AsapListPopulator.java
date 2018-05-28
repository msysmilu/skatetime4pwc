package activities;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.ListView;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Set;

import cfg.Cfg;
import data_works.AsapTimeSlot;
import data_works.AsapTimeSlotGrabber;
import data_works.FilterHolder;


/**
 * Created by Emil
 * Updated
 * Added 12Hour conversion call and changed display data for Activity
 * Updated
 * Added additional fields required for AsapRow Popup
 */
public class AsapListPopulator {

    public static int numberOfClosedActivities;

    public static void populateAsapList(ListView listView, Context context, Location usersBestLocationSoFar) {
        Set<AsapTimeSlot> asapTimeSlots = AsapTimeSlotGrabber.getAsapTimeSlotSet(AsapActivity.mainActivity, Cfg.DateChoice.getDateChosenOrNow());

        // this will keep
        numberOfClosedActivities = 0;

        // populate it
        AsapRow[] asapRows = null;
        FilterHolder.loadAllFiltersFromSharedPrefs();
        for (AsapTimeSlot asapTimeSlot : asapTimeSlots) {

            // if this AsapRow (time slot) is included in the selected activities
            if (Cfg.ProgramsChoice.getProgramsChosen().contains(asapTimeSlot.activity) && asapTimeSlot.matchesLocation() && asapTimeSlot.matchesAmenities() && asapTimeSlot.matchesTime()) {
                AsapRow asapRow = new AsapRow();

                asapRow.setActivity(asapTimeSlot.cityActivity);     //01-Feb-15: displays city-given name as it contains additional details (age, etc..)

                asapRow.setComplex_name(asapTimeSlot.complex_name);
                asapRow.setComplex_desc(asapTimeSlot.complex_desc);
                asapRow.setComplex_number(asapTimeSlot.complex_number);
                asapRow.setComplex_type(asapTimeSlot.complex_type);
                asapRow.setHasWashroom(asapTimeSlot.hasWashroom);
                asapRow.setHasChangeroom(asapTimeSlot.hasChangeRoom);
                asapRow.setHasRentals(asapTimeSlot.hasRentals);
                asapRow.setLat(asapTimeSlot.lat);
                asapRow.setLon(asapTimeSlot.lon);
                // setDistance_km/label ========================================================

                if (null == usersBestLocationSoFar) {
                    asapRow.setDistance_km_label("-");
                    asapRow.setDistance_km(0);
                } else {
                    float[] dist = new float[1];
                    Location.distanceBetween(
                            usersBestLocationSoFar.getLatitude(),
                            usersBestLocationSoFar.getLongitude(),
                            //ensuring latitude is positive (northern hemisphere)
                            (Double.parseDouble(asapTimeSlot.lat) > 0 ? Double.parseDouble(asapTimeSlot.lat) : -Double.parseDouble(asapTimeSlot.lat)),
                            //ensuring longitude is negative (western hemisphere)
                            (Double.parseDouble(asapTimeSlot.lon) < 0 ? Double.parseDouble(asapTimeSlot.lon) : -Double.parseDouble(asapTimeSlot.lon)),
                            dist
                    );
                    asapRow.setDistance_km_label((Math.round(dist[0]) / 1000L) + " km"); // 0 decimal
                    asapRow.setDistance_km(Math.round(dist[0]) / 100L / 10F);
                }

                // setStarts_in_min ============================================================

                int minutesUntilItEnds = (int) ((asapTimeSlot.getTimestampEnd() - System.currentTimeMillis()) / 1000F / 60F);
                int minutesUntilItStarts = (int) ((asapTimeSlot.getTimestampStart() - System.currentTimeMillis()) / 1000F / 60F);

                int commutingTime = Cfg.Constants.COMMUTING_TIME_CONSIDERED_WHEN_SORTING_ACTIVITIES ? Math.round(asapRow.getDistance_km() * Cfg.Constants.MIN_PER_KM) : 0; // [min]


                //wrt the maximum between the actual starting time from now and commuting time
                float asapTime = Math.max(minutesUntilItStarts, commutingTime); //getting the maximum asap time from: commuting time or time until it starts
                asapRow.setStarts_in_min(asapTime);

                // viability ====================================================================
                //if the activity doesn't expire soon (if it's worth going to this one for at least 60 min of play)
                Boolean isViable = minutesUntilItEnds > (commutingTime + Cfg.Constants.MINIMUM_PLAY_TIME_ON_ICE_TO_CONSIDER_A_VIABLE_OPTION);


                // setStarts_in_min_label ======================================================

                //human readable timestamp
                DateTime startTimeThis = new DateTime(asapTimeSlot.getTimestampStart(), DateTimeZone.forID("America/Montreal"));
                DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm MM/dd/yy");
                asapRow.setStarts_in_min_label(dtf.print(startTimeThis));

                // asap skating time (how soon can one skate)
                if (asapTime < 0) asapTime = 0;
                String daysLabel = (((asapTime / 60F / 24F) >= 1) ? (int) Math.floor(asapTime / 60F / 24F) + "d" : "");
                String hoursLabel = ((asapTime / 60F) >= 1) ? (int) (Math.floor(asapTime / 60F) % 24F) + "h" : "";
                String minuteSimbol = hoursLabel.equals("") ? "min" : "'";
                String minutesLabel = (asapTime >= 1) ? (int) (asapTime % 60F) + minuteSimbol : "";
//                String minutesLabel = ((Math.round(asapTime % 60F) < 10) && ((asapTime / 60F) > 1) ? "0" : "")// leading zero for when minutes are <10 and there is a hour in front
//                        + (Math.round(asapTime % 60F) + "'");// shows "20'", minutes
                if (!daysLabel.equals(""))
                    minutesLabel = "";//if there are days to show never mind minutes
                asapRow.setStarts_in_min_label("⌛ " + daysLabel + hoursLabel + minutesLabel);

                if (asapTime == 0)
                    asapRow.setStarts_in_min_label("⌚ Open"); // if it has already closed
                if (!isViable)
                    asapRow.setStarts_in_min_label("⚡ Ending"); // if it has already closed
                if (asapTimeSlot.getTimestampEnd() < System.currentTimeMillis())
                    asapRow.setStarts_in_min_label("❌ Closed"); // if it has already closed
                //asapRow.setStarts_in_min_label("⌚ " + asapRow.getStarts_in_min_label());

                // set the label the exact float number; used for DEBUG
                // asapRow.setStarts_in_min_label(Float.toString(asapTime));

                // setTimetable ================================================================

                // asapRow.setTimetable(asapTimeSlot.timeStart + " - " + asapTimeSlot.timeEnd); //old version
                asapRow.setTimetable(AsapTimeSlot.to12HourTime(asapTimeSlot.timeStart) + " - " + AsapTimeSlot.to12HourTime(asapTimeSlot.timeEnd)); //change input to 12 Hours

                // insert if ok ================================================================

                // the viability check is only applicable if the user chose Skate Now
                if (!Cfg.DateChoice.isDateChosenSkateNow() || Cfg.Constants.ALSO_SHOW_ACTIVITIES_THAT_WILL_EXPIRE_SOON || isViable) {
                    asapRows = ArrayUtils.add(asapRows, asapRow);
                } else {
                    if (Cfg.DateChoice.isDateChosenSkateNow()) numberOfClosedActivities++;
                }
            }
        }

        // -----------------------------------------------------------------------------------------
        // sorting, making new list that is with unique things
        AsapRow[] asapRowsLimitedNumber = null;
        if (null != asapRows && asapRows.length > 0) {
            //for(AsapRow ar: asapRows) System.out.println("[ " + ar.getDistance_km_label()+", "+ar.getTimetable()+"] ");
            Arrays.sort(asapRows, AsapRowComparators.comparatorWrtDistanceAndThenStartsInAndThenName);

            int maxUniqueActivityCountToShowInList = Cfg.Constants.maxActivityCountToShowInList; // this is the limit
            // taking the first X
            for (int i = 0; i < maxUniqueActivityCountToShowInList; i++) {
                //if there are enough elements to use and if those elements are valid
                if (i < asapRows.length && asapRows[i] != null) {

                    //checking for duplicates (unless if the check in list is empty / null)
                    Boolean notInList = true;
                    if (null != asapRowsLimitedNumber) {
                        for (int j = 0; j < asapRowsLimitedNumber.length; j++) {
                            if (asapRows[i].getComplex_name().compareToIgnoreCase(asapRowsLimitedNumber[j].getComplex_name()) == 0
                                    && asapRows[i].getActivity().compareToIgnoreCase(asapRowsLimitedNumber[j].getActivity()) == 0
                                    && asapRows[i].getDistance_km_label().compareToIgnoreCase(asapRowsLimitedNumber[j].getDistance_km_label()) == 0
                                    && asapRows[i].getStarts_in_min_label().compareToIgnoreCase(asapRowsLimitedNumber[j].getStarts_in_min_label()) == 0
                                    && asapRows[i].getTimetable().compareToIgnoreCase(asapRowsLimitedNumber[j].getTimetable()) == 0
                                    ) {
                                notInList = false;

                                // if we need to show a list of 20 and there is 1 duplicate, we will search in 21
                                maxUniqueActivityCountToShowInList++;
                            }
                        }
                    }
                    // once determined this is unique it will be inserted towards display;
                    if (notInList) {
                        asapRowsLimitedNumber = ArrayUtils.add(asapRowsLimitedNumber, asapRows[i]);
                    }

                    //set complex visibility for rows with same name and set row color
                    if ((i>0) && (!asapRows[i-1].getComplex_name().equals("")) && (asapRows[i].getComplex_name().equals(asapRows[i-1].getComplex_name()))){
                        Log.d("comparingListItems", asapRows[i].getComplex_name() + asapRows[i-1].getComplex_name());
                        asapRows[i].setComplexVisibility(false);
                        asapRows[i].setIsWhite(asapRows[i-1].isWhite());
                    }else{
                        asapRows[i].setComplexVisibility(true);
                        if (i>0){
                            asapRows[i].setIsWhite(!(asapRows[i-1].isWhite()));
                        }
                    }
                }
            }

            // put a message with the list with the hidden activities
            if (numberOfClosedActivities > 0) {
                AsapRow alsoEntryWithClosedActivities = new AsapRow();
                alsoEntryWithClosedActivities.setComplex_name(Cfg.Constants.CLOSED_ACTIVITIES_TODAY);
                alsoEntryWithClosedActivities.setActivity("There were also " + numberOfClosedActivities + " activities that closed or will close soon.");
                asapRowsLimitedNumber = ArrayUtils.add(asapRowsLimitedNumber, alsoEntryWithClosedActivities);
            }

        } else {
            AsapRow noEntriesMessage = new AsapRow();

            // message
            if (0 == Cfg.ProgramsChoice.getProgramsChosen().size()) {
                noEntriesMessage.setComplex_name(Cfg.Constants.NO_ACTIVITIES_SELECTED);
                noEntriesMessage.setActivity("Use the Skate icon above to select activities");
            } else {
                if (numberOfClosedActivities == 0) {
                    noEntriesMessage.setComplex_name(Cfg.Constants.NO_ACTIVITIES_FOUND);
                    noEntriesMessage.setActivity("Try selecting a different day or more activities.\nAlso try using less filters for your results.");
                } else {
                    noEntriesMessage.setComplex_name(Cfg.Constants.NO_ACTIVITIES_OPEN);
                    noEntriesMessage.setActivity("There were " + numberOfClosedActivities + " activities.\nThey're all closed or will close soon.");
                }
            }

            asapRowsLimitedNumber = ArrayUtils.add(asapRowsLimitedNumber, noEntriesMessage);
        }

        // populating the list ---------------------------------------------------------------------

        listView.setAdapter(new AsapRowAdapter(context, asapRowsLimitedNumber));
    }

}
