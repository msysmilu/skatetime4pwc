package activities;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dialogs.ComplexDetailsDialog;
import miluca.skatetime.R;

/**
 * Created by Emil
 * Updated Popup dialog listener
 */
class AsapRowAdapter extends BaseAdapter {

    Context context;
    AsapRow[] data;
    private static LayoutInflater inflater = null;
    ImageView skateImage;
    ImageView shinnyImage;

    public AsapRowAdapter(Context context, AsapRow[] data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return  data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) vi = inflater.inflate(R.layout.row_for_asap_list, null);

        //added alternating colors
        LinearLayout mainRow = (LinearLayout) vi.findViewById(R.id.mainRow);
        if (data[position].isWhite()){
            mainRow.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            mainRow.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }

        TextView textDistance = (TextView) vi.findViewById(R.id.textDistance);
        TextView textTime = (TextView) vi.findViewById(R.id.textTime);
        final TextView textComplexName = (TextView) vi.findViewById(R.id.textComplexName);
        TextView textActivity = (TextView) vi.findViewById(R.id.textActivity);
        TextView textTimetable = (TextView) vi.findViewById(R.id.textTimetable);

        textDistance.setText(data[position].getDistance_km_label());
        textTime.setText(data[position].getStarts_in_min_label());
        textComplexName.setText(data[position].getComplex_name());

        //check if row needs an invisible complex name
        if(!data[position].hasVisibleComplex()){
            textComplexName.setVisibility(View.GONE);
            textDistance.setVisibility(View.GONE);
        }else{
            textComplexName.setVisibility(View.VISIBLE);
            textDistance.setVisibility(View.VISIBLE);
        }

        textActivity.setText(data[position].getActivity());

       /* Log.d("textSize", String.valueOf(textActivity));
        if (textActivity.getLineCount() < 2 ){
            textActivity.setText(textActivity.getText() + "\n");
        }*/
        textTimetable.setText(data[position].getTimetable());

        //add images
        skateImage = (ImageView) vi.findViewById(R.id.imageViewMainList);
        if (data[position].getActivity().toLowerCase().contains("skat")){
            skateImage.setImageResource(R.drawable.icon_skating);
        }else{
            skateImage.setImageResource(R.drawable.icon_shinny);
        }

        vi.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ComplexDetailsDialog.show(AsapActivity.mainContext,data[position]);
            }
        });

                return vi;
    }
}