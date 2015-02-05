package fr.deepanse.soywod.deepanse;

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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;

public class MainActivity extends ActionBarActivity {

    private ListView listViewDeepAnse;
    private ArrayList<DeepAnse> arrayDeepAnse;
    private fr.deepanse.soywod.deepanse.adapter.DeepAnse adapterDeepAnse;
    private float historicX = Float.NaN;
    private View selectedView;
    private int selectedPosition;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this, 2);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        listViewDeepAnse = (ListView) findViewById(R.id.list_view);

        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}

        //deepAnseDb.insert(new DeepAnse(0, 20.5, new GregorianCalendar(), groupDb.select(1), "test", false));

        arrayDeepAnse = deepAnseDb.selectAll();
        adapterDeepAnse = new fr.deepanse.soywod.deepanse.adapter.DeepAnse(this, arrayDeepAnse);
        listViewDeepAnse.setAdapter(adapterDeepAnse);

        groupDb.close();
        deepAnseDb.close();

        listViewDeepAnse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                builder.setMessage("Voulez-vous vraiment supprimer cette dépense ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("YES");
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("NO");
                    }
                });
                builder.show();
                return false;
            }
        });

        listViewDeepAnse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //selectedView.setX(event.getX()-historicX);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < - 50) {
                            System.out.println("ACTION");
                            return true;
                        } else if (event.getX() - historicX > 50) {

                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
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
}
