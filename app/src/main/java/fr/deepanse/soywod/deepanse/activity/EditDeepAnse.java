package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ViewGroup;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditDeepAnse extends Activity {

    private static boolean buttonSavePressed;
    private ArrayList<DeepAnseGroup> arrayGroup;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.adapter.ViewGroup adapterGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deepanse);

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_group);

        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);

        openDatabases();

        arrayGroup = groupDb.selectAll();
        adapterGroup = new ViewGroup(this, arrayGroup);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        datePicker.setCalendarViewShown(false);
        spinner.setAdapter(adapterGroup);

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
}

