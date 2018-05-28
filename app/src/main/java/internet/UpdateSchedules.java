package internet;

/**
 * Performs download and storage of update schedule
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import com.google.gson.Gson;

import activities.AsapActivity;
import cfg.Cfg;
import data_works.ProgramNamesGrabber;
import miluca.skatetime.R;

public class UpdateSchedules implements Runnable {

    public static boolean isUpdated = false;
    public static int dataForMonth = 13;
    public static boolean isUpdating = false;
    Context mainContext;
    public UpdateSchedules(Context MainContext){
        this.mainContext = MainContext;
    }

    @Override
    public void run() {
        isUpdating = true;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        initUpdate();
    }

    public void initUpdate(){

        // check if you are connected or not
        if(isConnected()){

            String date = Cfg.ScheduleData.getUpdateDataDate();
            //Log.d("***DATE SENT IS ***", date);

            //date = "http://54.171.139.204/updater.php?schedDate=" + date;
            date = mainContext.getString(R.string.scheduleUpdate_URL) + date;
            //date = "http://54.171.139.204/updater.php?schedDate=10Aug16";
            new HttpAsyncTask().execute(date);
        }
        else{
            Toast.makeText(mainContext, "Cannot Update! Network Unavailable", Toast.LENGTH_LONG).show();
            isUpdating = false;
        }
    }

   /* public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
        }
        return result;
    }*/

    public static String GET(String uri){
        String result = "";
        BufferedReader bufferedReader = null;
        try {

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            Log.d("GetStringResult", sb.toString().trim());
            bufferedReader.close();
            con.disconnect();
            return sb.toString().trim();

        } catch (Exception e) {
            Log.e("InputStreamException", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) mainContext.getSystemService(mainContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //String stringResult = result.substring(0,7);
            String stringResult = result.replace(" ", "");
            Log.e("***RESULT LENGTH ***", String.valueOf(stringResult.length()));
            if (stringResult.toLowerCase().contains("uptodate")){

                //Schedules are up to date, no action required
                Cfg.ScheduleData.setLatestUpdateAttempt();
                Toast.makeText(mainContext, "Schedules are up to date", Toast.LENGTH_LONG).show();
            }else if (stringResult.toLowerCase().contains("noupdate")) {

                //Server returned update file not available
                Toast.makeText(mainContext, "Server-side error. Please try again later", Toast.LENGTH_LONG).show();
            }else if (stringResult.equals("")) {

                //Connection to the script was not possible
                Toast.makeText(mainContext, "Cannot connect to Update Server.", Toast.LENGTH_LONG).show();
            }else{

                //Schedules have just been updated.
                Cfg.ScheduleData.setLatestUpdateAttempt();
                String validResult = result;
                String updateFilename = "skating_times_current";
                FileOutputStream outputStream;

                try {
                    outputStream = mainContext.getApplicationContext().openFileOutput(updateFilename, Context.MODE_PRIVATE);
                    outputStream.write(validResult.getBytes());
                    Log.e("setScheduleData", validResult);
                    outputStream.close();
                    isUpdated = true;
                    dataForMonth = Cfg.ScheduleData.setUpdateData();
                    Toast.makeText(mainContext, "Schedules updated. Happy Skating!" , Toast.LENGTH_LONG).show();
                    AsapActivity.populateAsapListAfterGettingLocationInUi();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Could not save Updated Results" , Toast.LENGTH_LONG).show();
                }


            }
            isUpdating = false;
            this.cancel(true);
        }
    }

}
