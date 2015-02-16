package fr.deepanse.soywod.deepanse.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 05/02/2015.
 */
public class ViewByDay extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnse>
{
    private static View selectedView = null;
    private static int oldColor = 0;

    public ViewByDay(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> event) {
        super(context, 0, event);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_globalview_listview, parent, false);
        }

        // Lookup view for data population
        TextView textComment = (TextView) convertView.findViewById(R.id.text_comment);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);
        int maxWidth = 25;

        // Populate the data into the template view using the data object
        if (deepAnse.getComment().trim().isEmpty())
            textComment.setText("[" + ((deepAnse.getGroup().getName().length() > (maxWidth-2))?(deepAnse.getGroup().getName().substring(0, maxWidth-5)+"..."):(deepAnse.getGroup().getName())) + "]");
        else
            textComment.setText((deepAnse.getComment().length() > maxWidth)?(deepAnse.getComment().substring(0, maxWidth-3)+"..."):(deepAnse.getComment()));

        convertView.setBackgroundColor(deepAnse.getGroup().getColor());
        textAmount.setText(String.valueOf(deepAnse.getAmount())+" €");

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
