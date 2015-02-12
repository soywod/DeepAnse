package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnse extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnse>
{
    public DeepAnse(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> event) {
        super(context, 0, event);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_globalview_listview, parent, false);
        }

        // Lookup view for data population
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout_deepanse);
        TextView textDate = (TextView) convertView.findViewById(R.id.text_date);
        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);
        TextView textComment = (TextView) convertView.findViewById(R.id.text_comment);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);

        // Populate the data into the template view using the data object
        layout.setBackgroundColor(deepAnse.getGroup().getColor());
        textDate.setText("le " + deepAnse.getDate().get(GregorianCalendar.DAY_OF_MONTH));
        textGroup.setText("[" + deepAnse.getGroup().getName() + "]");
        textComment.setText((deepAnse.getComment().length() > 18)?(deepAnse.getComment().substring(0, 15)+"..."):(deepAnse.getComment()));
        textAmount.setText(String.valueOf(deepAnse.getAmount())+" €");

        // Return the completed view to render on screen
        return convertView;
    }

}
