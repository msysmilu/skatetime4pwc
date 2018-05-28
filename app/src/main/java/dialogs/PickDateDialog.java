package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.gc.materialdesign.views.ButtonFlat;

import org.joda.time.DateTime;

import activities.AsapActivity;
import cfg.Cfg;
import estha_helpers.DateTimeHelpers;
import miluca.skatetime.R;

import static android.widget.DatePicker.OnDateChangedListener;

/**
 * Created by Emil
 */
public class PickDateDialog {
    static DatePicker datePicker;
    static ButtonFlat datePickButtonSkateDate;
    static ButtonFlat datePickButtonSkateNow;

    public static void show(Context context) {
        // dialog stuff ============================================================================
        final Dialog dialog = new Dialog(context);

        // must be called before adding content
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);

        dialog.setContentView(R.layout.dialog_pick_date);
        dialog.setTitle("Pick schedule date");

        //date picker ====================================================================================
        datePicker = (DatePicker) dialog.findViewById(R.id.datePickDatePicker);
        //show year properly (instead of another calendar that is drawn on my samsung)
        datePicker.setCalendarViewShown(false);
        //limits...
        // DateTime minDate = new DateTime(2017, 1, 1, 0, 0);
        DateTime minDate = new DateTime(
                DateTime.now().getYear(),
                DateTime.now().getMonthOfYear(), //The initial month starting from zero.
                DateTime.now().getDayOfMonth(),
                0, 0);
        DateTime maxDate = new DateTime(DateTime.now().getYear()+5, 12, 31, 0, 0);
        datePicker.setMaxDate(maxDate.getMillis());
        datePicker.setMinDate(minDate.getMillis());
        // set current date into date picker
        datePicker.init(
                Cfg.DateChoice.getDateChosenOrNow().getYear(),
                Cfg.DateChoice.getDateChosenOrNow().getMonthOfYear() - 1, //The initial month starting from zero.
                Cfg.DateChoice.getDateChosenOrNow().getDayOfMonth(),
                dateChangedListener);

        // button select date ========================================================================
        datePickButtonSkateDate = (ButtonFlat) dialog.findViewById(R.id.datePickButtonOk);
        // if button is clicked, close the custom dialog
        datePickButtonSkateDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime selectedDate = new DateTime(
                        datePicker.getYear(),
                        datePicker.getMonth() + 1, //Value 0 for monthOfYear must be in the range [1,12]
                        datePicker.getDayOfMonth(),
                        0, 0, 0
                );
                setDateRedrawListAndCloseDialog(selectedDate, dialog);
            }
        });
        datePickButtonSkateDate.setEnabled(true);
        // button text color
        // datePickButtonSkateDate.setBackgroundColor(Color.RED);

        // button skate now ========================================================================
        datePickButtonSkateNow = (ButtonFlat) dialog.findViewById(R.id.datePickButtonSkateNow);
        datePickButtonSkateNow.setEnabled(true);
        // if button is clicked, close the custom dialog
        datePickButtonSkateNow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateRedrawListAndCloseDialog(DateTime.now(), dialog);
            }
        });
        // button text color
        // datePickButtonSkateNow.setBackgroundColor(Color.parseColor("#4caf50")); //dark green material

        changeDatePickButtonOkText(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());

        // dialog again====================================================================================
        dialog.show();
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_action_go_to_today_dark);
    }

    private static void setDateRedrawListAndCloseDialog(DateTime dateTime, Dialog dialog) {
        Cfg.DateChoice.setDateChosen(dateTime);

        AsapActivity.changeOptionMenuCalendarIconWrtToDateChosen();
        AsapActivity.updateDisplayDateTextViewWrtToDateChosen();
        AsapActivity.setBadgeCountDateWrtDateChosen();

        dialog.dismiss();

        AsapActivity.populateAsapListAfterGettingLocationInUi();
    }

    private static OnDateChangedListener dateChangedListener = new OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //since another time is being chosen we activate the "skate now" option
            datePickButtonSkateNow.setEnabled(true);

            //dirty
            datePickButtonSkateDate.setEnabled(true);
            // we change the text in the ok button for more intuition)
            changeDatePickButtonOkText(dayOfMonth, monthOfYear + 1, year);
        }
    };

    private static void changeDatePickButtonOkText(int day, int month, int year) {
        datePickButtonSkateDate.setText(
                "Skate " + DateTimeHelpers.getHumanReadableDate_northAmerica(day, month, year)
        );
    }
}
