package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 11/02/2015.
 */
public class FindDeepAnse extends DeepAnse {

    private EditText editAmout, editComment;
    private DatePicker datePicker;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_deepanse);

        initComponent();
    }

    public void initComponent() {
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        datePicker.setCalendarViewShown(false);
        spinner.setAdapter(new SpinnerDeepAnseGroup(FindDeepAnse.this, groupDb.selectAll()));
    }

    public void eventCheckDate(View view) {
        if (((CheckBox) view).isChecked()) {
            findViewById(R.id.date_picker).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.date_picker).setVisibility(View.GONE);
        }
    }

    public void eventCheckGroup(View view) {
        if (((CheckBox) view).isChecked()) {
            findViewById(R.id.spinner_group).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.spinner_group).setVisibility(View.GONE);
        }
    }

    public void eventCheckAmount(View view) {
        if (((CheckBox) view).isChecked()) {
            findViewById(R.id.edit_amount).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.edit_amount).setVisibility(View.GONE);
        }
    }

    public void eventCheckComment(View view) {
        if (((CheckBox) view).isChecked()) {
            findViewById(R.id.edit_comment).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.edit_comment).setVisibility(View.GONE);
        }
    }

    public void eventFind(View view) {
        Intent intent = new Intent(FindDeepAnse.this, ListDeepAnseDetail.class);

        intent.putExtra("title", R.string.title_find_deepanse);

        if (((CheckBox) findViewById(R.id.check_date)).isChecked())
            intent.putExtra("date", Conversion.dateToString(new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth())));
        if (((CheckBox) findViewById(R.id.check_group)).isChecked())
            intent.putExtra("group", spinner.getSelectedItem().toString());
        if (((CheckBox) findViewById(R.id.check_amount)).isChecked())
            intent.putExtra("amount", Double.parseDouble(editAmout.getText().toString()));
        if (((CheckBox) findViewById(R.id.check_comment)).isChecked())
            intent.putExtra("comment", editComment.getText().toString());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_deepanse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.home :
                Intent intent = new Intent(FindDeepAnse.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

