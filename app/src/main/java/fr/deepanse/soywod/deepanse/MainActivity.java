package fr.deepanse.soywod.deepanse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;

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


        //deepAnseDb.insert(new DeepAnse(0, 20.5, new GregorianCalendar(), groupDb.select(1), "test", false));

        arrayDeepAnse = deepAnseDb.selectAll();
        adapterDeepAnse = new fr.deepanse.soywod.deepanse.adapter.DeepAnse(this, arrayDeepAnse);

        listViewDeepAnse.setAdapter(adapterDeepAnse);

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
}
