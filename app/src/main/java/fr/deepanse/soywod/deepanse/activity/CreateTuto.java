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
 * Activity that explains to user how to create new expense by voice, extends activity.DeepAnse
 *
 * @author soywod
 */
public class CreateTuto extends DeepAnse {

    /**
     *  MediaPlayer that plays audio examples
     */
    private MediaPlayer soundExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tuto);

        initData();
    }

    /**
     *  Data initializer
     */
    public void initData() {
        soundExample = null;
    }

    /**
     *  Event triggered by clicking on the play example 1 button.
     *
     *  Plays audio example and show a little Toast to inform user.
     *
     *  @param view     The view concerned
     */
    public void eventPlayExample1(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_1", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    /**
     *  Event triggered by clicking on the play example 2 button.
     *
     *  Plays audio example and show a little Toast to inform user.
     *
     *  @param view     The view concerned
     */
    public void eventPlayExample2(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_2", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    /**
     *  Event triggered by clicking on the play example 3 button.
     *
     *  Plays audio example and show a little Toast to inform user.
     *
     *  @param view     The view concerned
     */
    public void eventPlayExample3(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_3", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
    }

    /**
     *  Event triggered by clicking on the play example 4 button.
     *
     *  Plays audio example and show a little Toast to inform user.
     *
     *  @param view     The view concerned
     */
    public void eventPlayExample4(View view) {
        if (soundExample != null) soundExample.stop();
        soundExample = MediaPlayer.create(this, getResources().getIdentifier("sound_tuto_example_4", "raw", getPackageName()));
        soundExample.start();

        Toast.makeText(getApplicationContext(), getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
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
            case R.id.home :
                Intent intent = new Intent(CreateTuto.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
