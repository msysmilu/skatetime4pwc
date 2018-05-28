/**
 * Added additional logic for filters
 */

package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cfg.Cfg;
import dialogs.FeedbackDialog;
import dialogs.PickDateDialog;
import estha_helpers.BadgeUtils;
import estha_helpers.DateTimeHelpers;
import internet.UpdateSchedules;
import miluca.skatetime.R;
import phone.PhoneGPS;
import phone.PhoneLocation;


public class AsapActivity extends ActionBarActivity {

    public static Activity mainActivity;
    public static Context mainContext;
    public static AsapActivity instance;
    static Menu optionsMenu;

    // the three buttons on the title menu for Badges or Items
    LayerDrawable badge_choose_programs;
    LayerDrawable badge_choose_date;
    LayerDrawable badge_choose_filter;
    //MenuItem action_choose_date;

    UpdateSchedules updateSchedule;

    public static AsapActivityUi ui;

    public static GoogleApiClient mGoogleApiClient; //used for google play services and location
    // LIFETIME ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(Cfg.Constants.APP_TITLE_IN_ACTION_BAR);
        setTitle(Cfg.Constants.APP_TITLE_IN_ACTION_BAR);  // provide compatibility to all the versions
        setContentView(R.layout.activity_asap);

        //save the activity and context
        mainActivity = this;
        mainContext = this;
        ActivityContext.setContext(this);
        instance = this;

        ui = new AsapActivityUi();

        buildGoogleApiClient();

