package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.*;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DateFR;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 11/02/2015.
 */
abstract public class DeepAnse extends ActionBarActivity {

    private final static int RESULT_RECOGNIZER = 0;

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

    protected GregorianCalendar mainDate;

    protected fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    protected fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);

        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        openDatabases();
    }

    @Override
    protected void onResume() {
        super.onResume();
        forwardMainDate(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabases();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_manage_group) {
            startActivity(new Intent(DeepAnse.this, GroupManager.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void openDatabases() {
        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}
    }

    protected void closeDatabases() {
        groupDb.close();
        deepAnseDb.close();
    }

    protected void refreshMainDate(String date) {
        TextView textTotal = (TextView) findViewById(R.id.text_main_date);
        textTotal.setText(date);
    }

    public void refreshTotal(double total) {
        TextView textViewTotal = (TextView) findViewById(R.id.text_total);
        textViewTotal.setText(getString(R.string.view_total) + "\n" + total + " €");
    }

    public void refreshRegexGroup() {
        ArrayList<DeepAnseGroup> tmpArrayGroup = groupDb.selectAllWithOutDefault();
        regexGroup = ((tmpArrayGroup == null)?(null):(Pattern.compile(getRegexGroup(tmpArrayGroup))));
    }

    public void eventBackwardDate(View v) {
        forwardMainDate(-1);
    }

    public void eventForwardDate(View v) {
        forwardMainDate(1);
    }

    abstract protected void forwardMainDate(int count);

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

    public void eventAddDeepAnse(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_deepanse));
        startActivityForResult(intent, RESULT_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_RECOGNIZER && resultCode == RESULT_OK) {

            String bestMatch = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase();

            System.out.println("BRUT : " + bestMatch);

            bestMatch = Conversion.spellOutToNumber(bestMatch);

            System.out.println("CONVERT : " + bestMatch);

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

                System.out.println("MONTANT : " + amount);
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

            System.out.println("RUBRIQUE : " + group);

            System.out.println("COMMENT : " + bestMatch);

            System.out.println("DATE : " + Conversion.dateToStringDayMonthYearFr(date));

            if (amount != 0) {
                fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse = new fr.deepanse.soywod.deepanse.model.DeepAnse(0, amount, date, groupDb.selectByName(group), bestMatch, false);
                deepAnse.setId(deepAnseDb.insert(deepAnse));
                mainDate.set(GregorianCalendar.YEAR, date.get(GregorianCalendar.YEAR));
                mainDate.set(GregorianCalendar.MONTH, date.get(GregorianCalendar.MONTH));
                mainDate.set(GregorianCalendar.DAY_OF_MONTH, date.get(GregorianCalendar.DAY_OF_MONTH));

                if (this.getClass() != ViewByDay.class) {
                    Intent intent = new Intent(DeepAnse.this, ViewByDay.class);
                    intent.putExtra("main_date", Conversion.dateToString(mainDate));
                    startActivity(intent);
                }
                else {
                    forwardMainDate(0);
                }
            }
        }
    }
}
