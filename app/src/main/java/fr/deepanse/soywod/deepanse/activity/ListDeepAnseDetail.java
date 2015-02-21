package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewDeepAnseDetail;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

public class ListDeepAnseDetail extends ActionBarActivity {

    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

    private ListView listView;
    private ListViewDeepAnseDetail adapter;

    private GregorianCalendar date = new GregorianCalendar();
    private DeepAnseGroup group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_deepanse_detail);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        openDatabases();

        Intent intent = getIntent();
        if (intent != null) {
            date = Conversion.stringToDate(intent.getStringExtra("date"));
            group = groupDb.selectByName(intent.getStringExtra("group"));
        }

        createCollectionForDateGroup();

        TextView textGroup = (TextView) findViewById(R.id.text_group);
        TextView textDate = (TextView) findViewById(R.id.text_date);

        textGroup.setText("[" + group.getName() + "]");
        textGroup.setTextColor(group.getColor());

        textDate.setText(Conversion.dateToStringFr(date));

        adapter = new ListViewDeepAnseDetail(this, arrayDeepAnse);

        listView = (ListView) findViewById(R.id.listview);
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);

        setTitle(R.string.view_title_detail);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDatabases();
    }

    public void openDatabases() {
        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}
    }

    public void closeDatabases() {
        groupDb.close();
        deepAnseDb.close();
    }

    private void createCollectionForDateGroup() {
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> tmpArrayDateGroup = deepAnseDb.selectAllByDateGroup(date, group);
        if (tmpArrayDateGroup != null)
            for (fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse : tmpArrayDateGroup)
                arrayDeepAnse.add(deepAnse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_deepanse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
