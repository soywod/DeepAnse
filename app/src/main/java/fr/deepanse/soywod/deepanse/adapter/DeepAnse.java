package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.Conversion;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_listview, parent, false);
        }

        // Lookup view for data population
        TextView textDate = (TextView) convertView.findViewById(R.id.text_date);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);

        // Populate the data into the template view using the data object
        textDate.setText(Conversion.dateToStringFr(deepAnse.getDate()));
        textAmount.setText(String.valueOf(deepAnse.getAmount()));

        // Return the completed view to render on screen
        return convertView;
    }

}
