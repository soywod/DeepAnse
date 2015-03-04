package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 25/02/2015.
 */
public class CreateTuto extends DeepAnse {

    private MediaPlayer soundExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tuto);

        initData();
    }

    public void initData() {
        soundExample = null;
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

            case R.id.home :
                Intent intent = new Intent(CreateTuto.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventPlayExample1(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_1", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    public void eventPlayExample2(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_2", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    public void eventPlayExample3(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_3", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    public void eventPlayExample4(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_4", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }
}
