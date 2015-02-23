package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DateFR;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
public class CreateDeepAnse extends DeepAnse {

    private EditText editFull, editReduce, editAmout, editComment;
    private TextView textInfo;
    private DatePicker datePicker;
    private Spinner spinner;

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
        setContentView(R.layout.activity_create_deepanse);

        initComponent();
        initExtract(getIntent().getStringExtra("best_match"));
    }

    public void initComponent() {
        editFull = (EditText) findViewById(R.id.edit_full);
        editReduce = (EditText) findViewById(R.id.edit_reduce);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        spinner = (Spinner) findViewById(R.id.spinner_group);
        editAmout = (EditText) findViewById(R.id.edit_amount);
        editComment = (EditText) findViewById(R.id.edit_comment);
        textInfo = (TextView) findViewById(R.id.text_info);

        datePicker.setCalendarViewShown(false);
        spinner.setAdapter(new SpinnerDeepAnseGroup(CreateDeepAnse.this, groupDb.selectAll()));

        findViewById(R.id.button_save).setEnabled(false);
    }

    public void initExtract(String bestMatch) {
        initRegexGroup();

        editFull.setText(bestMatch);

        bestMatch = Conversion.spellOutToNumber(bestMatch);
        editReduce.setText(bestMatch);

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
            group = "default";
        }

        if (amount != 0) {
            textInfo.setText(getString(R.string.deepanse_recognized));
            findViewById(R.id.layout_date).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_group).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_amount).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_comment).setVisibility(View.VISIBLE);

            datePicker.updateDate(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
            spinner.setSelection(groupDb.selectAllGroupName().indexOf(group));
            editAmout.setText(amount+"");
            editComment.setText(bestMatch);

            findViewById(R.id.button_save).setEnabled(true);
        }
        else {
            textInfo.setText(getString(R.string.deepanse_not_recognized));
            findViewById(R.id.layout_date).setVisibility(View.GONE);
            findViewById(R.id.layout_group).setVisibility(View.GONE);
            findViewById(R.id.layout_amount).setVisibility(View.GONE);
            findViewById(R.id.layout_comment).setVisibility(View.GONE);

            findViewById(R.id.button_save).setEnabled(false);
        }
    }

    public void eventAgain(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_deepanse));
        startActivityForResult(intent, 0);
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
        deepAnseDb.insert(deepAnse);
        Toast.makeText(getApplicationContext(), getString(R.string.inserted_deepense), Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            initExtract(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
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
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
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

}

