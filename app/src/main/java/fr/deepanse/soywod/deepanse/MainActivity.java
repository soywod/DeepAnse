package fr.deepanse.soywod.deepanse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.adapter.ReportByMonth;
import fr.deepanse.soywod.deepanse.adapter.ReportByYear;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DateFR;
import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.Report;

public class MainActivity extends ActionBarActivity {

    private final static int CODE_ACTIVITY_BY_YEAR = 0;
    private final static int CODE_ACTIVITY_BY_MONTH = 1;
    private final static int CODE_ACTIVITY_BY_DAY = 2;

    private AlertBox alertBox;
    private Activity context;
    private int selectedId;
    private int codeActivityActive;
    private GregorianCalendar mainDate = new GregorianCalendar();

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    private ArrayList<DeepAnse> arrayDeepAnse;
    private ArrayList<Report> arrayReport;
    private fr.deepanse.soywod.deepanse.adapter.DeepAnse adapterDeepAnse;
    private ReportByYear adapterReportByYear;
    private ReportByMonth adapterReportByMonth;

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

        startViewByYearActivity();

        //groupDb.update(1, new DeepAnseGroup(0, "bouffe", Color.parseColor("#AAFE8801")));
        //groupDb.insert(new DeepAnseGroup(0, "travail", Color.parseColor("#AA017FFE")));
        //deepAnseDb.insert(new DeepAnse(0, 9999.99, new GregorianCalendar(), groupDb.select(5), "efzfzgfzgqergerghqethqaethjqthjrthqergqzeryqethyqe", false));

        /*
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
        */
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

    public void openDatabases() {
        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}
    }

    public void startViewByYearActivity() {
        setContentView(R.layout.activity_view_deepanse);

        codeActivityActive = CODE_ACTIVITY_BY_YEAR;
        context = this;
        selectedId = -1;
        deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);
        arrayReport = new ArrayList<>();
        adapterReportByYear = new ReportByYear(this, arrayReport);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);
        listViewDeepAnse.setAdapter(adapterReportByYear);
        listViewDeepAnse.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainDate.set(GregorianCalendar.MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.MONTH));
                mainDate.set(GregorianCalendar.YEAR, arrayReport.get(position).getDate().get(GregorianCalendar.YEAR));
                startViewByMonthActivity();
            }
        });

        openDatabases();
        forwardMainDate(0);
    }

    public void startViewByMonthActivity() {
        setContentView(R.layout.activity_view_deepanse);

        codeActivityActive = CODE_ACTIVITY_BY_MONTH;
        context = this;
        selectedId = -1;
        deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);
        arrayReport = new ArrayList<>();
        adapterReportByMonth = new ReportByMonth(this, arrayReport);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);
        listViewDeepAnse.setAdapter(adapterReportByMonth);
        listViewDeepAnse.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainDate.set(GregorianCalendar.DAY_OF_MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.DAY_OF_MONTH));
                mainDate.set(GregorianCalendar.MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.MONTH));
                mainDate.set(GregorianCalendar.YEAR, arrayReport.get(position).getDate().get(GregorianCalendar.YEAR));
                startViewByDayActivity();
            }
        });

        openDatabases();
        forwardMainDate(0);
    }

    public void startViewByDayActivity() {
        setContentView(R.layout.activity_add_deepanse);

        codeActivityActive = CODE_ACTIVITY_BY_DAY;
        context = this;
        selectedId = -1;
        deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);
        arrayDeepAnse = new ArrayList<>();
        adapterDeepAnse = new fr.deepanse.soywod.deepanse.adapter.DeepAnse(this, arrayDeepAnse);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);
        listViewDeepAnse.setAdapter(adapterDeepAnse);

        openDatabases();
        forwardMainDate(0);
    }

    public void forwardMainDate(int count) {

        switch (codeActivityActive) {

            case CODE_ACTIVITY_BY_YEAR : {
                double total = 0;
                mainDate.add(GregorianCalendar.YEAR, count);

                arrayReport.clear();

                for(Report item : deepAnseDb.selectAllByYear(mainDate)) {
                    arrayReport.add(item);
                    total += item.getTotal();
                }

                adapterReportByYear.notifyDataSetChanged();

                refreshMainDate(mainDate.get(GregorianCalendar.YEAR)+"");
                refreshTotal(total);
                break;
            }

            case CODE_ACTIVITY_BY_MONTH : {
                double total = 0;
                mainDate.add(GregorianCalendar.MONTH, count);

                arrayReport.clear();

                for(Report item : deepAnseDb.selectAllByMonth(mainDate)) {
                    arrayReport.add(item);
                    total += item.getTotal();
                }

                adapterReportByMonth.notifyDataSetChanged();

                refreshMainDate(Conversion.dateToStringMonthYearFr(mainDate));
                refreshTotal(total);
                break;
            }

            case CODE_ACTIVITY_BY_DAY : {
                double total = 0;
                mainDate.add(GregorianCalendar.DAY_OF_MONTH, count);

                arrayDeepAnse.clear();

                for(DeepAnse item : deepAnseDb.selectAllByDay(mainDate)) {
                    arrayDeepAnse.add(item);
                    total += item.getAmount();
                }

                adapterDeepAnse.notifyDataSetChanged();

                refreshMainDate(Conversion.dateToStringDayMonthYearFr(mainDate));
                refreshTotal(total);
                break;
            }
        }
    }

    public void eventAddDeepAnse(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.prompt_recognizer);
        startActivityForResult(intent, 0);
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

                System.out.println("DATE : " + Conversion.dateToStringDayMonthYearFr(date));
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
                forwardMainDate(0);
            }
        }
    }

    public void refreshMainDate(String date) {
        Button buttonTotal = (Button) findViewById(R.id.button_main_date);
        buttonTotal.setText(date);
    }

    public void refreshTotal(double total) {
        TextView textViewTotal = (TextView) findViewById(R.id.text_total);
        textViewTotal.setText("Total : " + total);
    }

    public void eventBackwardDate(View v) {
        forwardMainDate(-1);
    }

    public void eventForwardDate(View v) {
        forwardMainDate(1);
    }

    public void eventBackwardTypeActivity(View v) {
        switch (codeActivityActive) {
            case CODE_ACTIVITY_BY_DAY : {
                startViewByMonthActivity();
                break;
            }

            case CODE_ACTIVITY_BY_MONTH : {
                startViewByYearActivity();
                break;
            }

            case CODE_ACTIVITY_BY_YEAR : {
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            eventBackwardTypeActivity(null);
        }

        return false;
    }
}
