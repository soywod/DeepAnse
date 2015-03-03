package fr.deepanse.soywod.deepanse.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewGroup;

/**
 * Created by soywod on 27/02/2015.
 */
public class ImportGroup extends DeepAnse {

    private String[] listGroup;
    private ListView listView;
    private ListViewGroup adapter;
    private ArrayList<String> arrayGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_group);

        initComponent();
        initData();
    }

    public void initComponent() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void initData() {
        Collections.addAll(arrayGroup, getResources().getStringArray(R.array.import_group));
        listGroup = getResources().getStringArray(R.array.import_group);

        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listGroup);
        //adapter = new ListViewGroup(this, arrayGroup);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        adapter.notifyDataSetChanged();
    }
}
