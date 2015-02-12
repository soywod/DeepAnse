package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 11/02/2015.
 */
public class SetColor extends Activity implements ColorPicker.OnColorChangedListener {

    private static boolean BUTTON_SETCOLOR_PRESSED;
    private ColorPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_color);

        BUTTON_SETCOLOR_PRESSED = false;

        Intent intent = getIntent();

        int newColor = ((intent != null)?(intent.getIntExtra("color", Color.GRAY)):(Color.GRAY));

        picker = (ColorPicker) findViewById(R.id.picker);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);

        picker.addSVBar(svBar);

        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(this);
        picker.setNewCenterColor(newColor);
        picker.setAlpha((float) 0.2);
    }

    @Override
    public void onColorChanged(int color) {
    }

    public int getColor() {
        return picker.getColor();
    }

    public int getColorLessOpacity() {
        return Color.argb(51, Color.red(picker.getColor()), Color.green(picker.getColor()), Color.blue(picker.getColor()));
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("color", getColorLessOpacity());

        if(BUTTON_SETCOLOR_PRESSED)
            setResult(RESULT_OK, data);
        else
            setResult(RESULT_CANCELED, data);

        super.finish();
    }

    public void eventSetColor(View view) {
        BUTTON_SETCOLOR_PRESSED = true;
        finish();
    }
}

