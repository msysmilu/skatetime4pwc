package phone;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

import activities.MainActivity;

/**
 * Created by Emil
 */
public class PhoneTextToSpeech {

    private static final String TAG = "Text To Speech";
    private static PhoneTextToSpeech _instance;

    TextToSpeech mTextToSpeech = null;
    Boolean textToSpeechInitialized = false; // marks when the TTS was loaded

    // CONSTRUCTOR and SINGLETON -------------------------------------------------------------------

    public PhoneTextToSpeech(Context context) {
        OnInitListener mOnInitListener = new OnInitListener() {

            @Override
            public void onInit(int status) {
                mTextToSpeech.setLanguage(Locale.UK);
                //tts.setPitch(1.1f);
                //tts.setSpeechRate(0.4f);
                textToSpeechInitialized = true; // mark as started
            }
        };
        mTextToSpeech = new TextToSpeech(context, mOnInitListener);
        _instance = this;
    }

    public static PhoneTextToSpeech getInstance() {
        try {
            if (_instance == null) _instance = new PhoneTextToSpeech(MainActivity.getContext());
            return _instance;
        } catch (Exception e) {
            Log.d(TAG, "Speech not initialized yet");
            return null;
        }
    }

    // PRIVATE -------------------------------------------------------------------------------------

    private void speakOnce(String text) {
        try {
            if (textToSpeechInitialized) mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        } catch (Exception e) {
            Log.d(TAG, "Some exception with the speaker");
        }
    }

    private void stopSpeak() {
        try {
            if (textToSpeechInitialized) mTextToSpeech.stop();
        } catch (Exception e) {
            Log.d(TAG, "Some exception with the speaker");
        }
    }

    private void shutdown() {
        try {
            if (textToSpeechInitialized ) mTextToSpeech.shutdown();
            textToSpeechInitialized = false;
        } catch (Exception e) {
            Log.d(TAG, "Some exception with the speaker");
        }
    }

    // STATIC --------------------------------------------------------------------------------------

    public static void speakIfEnabled(String text) {
        try {
            if(true) //if enables
                getInstance().mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        } catch (Exception e) {
            Log.d(TAG, "Some exception with the speaker");
        }
    }


}
