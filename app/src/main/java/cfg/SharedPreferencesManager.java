package cfg;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import activities.ActivityContext;


public class SharedPreferencesManager {

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;


    protected static SharedPreferences prefs() {
        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActivityContext.getContext().getApplicationContext());
        return mSharedPreferences;
    }


    protected static SharedPreferences.Editor editor() {
        if (mEditor == null) {
            mEditor = prefs().edit();
        }
        return mEditor;
    }
}
