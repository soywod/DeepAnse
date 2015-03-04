package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 27/02/2015.
 */
public class ImportGroup extends DeepAnse implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> arrayGroup;
    private ArrayList<String> arrayGroupSelected;


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
        arrayGroup = new ArrayList<>();
        arrayGroupSelected = new ArrayList<>();

        Collections.addAll(arrayGroup, getResources().getStringArray(R.array.import_group));
        Collections.sort(arrayGroup);
        arrayGroup = deleteExistingGroup();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arrayGroup);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public ArrayList<String> deleteExistingGroup() {
        ArrayList<String> arrayTemp = new ArrayList<>();

        for (String item : arrayGroup) {
            if (groupDb.selectByName(item.toLowerCase()) == null) {
                arrayTemp.add(item);
            }
        }

        if (arrayTemp.size() == 0) {
            showShortToast(R.string.toast_no_import_possible);
            finish();
        }

        return arrayTemp;
    }

    public void eventImport(View view) {
        Random random = new Random();

        if (arrayGroupSelected.size() > 0) {
            for (String item : arrayGroupSelected) {
                groupDb.insert(new DeepAnseGroup(0, item.toLowerCase(), Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
            }

            showShortToast(R.string.toast_group_imported);
            finish();
        }
        else {
            showShortToast(R.string.toast_no_group_selected);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (arrayGroupSelected.contains(arrayGroup.get(position))) arrayGroupSelected.remove(arrayGroup.get(position));
        else arrayGroupSelected.add(arrayGroup.get(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_standard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                eventCancel(null);
                break;

            case R.id.menu_home :
                Intent intent = new Intent(ImportGroup.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
