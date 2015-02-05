package fr.deepanse.soywod.deepanse;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.SQLException;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.DeepAnse;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        fr.deepanse.soywod.deepanse.database.DeepAnseGroup group = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        fr.deepanse.soywod.deepanse.database.DeepAnse deepAnse = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        try
        {
            group.open();
            deepAnse.open();
        }catch(SQLException e){e.printStackTrace();}

        /*
        DeepAnseGroup g1 = group.select(1);

        DeepAnse d1 = new DeepAnse(0, 15.5, new GregorianCalendar(), g1, "achat d'un gros cochon", false);
        d1.setId(deepAnse.insert(d1));

        System.out.println(d1.toString());

        */

        group.close();
        deepAnse.close();
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
