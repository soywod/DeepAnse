package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 05/02/2015.
 */
public class SpinnerDeepAnseGroup extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnseGroup>
{
    private boolean isBackgroundSet;

    public SpinnerDeepAnseGroup(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayDeepAnseGroup) {
        super(context, 0, arrayDeepAnseGroup);
        isBackgroundSet = false;
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

        textName.setText("["+deepAnseGroup.getName()+"]");
        textName.setTextColor(deepAnseGroup.getColor());
        //if (isBackgroundSet) textName.setBackgroundColor(Color.parseColor("#33FFFFFF"));

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

        textName.setText("["+deepAnseGroup.getName()+"]");
        textName.setTextColor(deepAnseGroup.getColor());

        return convertView;
    }

    public void setWhiteBackground(boolean isBackgroundSet) {
        this.isBackgroundSet = isBackgroundSet;
    }
}
