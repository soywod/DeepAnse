package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewDeepAnseDetail;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

public class ListDeepAnseDetail extends DeepAnse implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

    private ListView listView;
    private ListViewDeepAnseDetail adapter;

    private GregorianCalendar date;
    private DeepAnseGroup group;
    private double amount;
    private String comment;

    private static boolean longClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_deepanse_detail);

        initComponent();
        initData();
    }

    public void initComponent() {
        listView = (ListView) findViewById(R.id.listview);

        listView.setDividerHeight(1);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void initData() {
        Intent data = getIntent();

        if (data.getStringExtra("date") != null) date = Conversion.stringToDate(data.getStringExtra("date"));
        else date = null;

        if (data.getStringExtra("group") != null) group = groupDb.selectByName(data.getStringExtra("group"));
        else group = null;

        if (data.getDoubleExtra("amount", 0) != 0) amount = data.getDoubleExtra("amount", 0);
        else amount = 0;

        if (data.getStringExtra("comment") != null) comment = data.getStringExtra("comment");
        else comment = null;

        arrayDeepAnse = new ArrayList<>();
        adapter = new ListViewDeepAnseDetail(this, arrayDeepAnse);
        listView.setAdapter(adapter);

        setActionBarTitle(data.getIntExtra("title", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData() {
        arrayDeepAnse.clear();
        createCollection();
        adapter.notifyDataSetChanged();

        if (arrayDeepAnse.size() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_result), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void createCollection() {
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> tmpArray = deepAnseDb.selectAllBySearch(date, group, amount, comment);
        if (tmpArray != null)
            for (fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse : tmpArray)
                arrayDeepAnse.add(deepAnse);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!longClicked) {
            Intent intent = new Intent(ListDeepAnseDetail.this, EditDeepAnse.class);
            intent.putExtra("new_deepanse", false);
            intent.putExtra("id", arrayDeepAnse.get(position).getId());
            intent.putExtra("date", Conversion.dateToString(arrayDeepAnse.get(position).getDate()));
            intent.putExtra("group", arrayDeepAnse.get(position).getGroup().getName());
            intent.putExtra("amount", arrayDeepAnse.get(position).getAmount());
            intent.putExtra("comment", arrayDeepAnse.get(position).getComment());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        longClicked = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(ListDeepAnseDetail.this, 2);
        builder.setMessage(getString(R.string.prompt_del_deepanse));
        builder.setPositiveButton(getString(R.string.yes), new AlertBox() {
            @Override
            public void execute() {
                deepAnseDb.delete(arrayDeepAnse.get(position).getId());
                Toast.makeText(getApplicationContext(), getString(R.string.deleted_deepense), Toast.LENGTH_SHORT).show();
                refreshData();
                longClicked = false;
            }
        });
        builder.setNegativeButton(getString(R.string.no), new AlertBox() {
            @Override
            public void execute() {
                longClicked = false;
            }
        });
        builder.show();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_deepanse_detail, menu);
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
}
