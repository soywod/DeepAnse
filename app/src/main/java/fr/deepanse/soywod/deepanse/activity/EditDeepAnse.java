package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ViewGroup;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditDeepAnse extends Activity {

    private static boolean buttonSavePressed;
    private ArrayList<DeepAnseGroup> arrayGroup;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.adapter.ViewGroup adapterGroup;

    private boolean newDeepAnse;
    private long id;
    private DatePicker datePicker;
    private Spinner spinner;
    private EditText editAmout;
    private EditText editComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deepanse);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);

        buttonSavePressed = false;

        Intent intent = getIntent();

        newDeepAnse = ((intent == null) || (intent.getBooleanExtra("new_deepanse", true)));

        datePicker = (DatePicker) findViewById(R.id.date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        openDatabases();

        arrayGroup = groupDb.selectAll();
        adapterGroup = new ViewGroup(this, arrayGroup);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        datePicker.setCalendarViewShown(false);
        spinner.setAdapter(adapterGroup);

        if (!newDeepAnse) {
            id = intent.getLongExtra("id", 0);
            spinner.setSelection(adapterGroup.getPosition(groupDb.selectByName(intent.getStringExtra("group"))));
            editAmout.setText(intent.getDoubleExtra("amount", 0)+"");
            editComment.setText(intent.getStringExtra("comment"));
        }
        else {
            id = 0;
        }

        GregorianCalendar date = Conversion.stringToDate(intent.getStringExtra("date"));
        datePicker.updateDate(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
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
    public void finish() {
        Intent data = new Intent();
        data.putExtra("new_deepanse", newDeepAnse);
        data.putExtra("id", id);
        data.putExtra("date", Conversion.dateToString(new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth())));
        data.putExtra("group", spinner.getSelectedItem().toString());
        data.putExtra("comment", editComment.getText().toString());
        try {
            data.putExtra("amount", Double.parseDouble(editAmout.getText().toString()));
        } catch(NumberFormatException e) {
            data.putExtra("amount", 0);
        }

        if(buttonSavePressed)
            setResult(RESULT_OK, data);
        else
            setResult(RESULT_CANCELED, data);

        super.finish();
    }

    public void eventSaveDeepAnse(View view) {
        buttonSavePressed = true;
        finish();
    }
}

