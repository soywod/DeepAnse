package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import fr.deepanse.soywod.deepanse.R;

/**
 * Created by soywod on 11/02/2015.
 */
public class Config extends DeepAnse {

    private EditText editDefaultMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        initComponent();
        initData();
    }

    public void initComponent() {
        editDefaultMail = (EditText) findViewById(R.id.edit_mail);
    }

    public void initData() {
        editDefaultMail.setText(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("defaultMail", ""));
    }

    public void eventSave(View view) {
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("defaultMail", editDefaultMail.getText().toString()).apply();
        showShortToast(R.string.toast_config_updated);
        finish();
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

            case R.id.menu_home :
                Intent intent = new Intent(Config.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

