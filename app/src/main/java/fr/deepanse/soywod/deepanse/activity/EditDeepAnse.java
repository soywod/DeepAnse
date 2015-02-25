package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditDeepAnse extends DeepAnse {

    private Spinner spinner;
    private EditText editAmout, editComment;
    private Button buttonDatePicker;

    private GregorianCalendar main_date;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private boolean newDeepAnse;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deepanse);

        initComponent();
        initData();
    }

    public void initComponent() {
        buttonDatePicker = (Button) findViewById(R.id.button_date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        spinner.setAdapter(new SpinnerDeepAnseGroup(EditDeepAnse.this, groupDb.selectAll()));
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                main_date.set(GregorianCalendar.YEAR, year);
                main_date.set(GregorianCalendar.MONTH, month);
                main_date.set(GregorianCalendar.DAY_OF_MONTH, day);

                buttonDatePicker.setText(dateToStringFrExplicit(main_date));
            }
        };
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        EditDeepAnse.this,
                        datePickerListener,
                        main_date.get(GregorianCalendar.YEAR),
                        main_date.get(GregorianCalendar.MONTH),
                        main_date.get(GregorianCalendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    public void initData() {
        Intent data = getIntent();


        newDeepAnse = data.getBooleanExtra("new_deepanse", true);

        if (!newDeepAnse) {
            main_date = Conversion.stringToDate(data.getStringExtra("date"));
            id = data.getLongExtra("id", 0);
            spinner.setSelection(groupDb.selectAllGroupName().indexOf(data.getStringExtra("group")));
            editAmout.setText(data.getDoubleExtra("amount", 0) + "");
            editComment.setText(data.getStringExtra("comment"));
            buttonDatePicker.setText(dateToStringFrExplicit(main_date));

            setActionBarTitle(R.string.title_edit_deepanse);
        }
        else {
            main_date = new GregorianCalendar();
            id = 0;
            buttonDatePicker.setText(getString(R.string.today));

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
                showShortToast(R.string.toast_deleted_deepense);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    public void eventSave(View view) {
        if (!editAmout.getText().toString().isEmpty()) {
            fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = new fr.deepanse.soywod.deepanse.model.DeepAnse(
                    0,
                    Double.parseDouble(editAmout.getText().toString()),
                    main_date,
                    groupDb.selectByName(spinner.getSelectedItem().toString()),
                    editComment.getText().toString(),
                    false
            );

            if (id == 0) {
                deepAnseDb.insert(deepAnse);
                showShortToast(R.string.toast_inserted_deepense);
            } else {
                deepAnseDb.update(id, deepAnse);
                showShortToast(R.string.toast_updated_deepense);
            }

            finish();
        }
        else {
            showShortToast(R.string.toast_need_amount);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAmout.getWindowToken(), 0);
            editAmout.clearFocus();
        }
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

