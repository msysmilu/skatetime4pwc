package cfg;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import miluca.skatetime.BuildConfig;

public class CfgProgramsChoice extends SharedPreferencesManager {

    private static final String SPK_PROGRAMS_CHOSEN = "programs_chosen_SPK_list";
    private static String SPK_PROGRAMS_AVAILABLE = "programs_available_SPK_list_" + BuildConfig.VERSION_CODE; // BuildConfig.VERSION_CODE = 1 or 2 or 3 etc

    // WITH JSON ===================================================================================


    private static void setArrayListInSP(ArrayList<String> values, String key) {
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor().putString(key, a.toString());
        } else {
            editor().putString(key, "[]");
        }
        editor().commit();
    }

    private static ArrayList<String> getArrayListInSP(String key) {
        String json = prefs().getString(key, null);
        ArrayList<String> urls = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    // PUBLIC ======================================================================================

    public static void setProgramsChosen(ArrayList<String> values) {
        setArrayListInSP(values,SPK_PROGRAMS_CHOSEN);
    }

    public static ArrayList<String> getProgramsChosen() {
        return getArrayListInSP(SPK_PROGRAMS_CHOSEN);
    }

    public static void setProgramsAvailable(ArrayList<String> values) {
        setArrayListInSP(values,SPK_PROGRAMS_AVAILABLE);
    }

    public static ArrayList<String> getProgramsAvailable(String activityType) {
        return getArrayListInSP(SPK_PROGRAMS_AVAILABLE);
    }

}
