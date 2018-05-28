package phone;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Emil
 */
public class PhoneVibrator {

    Context callingContext;

    public PhoneVibrator(Context callingContext){
        this.callingContext = callingContext;
    }

    public void vibrate(int interval_ms){
        Vibrator vibrator = (Vibrator) callingContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
    }
}
