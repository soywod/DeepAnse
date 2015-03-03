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
 * Created by soywod on 05/02/2015.
 */
public class SpinnerGroup extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnseGroup>
{
    public SpinnerGroup(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayDeepAnseGroup) {
        super(context, 0, arrayDeepAnseGroup);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_spinner_item, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.text_name);

        textName.setText(Conversion.firstCharToUpperCase(deepAnseGroup.getName()));
        textName.setTextColor(deepAnseGroup.getColor());

        return convertView;
    }

    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_spinner_item, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.text_name);

        textName.setText(Conversion.firstCharToUpperCase(deepAnseGroup.getName()));
        textName.setTextColor(deepAnseGroup.getColor());

        return convertView;
    }
}