        //update ChosenDate in case it's yesterday;
        updateChosenDateInCaseItsOlderThanToday();

        }

    private void updateChosenDateInCaseItsOlderThanToday() {
        if (Cfg.DateChoice.getDateChosenOrNow().getYear() < DateTime.now().getYear() ||
                Cfg.DateChoice.getDateChosenOrNow().getMonthOfYear() < DateTime.now().getMonthOfYear() ||
                Cfg.DateChoice.getDateChosenOrNow().getDayOfMonth() < DateTime.now().getDayOfMonth())
            Cfg.DateChoice.setDateChosenNow(); // just right for Skate Now
    }

    @Override
    public void onStart() {
        super.onStart();
        ui.addEventListeners();
        // !!! BUG this was resetting the date chosen at the start of the app
        //update time chosen at the start of the app
//        if (Cfg.DateChoice.isDateChosenSkateNow())
//            Cfg.DateChoice.setDateChosenNow(); //skateNow
//        else {
//            DateTime todayAt00 = DateTime.now();
//            todayAt00 = DateTimeHelpers.setTimeToZero(todayAt00);
//            Cfg.DateChoice.setDateChosen(todayAt00);
//
        // on the first run select all activities
        if (Cfg.FirstRun.isFirstRun()) {
            ProgramsActivity.updateAsAllCheckedProgramsInPrefs(mainActivity);
            Cfg.FirstRun.setFirstRun(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ui.populateElementsFromSharedPreferences();
        //ui.hideLoadingProgressBarAndShowContent();

        //populate list
        //ui.populateAsapList();
        ui.populateAsapListAfterGettingLocation(); //includes ui.populateAsapList()

        //populate badges in actions in menu buttons
        if (null != badge_choose_programs)
            BadgeUtils.setBadgeCountPrograms(this, badge_choose_programs, Cfg.ProgramsChoice.getProgramsChosen().size());
        setBadgeCountDateWrtDateChosen();

        ui.changeOptionMenuCalendarIconWrtToDateChosen();
        ui.updateDisplayDateTextViewWrtToDateChosen();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ui.removeEventListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asap, menu);
        optionsMenu = menu;

        // BADGES ==================================================================================
        // Get the notifications MenuItem and its LayerDrawable (layer-list)
        // Note to self: this pattern is
        badge_choose_programs = (LayerDrawable) menu.findItem(R.id.action_badge_programs).getIcon();
        badge_choose_date = (LayerDrawable) menu.findItem(R.id.action_badge_date).getIcon();
        badge_choose_filter = (LayerDrawable) menu.findItem(R.id.action_badge_filter).getIcon();
        MenuItem updaterItem = menu.findItem(R.id.action_set_updates);

        //Check if updates are active or not
        Boolean updateToggle = Cfg.ScheduleData.getToggle();
        if (updateToggle == true){
            updaterItem.setTitle("Turn Off Updates");
        }else{
            updaterItem.setTitle("Turn On Updates");
        }

        // Update count
        BadgeUtils.setBadgeCountPrograms(this, badge_choose_programs, Cfg.ProgramsChoice.getProgramsChosen().size());
        setBadgeCountDateWrtDateChosen();

        // ICONS / ITEMS  ==========================================================================
        //action_choose_date = menu.findItem(R.id.action_choose_date);
        ui.changeOptionMenuCalendarIconWrtToDateChosen();

        //Attempt to download a file
        Date currentDateTime = Calendar.getInstance().getTime();
        SimpleDateFormat outboundFormat = new SimpleDateFormat("ddMMMyy", Locale.CANADA);
        outboundFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDate = outboundFormat.format(currentDateTime).toLowerCase();
        if (updateToggle && (!UpdateSchedules.isUpdating) &&(!currentDate.equals(Cfg.ScheduleData.getLatestUpdateAttempt()))) {
            updateSchedule = new UpdateSchedules(mainContext);
            updateSchedule.run();
        }

        // change the icon
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_choose_feedback_mail) {
            FeedbackDialog.prepareDialogForUser();
            return true;
        }

        if (id == R.id.action_badge_programs) {
            startProgramsActivity();
            return true;
        }

        if (id == R.id.action_badge_date) {
            PickDateDialog.show(this);
            return true;
        }

        if (id == R.id.action_badge_filter) {
            startFilterActivity();
            return true;
        }

        if (id == R.id.action_set_updates){

            //toggle updates
            Boolean updateToggle = Cfg.ScheduleData.getToggle();
            Cfg.ScheduleData.setToggle(!updateToggle);

            //uncomment this if wanting to update once on activation of the updater
          /*  if (!updateToggle) {
                UpdateSchedules updateSchedule = new UpdateSchedules(mainContext);
                updateSchedule.run();
            }*/
            invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // PRIVATE--------------------------------------------------------------------------------------

    private static void startProgramsActivity() {
        Intent myIntent = new Intent(mainContext, ProgramsActivity.class);
        mainContext.startActivity(myIntent);
    }

    private static void startFilterActivity() {

        Intent myIntent = new Intent(mainContext, FilterActivity.class);
        mainContext.startActivity(myIntent);
    }

    private static void startDatePickerActivity() {
        Intent myIntent = new Intent(mainContext, DatePickerActivity.class);
        mainContext.startActivity(myIntent);
    }

    GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {

            long startTime = System.currentTimeMillis();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(AsapActivity.mGoogleApiClient);
            System.out.println("total time in MILLISECONDS " + (startTime - System.currentTimeMillis()));
        }

        @Override
        public void onConnectionSuspended(int i) {
            System.out.println("connection failed");
        }
    };

    GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            System.out.println("connection failed");
        }
    };

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mainContext)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }


    class AsapActivityUi extends AbstractActivityUi {
        //declare ui elements
        private ListView listview;
        private ProgressBar progressBar;
        private TextView displayDateTextView;

        public AsapActivityUi() {
            //instantiate ui elements
            listview = (ListView) AsapActivity.mainActivity.findViewById(R.id.activitiesListView);
            progressBar = (ProgressBar) AsapActivity.mainActivity.findViewById(R.id.waitingForLocationProgressBar);
            displayDateTextView = (TextView) AsapActivity.mainActivity.findViewById(R.id.displayDateTextView);
        }

        @Override
        public void addEventListeners() {

        }

        @Override
        public void removeEventListeners() {
            //switchMainService.getSwitchWidget().setOnCheckedChangeListener(null);
        }

        public void populateElementsFromSharedPreferences() {
            //switchMainService.setSwitchState(Cfg.Service.isServiceStarted()); //set the switch on if the service is already started
        }

        public void showLoadingProgressBarAndHideContent() {
            progressBar.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }

        public void hideLoadingProgressBarAndShowContent() {
            progressBar.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
        }

        public void hideLoadingProgressBar() {
            progressBar.setVisibility(View.INVISIBLE);
        }

        public void updateDisplayDateTextViewWrtToDateChosen() {
            if (Cfg.DateChoice.isDateChosenSkateNow())
                displayDateTextView.setText(getResources().getString(R.string.displayDateTextView_skateNowMessage_2rows));
            else
                displayDateTextView.setText(
                        DateTimeHelpers.getHumanReadableDate_northAmerica_2rows(Cfg.DateChoice.getDateChosenOrNow())
                );
        }

        public void populateAsapListAfterGettingLocation() {
            // A) show the list with no location information
            AsapListPopulator.populateAsapList(listview, mainContext, null);

            LocationManager mLocationManager = PhoneGPS.getLocationManager(mainContext);

            //B) implementing the last known location and show the list
            //Location lastKnownBest = PhoneLocation.getLastKnownBest_acc500(mainContext);
            Location lastKnownBest = PhoneLocation.getLastKnownBest_forAsap(mainContext);
            if (null != lastKnownBest)
                AsapListPopulator.populateAsapList(listview, mainContext, lastKnownBest);


            //C) request location updates and then reshow the list
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setSpeedRequired(false);
            LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //save location for later use
                    PhoneLocation.setUpdatedLocation(location);

                    AsapListPopulator.populateAsapList(listview, mainContext, location);
                    AsapActivity.instance.ui.hideLoadingProgressBar();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            mLocationManager.requestSingleUpdate(criteria, mLocationListener, null);
        }

        // change calendar icon with respect to asap/other day schedule RED/GREEN
        public void changeOptionMenuCalendarIconWrtToDateChosen() {
//            try {//get the calendar button
//                action_choose_date = AsapActivity.optionsMenu.findItem(R.id.action_choose_date);
//                if (Cfg.DateChoice.isDateChosenSkateNow())
//                    action_choose_date.setIcon(R.drawable.ic_action_go_to_today_chosen_green); // green is live (Skate Now)
//                else
//                    action_choose_date.setIcon(R.drawable.ic_action_go_to_today_chosen_red);
//
//            } catch (Exception e) {
//                Log.d("ChangeTodayIcon", "Could not change icon now");
//            }
        }
    }
    // PUBLIC --------------------------------------------------------------------------------------

    public static void ToastShort(final String s) {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(mainActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void populateAsapListAfterGettingLocationInUi() {
        ui.populateAsapListAfterGettingLocation();
    }

    public static void updateDisplayDateTextViewWrtToDateChosen() {
        instance.ui.updateDisplayDateTextViewWrtToDateChosen();
    }

    public static void changeOptionMenuCalendarIconWrtToDateChosen() {
        ui.changeOptionMenuCalendarIconWrtToDateChosen();
    }

    public static void setBadgeCountDateWrtDateChosen() {
        if (null != AsapActivity.instance.badge_choose_date)
            if (Cfg.DateChoice.isDateChosenSkateNow())
                BadgeUtils.setBadgeCountDate(
                        AsapActivity.mainContext,
                        AsapActivity.instance.badge_choose_date,
                        //AsapActivity.mainContext.getResources().getString(R.string.displayDateTextView_skateNowMessage)
                        "Now"
                );
            else
                BadgeUtils.setBadgeCountDate(
                        AsapActivity.mainContext,
                        AsapActivity.instance.badge_choose_date,
                        DateTimeHelpers.getHumanReadableDate_northAmerica_2rows(Cfg.DateChoice.getDateChosenOrNow())
                );
    }
}
