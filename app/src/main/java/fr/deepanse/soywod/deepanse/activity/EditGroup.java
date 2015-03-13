package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 11/02/2015.
 * Activity that permits user to edit expense group or create new one, extends activity.DeepAnse
 *
 * @author soywod
 */
public class EditGroup extends DeepAnse {

    /**
     *  The id of the expense group
     */
    private long id;

    /**
     *  EditText that contains the expense group name
     */
    private EditText editName;

    /**
     *  ColorPicker that contains the expense group color
     */
    private ColorPicker colorPicker;

    /**
     *  Boolean if new expense group or edit one
     */
    private boolean newGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        initComponent();
        initData();

    }

    /**
     *  Components initializer.
     */
    public void initComponent() {
        colorPicker = (ColorPicker) findViewById(R.id.color_picker);
        editName = (EditText) findViewById(R.id.edit_name);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);

        colorPicker.addSVBar(svBar);
        colorPicker.setShowOldCenterColor(false);
    }

    /**
     *  Data initializer.
     */
    public void initData() {
        Intent data = getIntent();

        newGroup = data.getBooleanExtra("new_group", true);

        // If it is an expense group edit, retrieve expense group data, else create a new one
        if (!newGroup) {
            id = data.getLongExtra("id", 0);
            editName.setText(data.getStringExtra("name"));
            colorPicker.setColor(data.getIntExtra("color", Color.GREEN));

            setActionBarTitle(R.string.title_edit_group);
        }
        else {
            id = 0;
            colorPicker.setColor(Color.GREEN);

            findViewById(R.id.button_delete).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.layout_buttons)).setWeightSum(2);

            setActionBarIcon(R.drawable.add_group);
            setActionBarTitle(R.string.title_create_group);
        }
    }

    /**
     *  Event triggered by clicking on the delete button.
     *
     *  Ask user to confirm deletion, if accepted then delete current expense group.
     *
     *  @param view     The view concerned
     */
    public void eventDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditGroup.this, 2);
        builder.setMessage(getString(R.string.prompt_del_group));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                groupDb.updateAllById(id);
                groupDb.delete(id);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_deleted_group), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    /**
     *  Event triggered by clicking on the save button.
     *
     *  If the name is not null, then update the expense group (in edit mode) or create the new one (in creation mode).
     *
     *  @param view     The view concerned
     */
    public void eventSave(View view) {
        if (!editName.getText().toString().isEmpty()) {
            long error = 0;
            fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup = new fr.deepanse.soywod.deepanse.model.DeepAnseGroup(
                    0,
                    editName.getText().toString().toLowerCase(),
                    colorPicker.getColor()
            );

            try {
                if (newGroup)
                    error = groupDb.insert(deepAnseGroup);
                else
                    groupDb.update(id, deepAnseGroup);
            }
            catch (Exception e) {
                error = -1;
            }

            if(error == -1) {
                showShortToast(R.string.toast_group_already_exist);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
                editName.clearFocus();
            }
            else {
                if (id == 0)
                    showShortToast(R.string.toast_inserted_group);
                else
                    showShortToast(R.string.toast_updated_group);

                finish();
            }
        }
        else {
            showShortToast(R.string.toast_need_name);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
            editName.clearFocus();
        }
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
                Intent intent = new Intent(EditGroup.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

