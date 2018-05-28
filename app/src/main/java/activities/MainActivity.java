package activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;

import estha_helpers.FileToString;
import miluca.skatetime.R;
import skating_schedule.Schedule;


public class MainActivity extends ActionBarActivity {

    private static Activity mainActivity;
    private static Context mainContext;

    ActivityUi ui;

    // LIFETIME ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //save the activity and context
        mainActivity = this;
        mainContext = this;

        ui = new ActivityUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        ui.addEventListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.populateElementsFromSharedPreferences();
        String scheduleJson = null;
        Schedule schedule = null;
        try {
            InputStream scheduleJsonImputStream = getResources().openRawResource(R.raw.skating_times);
            scheduleJson = FileToString.convertStreamToString(scheduleJsonImputStream);
            schedule = new Gson().fromJson(scheduleJson, Schedule.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        schedule.getData().getLocations().getLocation();
        String s;
        s = "ok";

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // PRIVATE--------------------------------------------------------------------------------------

    class ActivityUi extends AbstractActivityUi {
        //declare ui elements

        public ActivityUi() {
            //instantiate ui elements
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

    }

    // PUBLIC --------------------------------------------------------------------------------------

    //TODO: Take this out
    public void startLoginActivityWithNoBackStack() {
        //Intent intentLaunchLoginActivity = new Intent(mainContext, LoginActivity.class);
        //intentLaunchLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intentLaunchLoginActivity);
    }

    public static Activity getActivity() {
        return MainActivity.mainActivity;
    }

    public static Context getContext() {
        return MainActivity.mainContext;
    }

    public static void ToastShort(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
