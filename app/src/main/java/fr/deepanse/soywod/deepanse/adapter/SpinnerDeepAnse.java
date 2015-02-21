package fr.deepanse.soywod.deepanse.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 19/02/2015.
 */
public class SpinnerDeepAnse extends ArrayAdapter<String> implements View.OnTouchListener {

    private TextView textDesc;

    public SpinnerDeepAnse(Context context, ArrayList<String> text) {
        super(context, 0, text);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String text = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_spinner_item, parent, false);
        }

        textDesc = (TextView) convertView.findViewById(R.id.text);
        textDesc.setText(text);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        String text = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_spinner_item, parent, false);
        }

        textDesc = (TextView) convertView.findViewById(R.id.text);
        textDesc.setOnTouchListener(this);
        textDesc.setText(text);
        return convertView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.setBackgroundColor(getContext().getResources().getColor(R.color.ColorBlue));
        return false;
    }
}
