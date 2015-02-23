package fr.deepanse.soywod.deepanse.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import java.sql.SQLException;

import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 23/02/2015.
 */
abstract public class DeepAnse extends ActionBarActivity {

    protected fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    protected fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatabase(this);
        initActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupDb.close();
        deepAnseDb.close();
    }

    private void initDatabase(Context context) {
        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(context);

        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        try {
            groupDb.open();
            deepAnseDb.open();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        if (groupDb.select(1) == null) groupDb.insert(new DeepAnseGroup(1, "default", Color.DKGRAY));
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    }

    protected void setActionBarIcon(int drawableRes) {
        actionBar.setIcon(getResources().getDrawable(drawableRes));
    }

    protected void setActionBarTitle(int stringRes) {
        actionBar.setTitle(getResources().getString(stringRes));
    }

    public void eventCancel(View view) {
        finish();
    }
}
