package activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cfg.Cfg;
import data_works.AsapTimeSlot;
import data_works.FilterHolder;
import data_works.ProgramNamesGrabber;
import miluca.skatetime.R;

/**
 * Created by Emil
 */
public class FilterActivity extends ActionBarActivity {

    private static Activity mainActivity;
    private static Context mainContext;

    CheckBox chk_isIndoor = null;
    CheckBox chk_isOutdoor = null;
    CheckBox chk_hasWashroom = null;
    CheckBox chk_hasChangeroom = null;
    CheckBox chk_hasRentals = null;
    CheckBox chk_isMorning = null;
    CheckBox chk_isAfternoon = null;
    CheckBox chk_isEvening = null;

    // LIFETIME ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //save the activity and context
        mainActivity = this;
        mainContext = this;
        ActivityContext.setContext(this);

        //grab all checkboxes from Filter Screen
        chk_isIndoor = (CheckBox) findViewById(R.id.filter_IndoorArenaBox);
        chk_isOutdoor = (CheckBox) findViewById(R.id.filter_OutdoorRinkBox);
        chk_hasWashroom = (CheckBox) findViewById(R.id.filter_AmenitiesWashroom);
        chk_hasChangeroom = (CheckBox) findViewById(R.id.filter_AmenitiesChangeroom);
        chk_hasRentals = (CheckBox) findViewById(R.id.filter_AmenitiesRentals);
        chk_isMorning = (CheckBox) findViewById(R.id.filter_SkateTimesMorning);
        chk_isAfternoon = (CheckBox) findViewById(R.id.filter_SkateTimesAfternoon);
        chk_isEvening = (CheckBox) findViewById(R.id.filter_SkateTimesEvening);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Populate check boxes based on filter status
        populateCheckBoxes();


    }

    @Override
    public void onPause() {
        super.onPause();
        //FilterHolder.saveAllFiltersToSharedPrefs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //FilterHolder.saveAllFiltersToSharedPrefs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            saveCheckBoxes();
            finish(); // finish the activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateCheckBoxes() {
        FilterHolder.loadAllFiltersFromSharedPrefs();
        if (FilterHolder.hasIndoor) chk_isIndoor.setChecked(true);
        else {
            chk_isIndoor.setChecked(false);
        }
        if (FilterHolder.hasOutdoor) chk_isOutdoor.setChecked(true);
        else {
            chk_isOutdoor.setChecked(false);
        }
        if (FilterHolder.hasWashroom) chk_hasWashroom.setChecked(true);
        else {
            chk_hasWashroom.setChecked(false);
        }
        if (FilterHolder.hasChangeroom) chk_hasChangeroom.setChecked(true);
        else {
            chk_hasChangeroom.setChecked(false);
        }
        if (FilterHolder.hasRentals) chk_hasRentals.setChecked(true);
        else {
            chk_hasRentals.setChecked(false);
        }
        if (FilterHolder.hasMorning) chk_isMorning.setChecked(true);
        else {
            chk_isMorning.setChecked(false);
        }
        if (FilterHolder.hasAfternoon) chk_isAfternoon.setChecked(true);
        else {
            chk_isAfternoon.setChecked(false);
        }
        if (FilterHolder.hasEvening) chk_isEvening.setChecked(true);
        else {
            chk_isEvening.setChecked(false);
        }
    }

    public void saveCheckBoxes(){
        FilterHolder.hasIndoor = chk_isIndoor.isChecked();
        FilterHolder.hasOutdoor = chk_isOutdoor.isChecked();
        FilterHolder.hasWashroom = chk_hasWashroom.isChecked();
        FilterHolder.hasChangeroom = chk_hasChangeroom.isChecked();
        FilterHolder.hasRentals = chk_hasRentals.isChecked();
        FilterHolder.hasMorning = chk_isMorning.isChecked();
        FilterHolder.hasAfternoon = chk_isAfternoon.isChecked();
        FilterHolder.hasEvening = chk_isEvening.isChecked();
        FilterHolder.saveAllFiltersToSharedPrefs();

    }

    // PRIVATE--------------------------------------------------------------------------------------


    //private String lv_items[] = {"Android", "iPhone", "BlackBerry", "AndroidPeople", "J2ME", "Listview", "ArrayAdapter", "ListItem", "Us", "UK", "India"};


    // PUBLIC --------------------------------------------------------------------------------------

    public static Activity getActivity() {
        return FilterActivity.mainActivity;
    }

    public static Context getContext() {
        return FilterActivity.mainContext;
    }

    public static void ToastShort(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
