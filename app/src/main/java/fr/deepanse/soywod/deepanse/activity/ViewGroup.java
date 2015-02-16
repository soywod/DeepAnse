package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class ViewGroup extends DeepAnse {

    private static boolean longClicked;
    private ArrayList<DeepAnseGroup> arrayGroup;
    private fr.deepanse.soywod.deepanse.adapter.ViewGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        longClicked = false;

        arrayGroup = new ArrayList<>();
        adapterGroup = new fr.deepanse.soywod.deepanse.adapter.ViewGroup(this, arrayGroup);

        ListView listViewGroup = (ListView) findViewById(R.id.listview);
        listViewGroup.setAdapter(adapterGroup);

        listViewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (adapterGroup.isViewSelected()) {
                        adapterGroup.setColorSelectedView(getResources().getColor(R.color.ColorBlue));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!adapterGroup.isViewSelected()) {
                        adapterGroup.removeSelectedView();
                    }
                } else {
                    adapterGroup.removeSelectedView();
                }

                return false;
            }
        });

        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!longClicked && position > 0) {
                    Intent intent = new Intent(ViewGroup.this, EditGroup.class);
                    intent.putExtra("new_group", false);
                    intent.putExtra("id", arrayGroup.get(position).getId());
                    intent.putExtra("color", arrayGroup.get(position).getColor());
                    intent.putExtra("name", arrayGroup.get(position).getName());
                    startActivityForResult(intent, RESULT_ADD_GROUP_BY_HAND);
                }
            }
        });

        listViewGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (position > 0) {
                    longClicked = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewGroup.this, 2);
                    builder.setMessage(getString(R.string.prompt_del_group));
                    builder.setPositiveButton("Oui", new AlertBox() {
                        @Override
                        public void execute() {
                            groupDb.updateAllById(arrayGroup.get(position).getId());
                            groupDb.delete(arrayGroup.get(position).getId());
                            arrayGroup.remove(position);
                            adapterGroup.notifyDataSetChanged();
                            longClicked = false;
                        }
                    });
                    builder.setNegativeButton("Non", new AlertBox() {
                        @Override
                        public void execute() {
                            longClicked = false;
                        }
                    });
                    builder.show();
                }

                return false;
            }
        });

    }

    @Override
    protected void mainRefresh(int count) {
        ArrayList<DeepAnseGroup> tmpArrayGroup = groupDb.selectAll();

        arrayGroup.clear();

        if (tmpArrayGroup != null)
            for (DeepAnseGroup item : tmpArrayGroup)
                arrayGroup.add(item);

        adapterGroup.notifyDataSetChanged();
    }
}
