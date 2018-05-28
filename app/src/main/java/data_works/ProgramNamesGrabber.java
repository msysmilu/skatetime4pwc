package data_works;

import android.app.Activity;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cfg.Cfg;
import estha_helpers.Stopwatch;
import skating_schedule.Schedule;

/**
 * Created by Emil
 */
public class ProgramNamesGrabber {

    //only gets certain array elements defined by the filter text
    public  static ArrayList<String> filterArray(ArrayList<String> arrayToFilter, String filterText){
        ArrayList<String> returnArray = new ArrayList<String>();
        int j = 0;
        for (int i = 0; i< arrayToFilter.size(); i++){
            if (arrayToFilter.get(i).contains(filterText)){
                returnArray.add(j, arrayToFilter.get(i));
                j++;
            }
        }
        //returnArray.get(1);
        return returnArray;
    }

    public static ArrayList<String> getAllActivitiesSetFromJsonOrFromSP(Activity activity_context, String activityType) {

        ArrayList<String> activitiesArray;


        //check SP for cached activities
        activitiesArray = Cfg.ProgramsChoice.getProgramsAvailable(activityType);
        if(activitiesArray.size()>0) {
            Log.d("ProgramNamesGrabber", "Retrieved from cache");
            //return activitiesArray;
            return ProgramNamesGrabber.filterArray(activitiesArray, activityType);
        }

        Log.d("AsapActivity", "Attempting to grab schedules");
        Schedule schedule = ScheduleGrabber.getSchedule(activity_context, null);

        Set<String> activities = new HashSet<>();
        String currentActivityName;
        Stopwatch stopwatch = new Stopwatch();

        for (Iterator<LinkedTreeMap> linkedTreeMapIterator = schedule.getData().getLocations().getLocation().iterator(); linkedTreeMapIterator.hasNext(); ) {
            LinkedTreeMap location = linkedTreeMapIterator.next();

            for (Iterator<LinkedTreeMap> activityIterator = schedule.getData().getSchedules().getActivity().iterator(); activityIterator.hasNext(); ) {
                LinkedTreeMap activity = activityIterator.next();

                // a new activity is retrieved; NAME + TYPE
                //activities.add(activity.get("type") + " " + activity.get("name"));

                // a new activity is retrieved; TYPE
                currentActivityName = activity.get("name").toString().trim();
                if (currentActivityName.contains(activityType))
                    activities.add(activity.get("name").toString().trim());
            }
        }
        stopwatch.end("For loops for getting activities");


        activitiesArray.addAll(activities);
        Collections.sort(activitiesArray);

        //save in SP as cached
        Cfg.ProgramsChoice.setProgramsAvailable(activitiesArray);

        return ProgramNamesGrabber.filterArray(activitiesArray, activityType);
    }
}
