package cfg;

public class CfgFirstRun extends SharedPreferencesManager {

    private static final String SPK_FIRST_RUN_RECORD = "SPK_FIRST_RUN_RECORD_SPK_BOOLEAN";

    public Boolean isFirstRun() {
        // if it's the first run
        return prefs().getBoolean(SPK_FIRST_RUN_RECORD, true);
    }

    public void setFirstRun(Boolean newVal) {
        editor().putBoolean(SPK_FIRST_RUN_RECORD, newVal);
        editor().commit(); 
    }


}
