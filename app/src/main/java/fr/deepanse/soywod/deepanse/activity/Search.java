package fr.deepanse.soywod.deepanse.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class Search extends DeepAnse {

    private EditText editAmout, editComment;
    private Button buttonDatePicker;
    private Spinner spinner;

    private GregorianCalendar main_date;
    private DatePickerDialog.OnDateSetListener datePickerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initComponent();
        initData();
    }

    public void initComponent() {
        buttonDatePicker = (Button) findViewById(R.id.button_date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        spinner.setAdapter(new SpinnerGroup(Search.this, groupDb.selectAll()));
        spinner.setEnabled(false);
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
                        Search.this,
                        datePickerListener,
                        main_date.get(GregorianCalendar.YEAR),
                        main_date.get(GregorianCalendar.MONTH),
                        main_date.get(GregorianCalendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    public void initData() {
        main_date = new GregorianCalendar();
        buttonDatePicker.setText(dateToStringFrExplicit(main_date));
    }

    public void eventCheckDate(View view) {
        findViewById(R.id.button_date_picker).setEnabled(((CheckBox) view).isChecked());
    }

    public void eventCheckGroup(View view) {
        findViewById(R.id.spinner_group).setEnabled(((CheckBox) view).isChecked());
    }

    public void eventCheckAmount(View view) {
        findViewById(R.id.edit_amount).setEnabled(((CheckBox) view).isChecked());
    }

    public void eventCheckComment(View view) {
        findViewById(R.id.edit_comment).setEnabled(((CheckBox) view).isChecked());
    }

    public void eventSearch(View view) {
        if (!(((CheckBox) findViewById(R.id.check_amount)).isChecked() && editAmout.getText().length() == 0)) {
            Intent intent = new Intent(Search.this, ListDetail.class);

            intent.putExtra("title", R.string.title_find_deepanse);

            if (((CheckBox) findViewById(R.id.check_date)).isChecked()) {
                intent.putExtra("year", main_date.get(GregorianCalendar.YEAR));
                intent.putExtra("month", main_date.get(GregorianCalendar.MONTH));
                intent.putExtra("day", main_date.get(GregorianCalendar.DAY_OF_MONTH));
            }

            if (((CheckBox) findViewById(R.id.check_group)).isChecked())
                intent.putExtra("group", spinner.getSelectedItem().toString());
            if (((CheckBox) findViewById(R.id.check_amount)).isChecked())
                intent.putExtra("amount", Double.parseDouble(editAmout.getText().toString()));
            if (((CheckBox) findViewById(R.id.check_comment)).isChecked())
                intent.putExtra("comment", editComment.getText().toString());

            startActivity(intent);
        }
        else {
            showShortToast(R.string.toast_need_amount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_standard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_home :
                Intent intent = new Intent(Search.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

