package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import activities.AsapActivity;
import activities.AsapRow;
import cfg.Cfg;
import miluca.skatetime.R;
import phone.PhoneLocation;

/**
 * Created
 * Updated
 * Added additional fields required for AsapRow Popup
 */
public class ComplexDetailsDialog {
    static ButtonFlat complexDetailsCallComplexBtn;
    static ButtonFlat complexDetailsGoToComplexBtn;

    //used for the URI call to maps;
    static String complexTitleForMaps; // used to put a label on google maps
    static String complexLatForMaps;
    static String complexLonForMaps;

    public static void show(Context context, AsapRow data) {
        final Dialog dialog = new Dialog(context);

        //try here because the data might be not populated;
        try {
            //if there are not any activities, don't show
            if (data.getComplex_name().equals(Cfg.Constants.NO_ACTIVITIES_FOUND)
                    || data.getComplex_name().equals(Cfg.Constants.NO_ACTIVITIES_SELECTED)
                    || data.getComplex_name().equals(Cfg.Constants.NO_ACTIVITIES_OPEN)
                    || data.getComplex_name().equals(Cfg.Constants.CLOSED_ACTIVITIES_TODAY)
                    )
                return;

            // sanity checks
            if (null == data.getComplex_name()) data.setComplex_name("");
            if (null == data.getComplex_desc()) data.setComplex_desc("");
            data.setComplex_type(data.getComplex_type().toLowerCase().trim());
            if (null == data.getComplex_type()) data.setComplex_type("");

            complexTitleForMaps = data.getComplex_name();
            complexLatForMaps = data.getLat();
            complexLonForMaps = data.getLon();


            dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
            dialog.setContentView(R.layout.dialog_complex_details);
            dialog.setTitle(data.getComplex_name());

            TextView complexDesc = (TextView) dialog.findViewById(R.id.complexDesc);
            TextView complexType = (TextView) dialog.findViewById(R.id.complexType);
            final TextView complexNumber = (TextView) dialog.findViewById(R.id.complexNumber);
            TextView complexFacilities = (TextView) dialog.findViewById(R.id.complexFacilities);
            complexDetailsGoToComplexBtn = (ButtonFlat) dialog.findViewById(R.id.goToComplexBtn);
            complexDetailsGoToComplexBtn.setEnabled(true);

            complexDetailsGoToComplexBtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Location lastKnownBest = new Location(PhoneLocation.getLastKnownBest(AsapActivity.mainContext,Cfg.Constants.DECENT_PERSONAL_LOCATION_ACCURACY));
                    Location lastKnownBest = PhoneLocation.getUpdatedLocation();

                    // if personal location is known then make a route;
                    if (null != lastKnownBest) {
                        Log.d("ComplexDetailsDialog", "Opening maps for route. Personal position accuracy: " + lastKnownBest.getAccuracy());

                        Double currentLatitude = lastKnownBest.getLatitude();
                        Double currentLongitude = lastKnownBest.getLongitude();
                        String url = "http://maps.google.com/maps?saddr=" + currentLatitude + "," + currentLongitude + "&daddr=" + complexLatForMaps + "," + complexLonForMaps + "&mode=driving";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        AsapActivity.mainContext.startActivity(intent);
                    }
                    //else, just show the location of the place
                    else {
                        Log.d("ComplexDetailsDialog", "Opening maps for location. lastKnown is null");

                        // link to maps with location
                        // Creates an Intent that will load a map of San Francisco
//                    Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    AsapActivity.mainContext.startActivity(mapIntent);

                        String label = complexTitleForMaps;
                        String uriBegin = "geo:" + complexLatForMaps + "," + complexLonForMaps;
                        String query = complexLatForMaps + "," + complexLonForMaps + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        AsapActivity.mainContext.startActivity(mapIntent);
                    }


                    //close dialog once on maps
                    //dialog.dismiss();
                }
            });

            complexNumber.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + complexNumber.getText()));
                    AsapActivity.mainContext.startActivity(intent);
                    //close dialog once on phone
                    //dialog.dismiss();
                }
            });
            complexDesc.setText(data.getComplex_desc());
            complexNumber.setText(data.getComplex_number());

            if (data.getComplex_type().equals("outdoor")) {
                complexType.setText("Outdoor Rink");
            } else {
                complexType.setText("Indoor Arena");
            }
            String facilities = "";

            if (data.isHasWashroom()) {
                facilities = "● Washroom  ";
            }

            if (data.isHasChangeroom()) {
                facilities = facilities + "● Changeroom  ";
            }

            if (data.isHasRentals()) {
                facilities = facilities + "● Rentals";
            }

            if (facilities.equals("")) {
                facilities = "● No facilities";
            }
            complexFacilities.setText(facilities);
        } catch (Exception e) {
            Log.d("ComplexDetailsDialog", e.toString());
            return;
        }

        dialog.show();
    }
}
