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

/**
 * Created by soywod on 27/02/2015.
 * Activity that permits user to list all expenses group, extends activity.DeepAnse
 *
 * @author soywod
 */
public class ListGroup extends DeepAnse implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    /**
     * ArrayList<DeepAnse> of expenses groups
     */
    private ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayDeepAnseGroup = new ArrayList<>();

    /**
     * ListView of data
     */
    private ListView listView;

    /**
     * ListView's adapter
     */
    private ListViewGroup adapter;

    /**
     * Boolean if long clicked or not
     */
    private static boolean longClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        initComponent();
        initData();
    }

    /**
     * Components initializer.
     */
    public void initComponent() {
        listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    /**
     * Data initializer.
     */
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

    /**
     * Function that refreshes all data
     */
    public void refreshData() {
        arrayDeepAnseGroup.clear();
        createCollection();
        adapter.notifyDataSetChanged();
    }

    /**
     * Function that creates the collection of all expenses groups.
     */
    private void createCollection() {
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> tmpArray = groupDb.selectAll();
        for (fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup : tmpArray)
            arrayDeepAnseGroup.add(deepAnseGroup);
    }

    /**
     *  Event triggered by clicking on a ListView item.
     *
     *  If it is not in long click mode, start a new EditGroup activity with current expense group selected.
     *
     *  @param parent       The parent adapter
     *  @param position     The child position
     *  @param id           The child id
     */
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

    /**
     *  Event triggered by long-clicking on a ListView item.
     *
     *  Delete the current expense group.
     *
     *  @param parent       The parent adapter
     *  @param position     The child position
     *  @param id           The child id
     */
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

            // If back arrow clicked, close this activity
            case android.R.id.home:
                finish();
                break;

            // If home clicked, start new Home activity deleting the others and close this one
            case R.id.menu_home :
                Intent intent = new Intent(ListGroup.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
