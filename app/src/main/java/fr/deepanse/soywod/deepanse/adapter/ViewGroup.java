package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 05/02/2015.
 */
public class ViewGroup extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnseGroup>
{
    private static View selectedView = null;
    private static int oldColor = 0;

    public ViewGroup(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> group) {
        super(context, 0, group);
    }

    @Override
    public View getView(int position, View convertView, android.view.ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup group = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view_group_listview, parent, false);
        }

        // Lookup view for data population
        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);

        // Populate the data into the template view using the data object
        convertView.setBackgroundColor(group.getColor());
        textGroup.setText(Conversion.firstCharToUpperCase(group.getName()));

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectedView = v;
                return false;
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public boolean isViewSelected() {
        return selectedView != null;
    }

    public void removeSelectedView() {
        if (isViewSelected())
            selectedView.setBackgroundColor(oldColor);
        selectedView = null;
    }

    public void setColorSelectedView(int color) {
        oldColor = ((ColorDrawable)selectedView.getBackground()).getColor();
        selectedView.setBackgroundColor(color);
    }
}
