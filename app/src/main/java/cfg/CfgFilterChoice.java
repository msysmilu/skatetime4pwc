package cfg;

/**
 * Handles the filter values
 */
public class CfgFilterChoice extends SharedPreferencesManager {

    public static void setFilterChosen(String key, boolean value) {
        editor().putBoolean(key, value).commit();
    }

    public static boolean getFilter (String key){
        boolean value;
        if(key.toLowerCase().contains("washroom") || key.toLowerCase().contains("changeroom") || key.toLowerCase().contains("rentals")){
            value = prefs().getBoolean(key, false);
        }else{
            value = prefs().getBoolean(key, true);
        }
        return value;
    }
}
