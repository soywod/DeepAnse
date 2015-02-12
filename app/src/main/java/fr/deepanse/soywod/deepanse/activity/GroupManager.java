package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class GroupManager extends Activity {

    private static final int RESULT_RECOGNIZER = 0;
    private static final int RESULT_SET_COLOR = 1;
    private static boolean longClicked = false;

    private ArrayList<DeepAnseGroup> arrayGroup;
    private DeepAnseGroup selectedGroup = null;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.adapter.GroupManager adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        ListView listViewGroup = (ListView) findViewById(R.id.list_view);

        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);

        openDatabases();

        arrayGroup = groupDb.selectAll();
        adapterGroup = new fr.deepanse.soywod.deepanse.adapter.GroupManager(this, arrayGroup);

        listViewGroup.setAdapter(adapterGroup);
        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!longClicked && position > 0) {
                    selectedGroup = arrayGroup.get(position);

                    Intent intent = new Intent(GroupManager.this, EditGroup.class);
                    intent.putExtra("color", selectedGroup.getColor());
                    intent.putExtra("name", selectedGroup.getName());
                    startActivityForResult(intent, RESULT_SET_COLOR);
                }
            }
        });

        listViewGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (position > 0) {
                    longClicked = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupManager.this, 2);
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
    protected void onDestroy() {
        super.onDestroy();
        closeDatabases();
    }

    protected void openDatabases() {
        try
        {
            groupDb.open();
        }catch(SQLException e){e.printStackTrace();}
    }

    protected void closeDatabases() {
        groupDb.close();
    }

    public void eventAddGroup(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_group));
        startActivityForResult(intent, RESULT_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SET_COLOR && resultCode == RESULT_OK) {
            selectedGroup.setColor(data.getIntExtra("color", selectedGroup.getColor()));
            selectedGroup.setName(data.getStringExtra("name"));
            groupDb.update(selectedGroup.getId(), selectedGroup);
            adapterGroup.notifyDataSetChanged();
        }
        else if (requestCode == RESULT_RECOGNIZER && resultCode == RESULT_OK) {
            DeepAnseGroup group = new DeepAnseGroup();
            Random rand = new Random();
            group.setColor(Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
            group.setName(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
            groupDb.insert(group);
            arrayGroup.add(group);
            adapterGroup.notifyDataSetChanged();
        }
    }
}
