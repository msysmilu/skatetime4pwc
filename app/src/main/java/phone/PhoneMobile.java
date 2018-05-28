package phone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import activities.MainActivity;

/**
 * Created by Emil
 * Deals with Mobile/NetworkData/3G connection
 */
public class PhoneMobile {
    private static final String TAG = "MobileData";

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) MainActivity.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static void setMobileDataEnabled(boolean enabled) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConnectivityManager dataManager;
        dataManager  = getConnectivityManager();
        Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        dataMtd.setAccessible(true);
        dataMtd.invoke(dataManager, enabled);
    }

    // PUBLIC --------------------------------------------------------------------------------------

    public static Boolean enableMobileData(){
        try {
            setMobileDataEnabled(true);
            return true;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return false;
        }
    }

    public static Boolean disableMobileData(){
        try {
            setMobileDataEnabled(false);
            return true;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return false;
        }
    }

    public static Boolean isEnabled(){
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = getConnectivityManager();
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return mobileDataEnabled;
    }

    public static Boolean hasInternet() {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) MainActivity.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) if (ni.isConnected()) haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }
}
