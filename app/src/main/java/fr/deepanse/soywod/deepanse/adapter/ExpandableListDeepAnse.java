package fr.deepanse.soywod.deepanse.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fr.deepanse.soywod.deepanse.R;

public class ExpandableListDeepAnse extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Object[], List<Object[]>> laptopCollections;
    private List<Object[]> laptops;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;

    private boolean childSelectable = false;

    public ExpandableListDeepAnse(Activity context, fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb, List<Object[]> laptops, Map<Object[], List<Object[]>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
        this.groupDb = groupDb;
    }

    public void setChildSelectable(boolean bool) {
        childSelectable = bool;
    }

    public Object[] getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String desc = (String) getChild(groupPosition, childPosition)[0];
        String sum = String.valueOf(getChild(groupPosition, childPosition)[1]);

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_expandable_listview_child, null);
        }

        TextView textDesc = (TextView) convertView.findViewById(R.id.text_desc);
        TextView textSum = (TextView) convertView.findViewById(R.id.text_sum);

        textDesc.setText("[" + desc + "]");
        textDesc.setTextColor(groupDb.selectByName(desc).getColor());

        textSum.setText(sum + " €");

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }

    public Object[] getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String desc = (String) getGroup(groupPosition)[0];
        String sum = String.valueOf(getGroup(groupPosition)[1]);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_expandable_listview_parent, null);
        }

        TextView textDesc = (TextView) convertView.findViewById(R.id.text_desc);
        TextView textSum = (TextView) convertView.findViewById(R.id.text_sum);

        textDesc.setText(desc);
        textSum.setText(sum + " €");

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return childSelectable;
    }
}