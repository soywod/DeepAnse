package fr.deepanse.soywod.deepanse.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerGroup;
import fr.deepanse.soywod.deepanse.model.Connectivity;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DateFR;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class Create extends DeepAnse {

    private EditText editFull, editAmout, editComment;
    private TextView textInfo;
    private Button buttonDatePicker;
    private Spinner spinner;

    private GregorianCalendar main_date;
    private DatePickerDialog.OnDateSetListener datePickerListener;

    private Pattern regexGroup;
    private final Pattern regexAmount = Pattern.compile(".*?(([0-9]+?)( euro[s]?[ ]?|[^0-9])([0-9]*)).*?");
    private final Pattern regexDate = Pattern.compile(
    ".*?" +
        "(" +
        "(aujourd\'hui)|" +
        "(demain)|" +
        "(après.demain)|" +
        "(hier)|" +
        "(avant.hier)|" +
        "(" +
            "(lundi|mardi|mercredi|jeudi|vendredi|samedi|dimanche) (dernier|prochain))|" +
        "(" +
            "([0-9]{1,2}) " +
            "(janvier|février|mars|avril|mai|juin|juillet|août|septembre|octobre|novembre|décembre) " +
            "([0-9]{4})))" +
        ".*?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initComponent();

        initExtract(getIntent().getStringExtra("best_match"));
    }

    public void initComponent() {
        editFull = (EditText) findViewById(R.id.edit_full);
        buttonDatePicker = (Button) findViewById(R.id.button_date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);
        textInfo = (TextView) findViewById(R.id.text_info);

        spinner.setAdapter(new SpinnerGroup(Create.this, groupDb.selectAll()));
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
                        Create.this,
                        datePickerListener,
                        main_date.get(GregorianCalendar.YEAR),
                        main_date.get(GregorianCalendar.MONTH),
                        main_date.get(GregorianCalendar.DAY_OF_MONTH))
                        .show();
            }
        });

        findViewById(R.id.button_save).setEnabled(false);
    }

    public void initExtract(String bestMatch) {
        initRegexGroup();

        editFull.setText(bestMatch);

        bestMatch = Conversion.spellOutToNumber(bestMatch);
        Matcher matcherDate = regexDate.matcher(bestMatch);

        double amount = 0;
        String group = "";
        GregorianCalendar date = new GregorianCalendar();

        if (matcherDate.matches()) {
            if (matcherDate.group(1) != null)
                bestMatch = bestMatch.replace(matcherDate.group(1), "");

            if (matcherDate.group(3) != null)
                date.add(GregorianCalendar.DATE, 1);

            if (matcherDate.group(4) != null)
                date.add(GregorianCalendar.DATE, 2);

            if (matcherDate.group(5) != null)
                date.add(GregorianCalendar.DATE, -1);

            if (matcherDate.group(6) != null)
                date.add(GregorianCalendar.DATE, -2);

            if (matcherDate.group(7) != null) {
                int tmpDiffDay = DateFR.findDateNumeric(matcherDate.group(8)) - date.get(GregorianCalendar.DAY_OF_WEEK);

                if (matcherDate.group(9).equals("prochain")) {
                    if (tmpDiffDay < 1) tmpDiffDay += 7;
                } else if (matcherDate.group(9).equals("dernier")) {
                    if (tmpDiffDay > -1) tmpDiffDay -= 7;
                }

                date.add(GregorianCalendar.DATE, tmpDiffDay);
            }

            if (matcherDate.group(10) != null)
                date.set(Integer.parseInt(matcherDate.group(13)), DateFR.findDateNumeric(matcherDate.group(12)), Integer.parseInt(matcherDate.group(11)));

        }

        Matcher matcherAmount = regexAmount.matcher(bestMatch);

        if (matcherAmount.matches()) {
            if (matcherAmount.group(1) != null)
                bestMatch = bestMatch.replace(matcherAmount.group(1), "").trim();

            if (matcherAmount.group(2) != null)
                amount = Double.parseDouble(matcherAmount.group(2));

            if (!matcherAmount.group(4).isEmpty())
                amount += (Double.parseDouble(matcherAmount.group(4)) / 100);
        }

        if(regexGroup != null) {
            Matcher matcherGroup = regexGroup.matcher(bestMatch);

            if (matcherGroup.matches()) {
                group = matcherGroup.group(1);
                bestMatch = bestMatch.replace(group, "").trim();
            }
        }

        if (group.trim().isEmpty()) {
            group = "divers";
        }

        if (amount != 0) {
            textInfo.setVisibility(View.GONE);

            findViewById(R.id.layout_date).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_group).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_amount).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);

            main_date = new GregorianCalendar(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
            buttonDatePicker.setText(dateToStringFrExplicit(main_date));
            spinner.setSelection(groupDb.selectAllGroupName().indexOf(group));
            editAmout.setText(amount+"");
            editComment.setText(bestMatch);

            findViewById(R.id.button_save).setEnabled(true);
        }
        else {
            textInfo.setVisibility(View.VISIBLE);

            findViewById(R.id.layout_date).setVisibility(View.GONE);
            findViewById(R.id.layout_group).setVisibility(View.GONE);
            findViewById(R.id.layout_amount).setVisibility(View.GONE);
            findViewById(R.id.layout_comment).setVisibility(View.GONE);

            findViewById(R.id.button_save).setEnabled(false);
        }
    }

    public void eventAgain(View view) {
        if (Connectivity.isConnectedFast(Create.this)) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_create_deepanse));
            startActivityForResult(intent, 0);
        }
        else {
            showShortToast(R.string.toast_need_fast_connection);
            finish();
        }
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
            deepAnseDb.insert(deepAnse);
            showShortToast(R.string.toast_inserted_deepense);

            finish();
        }
        else {
            showShortToast(R.string.toast_need_amount);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            initExtract(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
        }
    }

    public void initRegexGroup() {
        ArrayList<DeepAnseGroup> tmpArrayGroup = groupDb.selectAllWithOutDefault();
        regexGroup = ((tmpArrayGroup == null)?(null):(Pattern.compile(getRegexGroup(tmpArrayGroup))));
    }

    public String getRegexGroup(ArrayList<DeepAnseGroup> array)
    {
        if (array != null) {
            String regex = ".*?(";
            for (int i = 0; i < array.size(); i++) {
                if (i > 0)
                    regex += "|";
                regex += array.get(i).getName();
            }
            regex += ").*?";

            return regex;
        }
        else {
            return null;
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
                Intent intent = new Intent(Create.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

