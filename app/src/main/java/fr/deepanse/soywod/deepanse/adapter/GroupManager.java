package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 05/02/2015.
 */
public class GroupManager extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnseGroup>
{
    public GroupManager(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> group) {
        super(context, 0, group);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup group = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_group_manager_listview, parent, false);
        }

        // Lookup view for data population
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_group);
        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);

        // Populate the data into the template view using the data object
        layout.setBackgroundColor(group.getColorLessOpacity());
        textGroup.setText(Conversion.firstCharToUpperCase(group.getName()));

        // Return the completed view to render on screen
        return convertView;
    }

}
