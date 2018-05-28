package phone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import activities.MainActivity;

/**
 * Created by Emil
 */
public class PhoneWiFi {

    private static WifiManager getWifiManager() {
        WifiManager wifiManager = (WifiManager) MainActivity.getContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
    }

    private static void changeWiFiState(Boolean enabled){
        getWifiManager().setWifiEnabled(enabled);
    }

    // PUBLIC --------------------------------------------------------------------------------------

    public static void enableWiFi(){
        changeWiFiState(true);
    }

    public static void disableWiFi(){
        changeWiFiState(false);
    }

    public static Boolean isEnabled(){
        return getWifiManager().isWifiEnabled();
    }

    public static Boolean hasInternet() {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) MainActivity.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) if (ni.isConnected()) haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }
}
