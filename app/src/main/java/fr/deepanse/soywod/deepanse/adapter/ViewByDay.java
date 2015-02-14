package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 05/02/2015.
 */
public class ViewByDay extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnse>
{
    public ViewByDay(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> event) {
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
        TextView textComment = (TextView) convertView.findViewById(R.id.text_comment);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);
        int maxWidth = 25;

        // Populate the data into the template view using the data object
        if (deepAnse.getComment().trim().isEmpty())
            textComment.setText("[" + ((deepAnse.getGroup().getName().length() > (maxWidth-2))?(deepAnse.getGroup().getName().substring(0, maxWidth-5)+"..."):(deepAnse.getGroup().getName())) + "]");
        else
            textComment.setText((deepAnse.getComment().length() > maxWidth)?(deepAnse.getComment().substring(0, maxWidth-3)+"..."):(deepAnse.getComment()));
        layout.setBackgroundColor(deepAnse.getGroup().getColorLessOpacity());
        textAmount.setText(String.valueOf(deepAnse.getAmount())+" €");

        // Return the completed view to render on screen
        return convertView;
    }

}
