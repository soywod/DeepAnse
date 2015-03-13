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
 * Activity that permits user to change application parameters, extends activity.DeepAnse
 *
 * @author soywod
 */
public class Config extends DeepAnse {

    /**
     *  EditText that contains the default email used to send CSV exports
     */
    private EditText editDefaultMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        initComponent();
        initData();
    }

    /**
     *  Components initializer
     */
    public void initComponent() {
        editDefaultMail = (EditText) findViewById(R.id.edit_mail);
    }

    /**
     *  Data initializer
     */
    public void initData() {
        editDefaultMail.setText(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("defaultMail", ""));
    }

    /**
     *  Event triggered by clicking on the save button.
     *  Put the EditText text in local preferences and show short Toast to inform user.
     *
     *  @param view     The view concerned
     */
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

            // If back arrow clicked, close this activity
            case android.R.id.home:
                finish();
                break;

            // If home clicked, start new Home activity deleting the others and close this one
            case R.id.menu_home :
                Intent intent = new Intent(Config.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

