package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewGroup;

public class ListGroup extends DeepAnse implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayDeepAnseGroup = new ArrayList<>();

    private ListView listView;
    private ListViewGroup adapter;

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

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void initData() {
        arrayDeepAnseGroup = new ArrayList<>();
        adapter = new ListViewGroup(this, arrayDeepAnseGroup);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData() {
        arrayDeepAnseGroup.clear();
        createCollection();
        adapter.notifyDataSetChanged();
    }

    private void createCollection() {
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> tmpArray = groupDb.selectAll();
        for (fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup : tmpArray)
            arrayDeepAnseGroup.add(deepAnseGroup);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!longClicked && position > 0) {
            Intent intent = new Intent(ListGroup.this, EditGroup.class);
            intent.putExtra("new_group", false);
            intent.putExtra("id", arrayDeepAnseGroup.get(position).getId());
            intent.putExtra("name", arrayDeepAnseGroup.get(position).getName());
            intent.putExtra("color", arrayDeepAnseGroup.get(position).getColor());
            startActivity(intent);
        }
        else if (position == 0) {
            showShortToast(R.string.toast_no_edit_possible);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position > 0) {
            longClicked = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(ListGroup.this, 2);
            builder.setMessage(getString(R.string.prompt_del_group));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    groupDb.updateAllById(arrayDeepAnseGroup.get(position).getId());
                    groupDb.delete(arrayDeepAnseGroup.get(position).getId());
                    showShortToast(R.string.toast_deleted_group);
                    refreshData();
                    longClicked = false;
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    longClicked = false;
                }
            });
            builder.show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_standard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_home :
                Intent intent = new Intent(ListGroup.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
