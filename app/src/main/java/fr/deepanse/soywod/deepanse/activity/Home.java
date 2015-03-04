package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Connectivity;
import fr.deepanse.soywod.deepanse.model.HomeAnimation;

/**
 * Created by soywod on 18/02/2015.
 */
public class Home extends ActionBarActivity implements View.OnClickListener {

    private boolean animRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        animRunning = false;

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setTitle(getString(R.string.title_home));
        actionBar.setIcon(getResources().getDrawable(R.mipmap.home));
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu));

        findViewById(R.id.button_add_deepanse_by_voice).setOnClickListener(this);
        findViewById(R.id.button_add_deepanse_by_hand).setOnClickListener(this);
        findViewById(R.id.button_list_deepanse).setOnClickListener(this);
        findViewById(R.id.button_find_deepanse).setOnClickListener(this);

        findViewById(R.id.button_add_group).setOnClickListener(this);
        findViewById(R.id.button_list_group).setOnClickListener(this);
        findViewById(R.id.button_import_group).setOnClickListener(this);

        findViewById(R.id.button_first_launch).setOnClickListener(this);
        findViewById(R.id.button_create_deepanse_tuto).setOnClickListener(this);

        checkFirstRun();
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, 2);
            builder.setMessage(getString(R.string.prompt_first_launch));
            builder.setPositiveButton(getString(R.string.ok), null);
            builder.show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
        }
    }

    @Override
    public void onClick(View v) {
        if(!animRunning) {
            Intent intent;

            switch (v.getId()) {
                case R.id.button_add_deepanse_by_voice:
                    if (Connectivity.isConnectedFast(Home.this)) {
                        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_create_deepanse));
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        startActivityForResult(intent, 0);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_need_fast_connection), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.button_add_deepanse_by_hand:
                    intent = new Intent(Home.this, Edit.class);
                    intent.putExtra("new_deepanse", true);
                    startActivity(intent);
                    break;

                case R.id.button_list_deepanse:
                    intent = new Intent(Home.this, List.class);
                    startActivity(intent);
                    break;

                case R.id.button_find_deepanse:
                    intent = new Intent(Home.this, Search.class);
                    startActivity(intent);
                    break;

                case R.id.button_add_group:
                    intent = new Intent(Home.this, EditGroup.class);
                    intent.putExtra("new_group", true);
                    startActivity(intent);
                    break;

                case R.id.button_list_group:
                    intent = new Intent(Home.this, ListGroup.class);
                    startActivity(intent);
                    break;

                case R.id.button_import_group:
                    intent = new Intent(Home.this, ImportGroup.class);
                    startActivity(intent);
                    break;

                case R.id.button_create_deepanse_tuto:
                    intent = new Intent(Home.this, CreateTuto.class);
                    startActivity(intent);
                    break;

                case R.id.button_first_launch:
                    animRunning = true;

                    MediaPlayer sound1 = MediaPlayer.create(this, getResources().getIdentifier("sound1", "raw", getPackageName()));
                    MediaPlayer sound2 = MediaPlayer.create(this, getResources().getIdentifier("sound2", "raw", getPackageName()));
                    MediaPlayer sound3 = MediaPlayer.create(this, getResources().getIdentifier("sound3", "raw", getPackageName()));
                    MediaPlayer sound4 = MediaPlayer.create(this, getResources().getIdentifier("sound4", "raw", getPackageName()));
                    MediaPlayer sound5 = MediaPlayer.create(this, getResources().getIdentifier("sound5", "raw", getPackageName()));
                    MediaPlayer sound6 = MediaPlayer.create(this, getResources().getIdentifier("sound6", "raw", getPackageName()));
                    MediaPlayer sound7 = MediaPlayer.create(this, getResources().getIdentifier("sound7", "raw", getPackageName()));
                    MediaPlayer sound8 = MediaPlayer.create(this, getResources().getIdentifier("sound8", "raw", getPackageName()));

                    HomeAnimation animation7 = new HomeAnimation(sound8, this, null, null, getString(R.string.toast_anim_find_deepanse));
                    HomeAnimation animation6 = new HomeAnimation(sound7, this, findViewById(R.id.button_find_deepanse), animation7, getString(R.string.toast_anim_list_deepanse));
                    HomeAnimation animation5 = new HomeAnimation(sound6, this, findViewById(R.id.button_list_deepanse), animation6, getString(R.string.toast_anim_add_deepanse_by_voice));
                    HomeAnimation animation4 = new HomeAnimation(sound5, this, findViewById(R.id.button_add_deepanse_by_voice), animation5, getString(R.string.toast_anim_add_deepanse_by_hand));
                    HomeAnimation animation3 = new HomeAnimation(sound4, this, findViewById(R.id.button_add_deepanse_by_hand), animation4, getString(R.string.toast_anim_list_group));
                    HomeAnimation animation2 = new HomeAnimation(sound3, this, findViewById(R.id.button_list_group), animation3, getString(R.string.toast_anim_import_group));
                    final HomeAnimation  animation1 = new HomeAnimation(sound2, this, findViewById(R.id.button_import_group), animation2, getString(R.string.toast_anim_add_group));

                    Toast.makeText(getApplicationContext(), getString(R.string.home_desc), Toast.LENGTH_LONG).show();
                    sound1.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.button_add_group).startAnimation(animation1);
                        }
                    }, 11500);

                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Intent intent = new Intent(Home.this, Create.class);
            intent.putExtra("best_match", data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
            intent.putExtra("tuto_mode", false);
            startActivity(intent);
        }
    }

    public void endAnimation() {
        animRunning = false;
        MediaPlayer sound9 = MediaPlayer.create(this, getResources().getIdentifier("sound9", "raw", getPackageName()));
        Toast.makeText(getApplicationContext(), getString(R.string.toast_more_help), Toast.LENGTH_LONG).show();
        sound9.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!animRunning) {
            switch (item.getItemId()) {
                case R.id.menu_config:
                    Intent intent = new Intent(Home.this, Config.class);
                    startActivity(intent);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
