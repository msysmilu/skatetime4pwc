package estha_helpers;

import android.location.Location;

import activities.AsapActivity;
import cfg.Cfg;

/**
 * Created by Emil
 */
public class LocationHelper {

    public static final int maxTimeToWait_ms = 5000;
    private static Location location;
    static long startTime;

    public static void findAndSaveLocation(){

        startTime = System.currentTimeMillis();

        LegacyLastLocationFinder legacyLastLocationFinder = new LegacyLastLocationFinder(AsapActivity.mainContext);
        setLocation(legacyLastLocationFinder.getLastBestLocation(1000, 1));
        if(null == getLocation()) setLocation(legacyLastLocationFinder.getLastBestLocation(3000, 3));
        if(null == getLocation()) setLocation(legacyLastLocationFinder.getLastBestLocation(10000, 3));
        if(null == getLocation()) setLocation(legacyLastLocationFinder.getLastBestLocation(30000, 3));
        if (Cfg.Constants.D) System.out.println("total time in MILLISECONDS: " + (System.currentTimeMillis() - startTime));
    }

    public static void findAndSaveLocationOnThread(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                findAndSaveLocation();
            }
        });
        thread.start();
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        LocationHelper.location = location;
    }

    public static Boolean isTimeout(){
        return (System.currentTimeMillis() - startTime) > maxTimeToWait_ms;
    }
}
