package phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

import activities.MainActivity;

/**
 * Created by Emil
 * Handles the GPS to be On or Off
 */
public class PhoneGPS {

    private static LocationManager getLocationManager() {
        LocationManager locationManager = (LocationManager) MainActivity.getContext().getSystemService(MainActivity.getContext().LOCATION_SERVICE);
        return locationManager;
    }


    // PUBLIC --------------------------------------------------------------------------------------

    public static LocationManager getLocationManager(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager;
    }


    public static Boolean isGpsOn() {
        return (getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public static void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.getContext());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                MainActivity.getContext().startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
