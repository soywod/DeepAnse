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

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ListViewDeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.AlertBox;

public class ListDeepAnseGroup extends DeepAnse implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayDeepAnseGroup = new ArrayList<>();

    private ListView listView;
    private ListViewDeepAnseGroup adapter;

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

        listView.setDividerHeight(5);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void initData() {
        arrayDeepAnseGroup = new ArrayList<>();
        adapter = new ListViewDeepAnseGroup(this, arrayDeepAnseGroup);
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
            Intent intent = new Intent(ListDeepAnseGroup.this, EditDeepAnseGroup.class);
            intent.putExtra("new_group", false);
            intent.putExtra("id", arrayDeepAnseGroup.get(position).getId());
            intent.putExtra("name", arrayDeepAnseGroup.get(position).getName());
            intent.putExtra("color", arrayDeepAnseGroup.get(position).getColor());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position > 0) {
            longClicked = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(ListDeepAnseGroup.this, 2);
            builder.setMessage(getString(R.string.prompt_del_group));
            builder.setPositiveButton(getString(R.string.yes), new AlertBox() {
                @Override
                public void execute() {
                    groupDb.updateAllById(arrayDeepAnseGroup.get(position).getId());
                    groupDb.delete(arrayDeepAnseGroup.get(position).getId());
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_deleted_group), Toast.LENGTH_SHORT).show();
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
        }
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
                Intent intent = new Intent(ListDeepAnseGroup.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
