package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 18/02/2015.
 */
public class Home extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setTitle(getString(R.string.title_home));

        findViewById(R.id.button_add_deepanse_by_voice).setOnClickListener(this);
        findViewById(R.id.button_add_deepanse_by_hand).setOnClickListener(this);
        findViewById(R.id.button_list_deepanse).setOnClickListener(this);
        findViewById(R.id.button_find_deepanse).setOnClickListener(this);

        findViewById(R.id.button_add_group).setOnClickListener(this);
        findViewById(R.id.button_list_group).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button_add_deepanse_by_voice :
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_deepanse));
                startActivityForResult(intent, 0);
                break;

            case R.id.button_add_deepanse_by_hand :
                intent = new Intent(Home.this, EditDeepAnse.class);
                intent.putExtra("new_deepanse", true);
                startActivity(intent);
                break;

            case R.id.button_list_deepanse :
                intent = new Intent(Home.this, ListDeepAnse.class);
                startActivity(intent);
                break;

            case R.id.button_find_deepanse :
                intent = new Intent(Home.this, FindDeepAnse.class);
                startActivity(intent);
                break;

            case R.id.button_add_group :
                intent = new Intent(Home.this, EditDeepAnseGroup.class);
                intent.putExtra("new_group", true);
                startActivity(intent);
                break;

            case R.id.button_list_group :
                intent = new Intent(Home.this, ListDeepAnseGroup.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Intent intent = new Intent(Home.this, CreateDeepAnse.class);
            intent.putExtra("best_match", data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
            startActivity(intent);
        }
    }
}
