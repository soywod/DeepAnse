package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditGroup extends Activity {

    private static boolean buttonSavePressed;

    private boolean newGroup;
    private long id;
    private ColorPicker picker;
    private EditText editGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        buttonSavePressed = false;

        Intent intent = getIntent();

        newGroup = ((intent == null) || (intent.getBooleanExtra("new_group", true)));

        SVBar svBar = (SVBar) findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacity_bar);
        editGroup = (EditText) findViewById(R.id.edit_name);
        picker = (ColorPicker) findViewById(R.id.picker);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setShowOldCenterColor(false);

        if (!newGroup) {
            id = intent.getLongExtra("id", 0);
            picker.setColor(intent.getIntExtra("color", Color.GREEN));
            editGroup.setText(intent.getStringExtra("name"));
        }
        else {
            id = 0;
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("new_group", newGroup);
        data.putExtra("id", id);
        data.putExtra("color", picker.getColor());
        data.putExtra("name", editGroup.getText().toString());

        if(buttonSavePressed)
            setResult(RESULT_OK, data);
        else
            setResult(RESULT_CANCELED, data);

        super.finish();
    }

    public void eventSaveGroup(View view) {
        buttonSavePressed = true;
        finish();
    }
}

