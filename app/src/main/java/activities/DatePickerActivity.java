package activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import miluca.skatetime.R;


public class DatePickerActivity extends ActionBarActivity {

    private static Activity mainActivity;
    private static Context mainContext;

    CalendarView calendar;

    // LIFETIME ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        //save the activity and context
        mainActivity = this;
        mainContext = this;

        initializeCalendar();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public static Activity getActivity() {
        return DatePickerActivity.mainActivity;
    }

    public static Context getContext() {
        return DatePickerActivity.mainContext;
    }

    public void initializeCalendar() {


    }

}
