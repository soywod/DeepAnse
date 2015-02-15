package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 11/02/2015.
 */
public class EditGroup extends Activity implements ColorPicker.OnColorChangedListener {

    private static boolean buttonSavePressed;

    private ColorPicker picker;
    private EditText editGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        buttonSavePressed = false;

        Intent intent = getIntent();

        int oldColor = ((intent != null)?(intent.getIntExtra("color", Color.RED)):(Color.RED));
        String oldName = ((intent != null)?(intent.getStringExtra("name")):(""));

        picker = (ColorPicker) findViewById(R.id.picker);
        editGroup = (EditText) findViewById(R.id.edit_name);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);

        svBar.setAlpha((float) 0.3);
        editGroup.setText(oldName);

        picker.addSVBar(svBar);
        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(this);
        picker.setNewCenterColor(oldColor);
        picker.setAlpha((float) 0.3);
    }

    @Override
    public void onColorChanged(int color) {
    }

    @Override
    public void finish() {
        Intent data = new Intent();
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

