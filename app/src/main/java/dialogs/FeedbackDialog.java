package dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import activities.AsapActivity;
import miluca.skatetime.BuildConfig;
import miluca.skatetime.R;

/**
 * Created by Emil
 */
public class FeedbackDialog {

    public static void prepareDialogForUser() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@skatetime.ca"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback, Suggestion or Issue on " + AsapActivity.mainActivity.getResources().getString(R.string.app_name));
        i.putExtra(Intent.EXTRA_TEXT, getMailBody());
        try {
            AsapActivity.mainActivity.startActivity(Intent.createChooser(i, "Send mail..."));
            Toast.makeText(AsapActivity.mainActivity, "Thanks! :)", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AsapActivity.mainActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    private static String getMailBody() {
        String message = "";
        message += "[ App: " + AsapActivity.mainActivity.getResources().getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " ("+BuildConfig.VERSION_CODE + ") ]\n";
        message += "[ Phone: " + Build.MANUFACTURER +" " + Build.MODEL + " ]\n";
        message += "[ Android: " + getAndroidVersion() + " ]\n";
        message += "\n";
        message += "Dear Canuck,\n\n";
        message += "We would like your input on new features or feedback if you've encountered difficulties with our application. Cheers!\n\n";
        message += "SkateTime Team!\n";
        message += "\n\n\n";
        message += "Dear SkateTime,\n";
        message += "\n";
        return message;
    }

    private static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

}
