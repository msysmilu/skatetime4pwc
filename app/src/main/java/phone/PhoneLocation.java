package phone;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Emil
 */
public class PhoneLocation {
    private static Location updatedLocation;

    /**
     * Gets the best known location with an accuracy better than 500m (or null if unknown)
     *
     * @param context
     * @param lowestAccuracy
     * @return Location or null
     */
    public static Location getLastKnownBest(Context context, int lowestAccuracy) {
        LocationManager mLocationManager = PhoneGPS.getLocationManager(context);
        Location lastKnownGps = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location lastKnownBest = null;
        if (null != lastKnownGps && null != lastKnownNet &&
                lastKnownGps.getAccuracy() < lowestAccuracy && lastKnownGps.getAccuracy() < lowestAccuracy) {
            if (lastKnownGps.getTime() > lastKnownNet.getTime()) lastKnownBest.set(lastKnownGps);
            if (lastKnownNet.getTime() > lastKnownNet.getTime()) lastKnownBest.set(lastKnownNet);
        } else {
            if (null != lastKnownGps && lastKnownGps.getAccuracy() < lowestAccuracy)
                lastKnownBest.set(lastKnownGps);
            else if (null != lastKnownNet && lastKnownNet.getAccuracy() < lowestAccuracy)
                lastKnownBest.set(lastKnownNet);
        }

        return lastKnownBest;
    }

    public static Location getLastKnownBest_forAsap(Context context) {
        return getUpdatedLocation();
    }

    public static Location getLastKnownBest_acc500(Context context) {
        return getLastKnownBest(context, 500);
    }

    public static Location getLastKnownBest_acc1000(Context context) {
        return getLastKnownBest(context, 1000);
    }

    public static void setUpdatedLocation(Location newLocation) {
        if (newLocation == null) return;
        if (updatedLocation == null) updatedLocation = new Location(newLocation);
        updatedLocation.set(newLocation);
    }

    public static Location getUpdatedLocation() {
        return updatedLocation;
    }
}
