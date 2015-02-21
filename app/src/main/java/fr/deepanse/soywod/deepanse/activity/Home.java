package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
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
        actionBar.setTitle(getString(R.string.home));
        actionBar.setIcon(R.drawable.home);

        findViewById(R.id.buttonListDeepanse).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonListDeepanse :
                intent = new Intent(Home.this, ListDeepAnse.class);
                startActivity(intent);
                break;
        }
    }
}
