package activities;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cfg.Cfg;
import data_works.ProgramNamesGrabber;
import miluca.skatetime.R;

/**
 * Created by Emil
 */
public class ProgramsActivity extends ActionBarActivity {

    private static Activity mainActivity;
    private static Context mainContext;

    ActivityUi ui;

    ArrayList<String> programNames1;
    ArrayList<String> programNames2;

    // LIFETIME ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        //save the activity and context
        mainActivity = this;
        mainContext = this;
        ActivityContext.setContext(this);

        ui = new ActivityUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        //populate this list
        ui.populateListWithAllPrograms();
        //wait for the list to be populated (in the future) and install listeners and read from shared prefs what was clicked before
        new Thread(ui.populateElementsFromSharedPreferencesRunnable).start();

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
        // getMenuInflater().inflate(R.menu.menu_programs, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == android.R.id.home) {
             finish(); // finish the activity
             return true;
         }

        return super.onOptionsItemSelected(item);
    }

    // PRIVATE--------------------------------------------------------------------------------------


    //private String lv_items[] = {"Android", "iPhone", "BlackBerry", "AndroidPeople", "J2ME", "Listview", "ArrayAdapter", "ListItem", "Us", "UK", "India"};

    class ActivityUi extends AbstractActivityUi {
        //declare ui elements
        private ListView lView;
        private ListView lView2;
        private ImageView image;
        private ImageView image2;

        public ActivityUi() {
            image = (ImageView)findViewById(R.id.imageView01);
            image.setImageResource(R.drawable.icon_skating);

            image2 = (ImageView)findViewById(R.id.imageView02);
            image2.setImageResource(R.drawable.icon_shinny);

            lView = (ListView) findViewById(R.id.ListView01);
            lView2 = (ListView) findViewById(R.id.ListView02);
        }


        @Override
        public void addEventListeners() {
            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    updateCheckedProgramsInPrefs();
                }
            });
            lView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    updateCheckedProgramsInPrefs();
                }
            });
        }

        @Override
        public void removeEventListeners() {
            lView.setOnItemClickListener(null);
            lView2.setOnItemClickListener(null);
        }

        @Override
        public void populateElementsFromSharedPreferences() {
            // more complicated then just SP so look in populateListWithAllPrograms()
        }

        public void populateListWithAllPrograms() {
            //retrieve programs that are in the current xml
//            programNames = ProgramNamesGrabber.getProgramNamesList();
            programNames1 = ProgramNamesGrabber.getAllActivitiesSetFromJsonOrFromSP(ProgramsActivity.getActivity(),"Skate");
            programNames2 = ProgramNamesGrabber.getAllActivitiesSetFromJsonOrFromSP(ProgramsActivity.getActivity(),"Shinny");

            //build the list view for programs from the XML
            lView.setAdapter(new ArrayAdapter<>(ProgramsActivity.getContext(), android.R.layout.simple_list_item_multiple_choice, programNames1));
            lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lView2.setAdapter(new ArrayAdapter<>(ProgramsActivity.getContext(), android.R.layout.simple_list_item_multiple_choice, programNames2));
            lView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        }

        Runnable populateElementsFromSharedPreferencesRunnable = new Runnable() {
            @Override
            public void run() {
                //waiting for the adapter to load the data
                while (lView.getCount() < programNames1.size()) {
                    SystemClock.sleep(10);
                }
                while (lView2.getCount() < programNames2.size()) {
                    SystemClock.sleep(10);
                }

                // run the checking of the checkboxes on the ui
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        // UI code goes here
                        checkCheckboxesFromSharedPreferences();
                    }
                });


            }
        };

        public void checkCheckboxesFromSharedPreferences() {
            // checked ones -----------------------------------------------------------------------
            //retrieve the set of checked programs from prefs
            Set<String> selectedPrograms = new HashSet(Cfg.ProgramsChoice.getProgramsChosen());
            //select all checkboxes that were included in the checked programs
            for (int i = 0; i < lView.getCount(); i++) {
                //CheckBox cb = (CheckBox) lView.getItemAtPosition(i);
                if (selectedPrograms.contains(lView.getItemAtPosition(i))) lView.setItemChecked(i, true);
                else lView.setItemChecked(i, false);
            }

            for (int i = 0; i < lView2.getCount(); i++) {
                //CheckBox cb = (CheckBox) lView.getItemAtPosition(i);
                if (selectedPrograms.contains(lView2.getItemAtPosition(i))) lView2.setItemChecked(i, true);
                else lView2.setItemChecked(i, false);
            }

            ui.addEventListeners();

        }

        private synchronized void updateCheckedProgramsInPrefs() {
            //create a new empty list
            ArrayList<String> checkedPrograms = new ArrayList<>();

            //see which items are checked and insert only those in the list
            SparseBooleanArray checked = lView.getCheckedItemPositions();
            for (int i = 0; i < lView.getAdapter().getCount(); i++) {
                if (checked.get(i)) {
                    checkedPrograms.add((String)lView.getItemAtPosition(i));
                }
            }

            SparseBooleanArray checked2 = lView2.getCheckedItemPositions();
            for (int i = 0; i < lView2.getAdapter().getCount(); i++) {
                if (checked2.get(i)) {
                    checkedPrograms.add((String)lView2.getItemAtPosition(i));
                }
            }

            //save the new list in prefs
            Cfg.ProgramsChoice.setProgramsChosen(checkedPrograms);
        }


    }


    // PUBLIC --------------------------------------------------------------------------------------

    public static Activity getActivity() {
        return ProgramsActivity.mainActivity;
    }

    public static Context getContext() {
        return ProgramsActivity.mainContext;
    }

    public static void ToastShort(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static synchronized void updateAsAllCheckedProgramsInPrefs(Activity activity) {
        Cfg.ProgramsChoice.setProgramsChosen(
                ProgramNamesGrabber.getAllActivitiesSetFromJsonOrFromSP(activity, "")
        );
    }

}
