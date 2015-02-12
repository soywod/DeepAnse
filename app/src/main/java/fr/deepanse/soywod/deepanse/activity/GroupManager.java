package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class GroupManager extends Activity {

    private static final int RESULT_SET_COLOR = 1;

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

                //if (position > 0) {
                selectedGroup = arrayGroup.get(position);

                Intent intent = new Intent(GroupManager.this, SetColor.class);
                intent.putExtra("color", selectedGroup.getColor());
                startActivityForResult(intent, RESULT_SET_COLOR);
                //}
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SET_COLOR && resultCode == RESULT_OK) {
            selectedGroup.setColor(data.getIntExtra("color", selectedGroup.getColor()));
            groupDb.update(selectedGroup.getId(), selectedGroup);
            adapterGroup.notifyDataSetChanged();
        }
    }
}
