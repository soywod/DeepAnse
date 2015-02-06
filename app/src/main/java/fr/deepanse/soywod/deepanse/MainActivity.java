package fr.deepanse.soywod.deepanse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
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

    private static Pattern regexAmount;
    private static Pattern regexGroup;

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

        System.out.println(groupDb.selectAll());
        //groupDb.insert(new DeepAnseGroup(0, "professionnel"));
        //deepAnseDb.insert(new DeepAnse(0, 9999.99, new GregorianCalendar(), groupDb.select(5), "efzfzgfzgqergerghqethqaethjqthjrthqergqzeryqethyqe", false));

        arrayDeepAnse = deepAnseDb.selectAll();
        adapterDeepAnse = new fr.deepanse.soywod.deepanse.adapter.DeepAnse(this, arrayDeepAnse);

        listViewDeepAnse.setAdapter(adapterDeepAnse);

        regexAmount = Pattern.compile(".*?([0-9]+?)( euro | euros |0|h)([0-9]*).*?");
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
        TextView textViewDate = (TextView) findViewById(R.id.text_date);
        textViewDate.setText(Conversion.dateToStringMonthYearFr(mainDate));
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

            System.out.println(bestMatch);

            bestMatch = Conversion.spellOutToNumber(bestMatch);

            System.out.println(bestMatch);

            Matcher matcherAmount = regexAmount.matcher(bestMatch);
            Matcher matcherGroup = regexGroup.matcher(bestMatch);

            double amount = 0;
            String group = "";

            if(matcherAmount.matches())
            {

                if(!matcherAmount.group(1).isEmpty())
                    amount = Double.parseDouble(matcherAmount.group(1));

                if(!matcherAmount.group(3).isEmpty())
                    amount += (Double.parseDouble(matcherAmount.group(3))/100);
            }

            if(matcherGroup.matches())
            {
                group = matcherGroup.group(1);
            }

            if(amount !=0 && group != "")
            {
                DeepAnse deepAnse = new DeepAnse(0, amount, new GregorianCalendar(), groupDb.selectByName(group), "test", false);
                deepAnse.setId(deepAnseDb.insert(deepAnse));
                arrayDeepAnse.add(deepAnse);
                adapterDeepAnse.notifyDataSetChanged();
            }
        }
    }
}
