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
 * Activity that permits user to search expense(s) according to a date, an amount, a group and a comment, extends activity.DeepAnse
 *
 * @author soywod
 */
public class Search extends DeepAnse {

    /**
     *  EditTexts that contain amount and comment
     */
    private EditText editAmout, editComment;

    /**
     *  Button that allows date changing
     */
    private Button buttonDatePicker;

    /**
     *  Spinner that allows group changing
     */
    private Spinner spinnerGroup;

    /**
     *  Main date selected
     */
    private GregorianCalendar mainDate;

    /**
     *  Listener for the datePicker
     */
    private DatePickerDialog.OnDateSetListener datePickerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initComponent();
        initData();
    }

    /**
     *  Components initializer.
     */
    public void initComponent() {
        buttonDatePicker = (Button) findViewById(R.id.button_date_picker);
        spinnerGroup = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);

        spinnerGroup.setAdapter(new SpinnerGroup(Search.this, groupDb.selectAll()));
        spinnerGroup.setEnabled(false);
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mainDate.set(GregorianCalendar.YEAR, year);
                mainDate.set(GregorianCalendar.MONTH, month);
                mainDate.set(GregorianCalendar.DAY_OF_MONTH, day);

                buttonDatePicker.setText(dateToStringFrExplicit(mainDate));
            }
        };
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        Search.this,
                        datePickerListener,
                        mainDate.get(GregorianCalendar.YEAR),
                        mainDate.get(GregorianCalendar.MONTH),
                        mainDate.get(GregorianCalendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    /**
     *  Data initializer.
     */
    public void initData() {
        mainDate = new GregorianCalendar();
        buttonDatePicker.setText(dateToStringFrExplicit(mainDate));
    }

    /**
     *  Event triggered by clicking on the date checkBox.
     *
     *  Enable the date picker button.
     *
     *  @param view     The view concerned
     */
    public void eventCheckDate(View view) {
        findViewById(R.id.button_date_picker).setEnabled(((CheckBox) view).isChecked());
    }

    /**
     *  Event triggered by clicking on the group checkBox.
     *
     *  Enable the group spinner.
     *
     *  @param view     The view concerned
     */
    public void eventCheckGroup(View view) {
        findViewById(R.id.spinner_group).setEnabled(((CheckBox) view).isChecked());
    }

    /**
     *  Event triggered by clicking on the amount checkBox.
     *
     *  Enable the amount EditText.
     *
     *  @param view     The view concerned
     */
    public void eventCheckAmount(View view) {
        findViewById(R.id.edit_amount).setEnabled(((CheckBox) view).isChecked());
    }

    /**
     *  Event triggered by clicking on the comment checkBox.
     *
     *  Enable the comment EditText.
     *
     *  @param view     The view concerned
     */
    public void eventCheckComment(View view) {
        findViewById(R.id.edit_comment).setEnabled(((CheckBox) view).isChecked());
    }

    /**
     *  Event triggered by clicking on the search button.
     *
     *  If the amount is not empty or not checked, start a new ListDetail activity with selected parameters.
     *
     *  @param view     The view concerned
     */
    public void eventSearch(View view) {
        if (!(((CheckBox) findViewById(R.id.check_amount)).isChecked() && editAmout.getText().length() == 0)) {
            Intent intent = new Intent(Search.this, ListDetail.class);

            intent.putExtra("title", R.string.title_find_deepanse);

            if (((CheckBox) findViewById(R.id.check_date)).isChecked()) {
                intent.putExtra("year", mainDate.get(GregorianCalendar.YEAR));
                intent.putExtra("month", mainDate.get(GregorianCalendar.MONTH));
                intent.putExtra("day", mainDate.get(GregorianCalendar.DAY_OF_MONTH));
            }

            if (((CheckBox) findViewById(R.id.check_group)).isChecked())
                intent.putExtra("group", spinnerGroup.getSelectedItem().toString());
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

            // If back arrow clicked, close this activity
            case android.R.id.home:
                finish();
                break;

            // If home clicked, start new Home activity deleting the others and close this one
            case R.id.menu_home :
                Intent intent = new Intent(Search.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

