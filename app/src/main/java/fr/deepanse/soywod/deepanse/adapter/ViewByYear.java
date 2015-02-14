package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.DateFR;

/**
 * Created by soywod on 05/02/2015.
 */
public class ViewByYear extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.Report>
{
    public ViewByYear(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.Report> report) {
        super(context, 0, report);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.Report report = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_globalview_listview, parent, false);
        }

        // Lookup view for data population
        TextView textComment = (TextView) convertView.findViewById(R.id.text_comment);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);

        // Populate the data into the template view using the data object
        textComment.setText(Conversion.firstCharToUpperCase(DateFR.findDateLitteral(report.getDate().get(GregorianCalendar.MONTH))));
        textAmount.setText(report.getTotal()+" €");

        // Return the completed view to render on screen
        return convertView;
    }

}
