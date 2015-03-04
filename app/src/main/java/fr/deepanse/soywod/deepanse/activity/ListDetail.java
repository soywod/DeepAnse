package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewDetail;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

public class ListDetail extends DeepAnse implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

    private ListView listView;
    private ListViewDetail adapter;

    private int year, month, day;
    private DeepAnseGroup group;
    private double amount;
    private String comment;

    private static boolean longClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

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

        if (data.getIntExtra("year", -1) != -1) year = data.getIntExtra("year", -1);
        else year = -1;

        if (data.getIntExtra("month", -1) != -1) month = data.getIntExtra("month", -1);
        else month = -1;

        if (data.getIntExtra("day", -1) != -1) day = data.getIntExtra("day", -1);
        else day = -1;

        if (data.getStringExtra("group") != null) group = groupDb.selectByName(data.getStringExtra("group"));
        else group = null;

        if (data.getDoubleExtra("amount", 0) != 0) amount = data.getDoubleExtra("amount", 0);
        else amount = 0;

        if (data.getStringExtra("comment") != null) comment = data.getStringExtra("comment");
        else comment = null;

        arrayDeepAnse = new ArrayList<>();
        adapter = new ListViewDetail(this, arrayDeepAnse);
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
            showShortToast(R.string.toast_no_result);
            finish();
        }

    }

    private void createCollection() {
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> tmpArray = deepAnseDb.selectAllBySearch(year, month, day, group, amount, comment);
        if (tmpArray != null)
            for (fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse : tmpArray)
                arrayDeepAnse.add(deepAnse);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!longClicked) {
            Intent intent = new Intent(ListDetail.this, Edit.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ListDetail.this, 2);
        builder.setMessage(getString(R.string.prompt_del_deepanse));
        builder.setPositiveButton(getString(R.string.yes), new AlertBox() {
            @Override
            public void execute() {
                deepAnseDb.delete(arrayDeepAnse.get(position).getId());
                showShortToast(R.string.toast_deleted_deepense);
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
        getMenuInflater().inflate(R.menu.menu_edit_deepanse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.home :
                Intent intent = new Intent(ListDetail.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
