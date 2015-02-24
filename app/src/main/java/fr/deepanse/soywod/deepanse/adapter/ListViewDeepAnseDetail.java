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
public class ListViewDeepAnseDetail extends ArrayAdapter<fr.deepanse.soywod.deepanse.model.DeepAnse>
{
    public ListViewDeepAnseDetail(Context context, ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse) {
        super(context, 0, arrayDeepAnse);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_listview_detail, parent, false);
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.text_group);
        TextView textDate = (TextView) convertView.findViewById(R.id.text_date);
        TextView textDesc = (TextView) convertView.findViewById(R.id.text_desc);
        TextView textAmount = (TextView) convertView.findViewById(R.id.text_amount);

        textGroup.setText("["+deepAnse.getGroup().getName()+"]");
        textGroup.setTextColor(deepAnse.getGroup().getColor());

        textDate.setText(Conversion.dateToStringFr(deepAnse.getDate()));

        textDesc.setText(Conversion.firstCharToUpperCase(deepAnse.getComment()));

        textAmount.setText(deepAnse.getAmount() + " €");

        return convertView;
    }
}