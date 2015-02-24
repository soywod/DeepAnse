package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditDeepAnse extends DeepAnse {

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

        initComponent();
        initData();
    }

    public void initComponent() {
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        datePicker.setCalendarViewShown(false);
        spinner.setAdapter(new SpinnerDeepAnseGroup(EditDeepAnse.this, groupDb.selectAll()));
    }

    public void initData() {
        Intent data = getIntent();

        newDeepAnse = data.getBooleanExtra("new_deepanse", true);

        if (!newDeepAnse) {
            GregorianCalendar date = Conversion.stringToDate(data.getStringExtra("date"));
            id = data.getLongExtra("id", 0);
            spinner.setSelection(groupDb.selectAllGroupName().indexOf(data.getStringExtra("group")));
            editAmout.setText(data.getDoubleExtra("amount", 0) + "");
            editComment.setText(data.getStringExtra("comment"));
            datePicker.updateDate(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));

            setActionBarTitle(R.string.title_edit_deepanse);
        }
        else {
            GregorianCalendar date = new GregorianCalendar();
            id = 0;
            datePicker.updateDate(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));

            findViewById(R.id.button_delete).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.layout_buttons)).setWeightSum(2);

            setActionBarTitle(R.string.title_create_deepanse);
        }
    }

    public void eventDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDeepAnse.this, 2);
        builder.setMessage(getString(R.string.prompt_del_deepanse));
        builder.setPositiveButton(getString(R.string.yes), new AlertBox() {
            @Override
            public void execute() {
                deepAnseDb.delete(id);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_deleted_deepense), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    public void eventSave(View view) {
        fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = new fr.deepanse.soywod.deepanse.model.DeepAnse(
                0,
                Double.parseDouble(editAmout.getText().toString()),
                new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()),
                groupDb.selectByName(spinner.getSelectedItem().toString()),
                editComment.getText().toString(),
                false
        );

        if (id == 0) {
            deepAnseDb.insert(deepAnse);
            Toast.makeText(getApplicationContext(), getString(R.string.toast_inserted_deepense), Toast.LENGTH_SHORT).show();
        }
        else {
            deepAnseDb.update(id, deepAnse);
            Toast.makeText(getApplicationContext(), getString(R.string.toast_updated_deepense), Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_deepanse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                eventCancel(null);
                break;

            case R.id.home :
                Intent intent = new Intent(EditDeepAnse.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

