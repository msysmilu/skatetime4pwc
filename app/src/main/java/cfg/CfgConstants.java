package cfg;

public class CfgConstants extends SharedPreferencesManager {

    // PRIVATE =====================================================================================

    // PUBLIC ======================================================================================

    /**
     * - version id; used for Shared Preferences
     * - this string is used in shared preferences key in order to invalidate these in case of an update while the app is on google play;
     * - this will help saved skating programs in SP; on the assumption that the json changes then this string will change and force the app to re-read the json;
     * - !!! the "constant" that is initialized with this must not be FINAL because this will be "null"
     * - history:
     * "1_12" - 2017 02 10
     */

    // took this down and using build.gradle version
    //public static final String APP_VERSION_CFG = "v1_12";


    public static final String ACCEPTED_MESSAGE;

    public static final String NOT_ACCEPTED_MESSAGE;
    // messages in the asap list
    public static final String NO_ACTIVITIES_SELECTED;
    public static final String NO_ACTIVITIES_FOUND;
    public static final String NO_ACTIVITIES_OPEN;
    public static final String CLOSED_ACTIVITIES_TODAY;


    public static final String APP_TITLE_IN_ACTION_BAR = "SkateTime T.O.";

    static {
        ACCEPTED_MESSAGE = "acc";
        NOT_ACCEPTED_MESSAGE = "nacc";

        NO_ACTIVITIES_SELECTED = "No activities selected";
        NO_ACTIVITIES_FOUND = "No activities found";
        NO_ACTIVITIES_OPEN = "No activities are still open today.";
        CLOSED_ACTIVITIES_TODAY = "Other activities closed today.";
    }
    public static Boolean D = true; //debug
    public static float MIN_PER_KM = 2F; // [min/km]

    //TODO: review on releases (highly used in tests)
    public static final Boolean COMMUTING_TIME_CONSIDERED_WHEN_SORTING_ACTIVITIES = false;
    public static final Boolean ALSO_SHOW_ACTIVITIES_THAT_WILL_EXPIRE_SOON = false; // only show activities that won't close soon
    public static int MINIMUM_PLAY_TIME_ON_ICE_TO_CONSIDER_A_VIABLE_OPTION = 30; // [min] //switched to 30 to maximize listings

    public static int maxActivityCountToShowInList = 200;

    public static final int DECENT_PERSONAL_LOCATION_ACCURACY = 1000; //[m]

}
