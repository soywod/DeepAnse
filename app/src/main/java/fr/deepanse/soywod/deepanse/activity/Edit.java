package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import fr.deepanse.soywod.deepanse.adapter.SpinnerGroup;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 11/02/2015.
 * Activity that permits user to edit expense or create new one by hand, extends activity.DeepAnse
 *
 * @author soywod
 */
public class Edit extends DeepAnse {

    /**
     *  The id of the expense
     */
    private long id;

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

    /**
     *  Boolean if new expense by hand or edit expense
     */
    private boolean newDeepAnse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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

        spinnerGroup.setAdapter(new SpinnerGroup(Edit.this, groupDb.selectAll()));
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
                        Edit.this,
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
        Intent data = getIntent();

        newDeepAnse = data.getBooleanExtra("new_deepanse", true);

        // If it is an expense edit, retrieve expense data, else create a new one
        if (!newDeepAnse) {
            id = data.getLongExtra("id", 0);
            mainDate = Conversion.stringToDate(data.getStringExtra("date"));
            spinnerGroup.setSelection(groupDb.selectAllGroupName().indexOf(data.getStringExtra("group")));
            editAmout.setText(data.getDoubleExtra("amount", 0) + "");
            editComment.setText(data.getStringExtra("comment"));
            buttonDatePicker.setText(dateToStringFrExplicit(mainDate));

            setActionBarTitle(R.string.title_edit_deepanse);
        }
        else {
            id = 0;
            mainDate = new GregorianCalendar();
            buttonDatePicker.setText(getString(R.string.today));

            findViewById(R.id.button_delete).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.layout_buttons)).setWeightSum(2);

            setActionBarTitle(R.string.title_create_deepanse);
        }
    }

    /**
     *  Event triggered by clicking on the delete button.
     *
     *  Ask user to confirm deletion, if accepted then delete current expense.
     *
     *  @param view     The view concerned
     */
    public void eventDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this, 2);
        builder.setMessage(getString(R.string.prompt_del_deepanse));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deepAnseDb.delete(id);
                showShortToast(R.string.toast_deleted_deepense);
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    /**
     *  Event triggered by clicking on the save button.
     *
     *  If the amount is not null, then update the expense (in edit mode) else create the new one (in creation mode).
     *
     *  @param view     The view concerned
     */
    public void eventSave(View view) {
        if (!editAmout.getText().toString().isEmpty()) {
            fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = new fr.deepanse.soywod.deepanse.model.DeepAnse(
                    0,
                    Double.parseDouble(editAmout.getText().toString()),
                    mainDate,
                    groupDb.selectByName(spinnerGroup.getSelectedItem().toString()),
                    editComment.getText().toString(),
                    false
            );

            if (newDeepAnse) {
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
                Intent intent = new Intent(Edit.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

