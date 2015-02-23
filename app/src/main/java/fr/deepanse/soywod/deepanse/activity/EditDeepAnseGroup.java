package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.AlertBox;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditDeepAnseGroup extends DeepAnse {

    private boolean newGroup;
    private long id;

    private ColorPicker colorPicker;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        initComponent();
        initData();

    }

    public void initComponent() {
        colorPicker = (ColorPicker) findViewById(R.id.color_picker);
        editName = (EditText) findViewById(R.id.edit_name);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);

        colorPicker.addSVBar(svBar);
        colorPicker.setShowOldCenterColor(false);
    }

    public void initData() {
        Intent data = getIntent();

        newGroup = data.getBooleanExtra("new_group", true);

        if (!newGroup) {
            id = data.getLongExtra("id", 0);
            editName.setText(data.getStringExtra("name"));
            colorPicker.setColor(data.getIntExtra("color", Color.GREEN));

            setActionBarIcon(R.drawable.edit_group);
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

    public void eventDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDeepAnseGroup.this, 2);
        builder.setMessage(getString(R.string.prompt_del_group));
        builder.setPositiveButton(getString(R.string.yes), new AlertBox() {
            @Override
            public void execute() {
                groupDb.updateAllById(id);
                groupDb.delete(id);
                Toast.makeText(getApplicationContext(), getString(R.string.deleted_group), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    public void eventSave(View view) {
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup deepAnseGroup = new fr.deepanse.soywod.deepanse.model.DeepAnseGroup(
                0,
                editName.getText().toString(),
                colorPicker.getColor()
        );

        if (id == 0) {
            groupDb.insert(deepAnseGroup);
            Toast.makeText(getApplicationContext(), getString(R.string.inserted_group), Toast.LENGTH_SHORT).show();
        }
        else {
            groupDb.update(id, deepAnseGroup);
            Toast.makeText(getApplicationContext(), getString(R.string.updated_group), Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

