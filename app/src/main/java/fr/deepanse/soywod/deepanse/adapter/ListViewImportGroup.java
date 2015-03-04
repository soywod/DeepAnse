package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 27/02/2015.
 */
public class ListViewImportGroup extends ArrayAdapter<String>
{
    public ListViewImportGroup(Context context, ArrayList<String> arrayGroup) {
        super(context, 0, arrayGroup);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String group = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_listview_simple, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.text_name);

        textName.setText(Conversion.firstCharToUpperCase(group));

        return convertView;
    }
}
