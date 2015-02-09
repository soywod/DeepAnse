package fr.deepanse.soywod.deepanse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.DateFR;
import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

public class MainActivity extends ActionBarActivity {


    private AlertBox alertBox;
    private Activity context;
    private int selectedId;
    private GregorianCalendar mainDate;

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    private  ArrayList<DeepAnse> arrayDeepAnse;
    private fr.deepanse.soywod.deepanse.adapter.DeepAnse adapterDeepAnse;

    private static Pattern regexAmount, regexGroup, regexDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        selectedId = -1;
        mainDate = new GregorianCalendar();

        deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);

        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}

        //groupDb.update(1, new DeepAnseGroup(0, "bouffe", Color.parseColor("#AAFE8801")));
        //groupDb.insert(new DeepAnseGroup(0, "travail", Color.parseColor("#AA017FFE")));
        //deepAnseDb.insert(new DeepAnse(0, 9999.99, new GregorianCalendar(), groupDb.select(5), "efzfzgfzgqergerghqethqaethjqthjrthqergqzeryqethyqe", false));

        System.out.println(groupDb.selectAll());

        arrayDeepAnse = new ArrayList<>();
        adapterDeepAnse = new fr.deepanse.soywod.deepanse.adapter.DeepAnse(this, arrayDeepAnse);

        listViewDeepAnse.setAdapter(adapterDeepAnse);

        regexDate = Pattern.compile(
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

        regexAmount = Pattern.compile(".*?(([0-9]+?)( euro[s]?[ ]?|[^0-9])([0-9]*)).*?");
        regexGroup = Pattern.compile(getRegexGroup(groupDb.selectAll()));

        listViewDeepAnse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                selectedId = position;
                displayYesNoDialog();

                return false;
            }
        });

        alertBox = new AlertBox() {

            @Override
            public void execute() {
                deepAnseDb.delete(arrayDeepAnse.get(selectedId).getId());
                arrayDeepAnse.remove(selectedId);
                adapterDeepAnse.notifyDataSetChanged();
                selectedId = -1;
                refreshTotal();
            }
        };

        addMonth(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        groupDb.close();
        deepAnseDb.close();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshTotal() {
        TextView textViewTotal = (TextView) findViewById(R.id.text_total);
        textViewTotal.setText("Total : " + deepAnseDb.selectSumByDate(mainDate));
    }

    public void refreshMainDate() {
        TextView textViewDate = (TextView) findViewById(R.id.text_main_date);
        textViewDate.setText(Conversion.dateToStringMonthYearFr(mainDate));
    }

    public void eventRemoveMonth(View v) {
        addMonth(-1);
    }

    public void eventAddMonth(View v) {
        addMonth(1);
    }

    public void eventAddDeepAnse(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.prompt_recognizer);
        startActivityForResult(intent, 0);
    }

    public void addMonth(int count) {
        mainDate.add(GregorianCalendar.MONTH, count);
        arrayDeepAnse.clear();

        for(DeepAnse item : deepAnseDb.selectAllByDate(mainDate))
            arrayDeepAnse.add(item);

        Collections.sort(arrayDeepAnse);

        adapterDeepAnse.notifyDataSetChanged();

        refreshMainDate();
        refreshTotal();
    }

    public void displayYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 2);
        builder.setMessage("Voulez-vous vraiment supprimer cette dépense ?");
        builder.setPositiveButton("Oui", alertBox);
        builder.setNegativeButton("Non", null);
        builder.show();
    }

    public String getRegexGroup(ArrayList<DeepAnseGroup> array)
    {
        String regex = ".*?(";
        for(int i=0 ; i<array.size() ; i++)
        {
            if(i > 0)
                regex += "|";
            regex += array.get(i).getName();
        }
        regex += ").*?";

        return regex;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0 && resultCode == RESULT_OK) {
            String bestMatch = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase();

            System.out.println("BRUT : " + bestMatch);

            bestMatch = Conversion.spellOutToNumber(bestMatch);

            System.out.println("CONVERT : " + bestMatch);

            Matcher matcherDate = regexDate.matcher(bestMatch);

            double amount = 0;
            String group = "";
            GregorianCalendar date = new GregorianCalendar();


            if(matcherDate.matches())
            {
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

                if (matcherDate.group(7) != null)
                {
                    int tmpDiffDay = DateFR.findDateNumeric(matcherDate.group(8)) - date.get(GregorianCalendar.DAY_OF_WEEK);

                    if(matcherDate.group(9).equals("prochain")) {
                        if (tmpDiffDay < 1) tmpDiffDay += 7;
                    }
                    else if(matcherDate.group(9).equals("dernier")) {
                        if (tmpDiffDay > -1) tmpDiffDay -= 7;
                    }

                    date.add(GregorianCalendar.DATE, tmpDiffDay);
                }

                if (matcherDate.group(10) != null)
                    date.set(Integer.parseInt(matcherDate.group(13)), DateFR.findDateNumeric(matcherDate.group(12)), Integer.parseInt(matcherDate.group(11)));

                System.out.println("DATE : " + Conversion.dateToStringFr(date));
            }

            Matcher matcherAmount = regexAmount.matcher(bestMatch);

            if(matcherAmount.matches())
            {
                if(matcherAmount.group(1) != null)
                    bestMatch = bestMatch.replace(matcherAmount.group(1), "").trim();

                if(matcherAmount.group(2) != null)
                    amount = Double.parseDouble(matcherAmount.group(2));

                if(!matcherAmount.group(4).isEmpty())
                    amount += (Double.parseDouble(matcherAmount.group(4))/100);

                System.out.println("MONTANT : " + amount);
            }

            Matcher matcherGroup = regexGroup.matcher(bestMatch);

            if(matcherGroup.matches())
            {
                group = matcherGroup.group(1);
                bestMatch = bestMatch.replace(group, "").trim();
                System.out.println("RUBRIQUE : " + group);
            }

            System.out.println("COMMENT : " + bestMatch);

            if(amount != 0 && group != "")
            {
                DeepAnse deepAnse = new DeepAnse(0, amount, date, groupDb.selectByName(group), bestMatch, false);
                deepAnse.setId(deepAnseDb.insert(deepAnse));
                mainDate.set(GregorianCalendar.YEAR, date.get(GregorianCalendar.YEAR));
                mainDate.set(GregorianCalendar.MONTH, date.get(GregorianCalendar.MONTH));
                addMonth(0);
            }
        }
    }
}
