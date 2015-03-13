package fr.deepanse.soywod.deepanse.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;

/**
 * Created by soywod on 23/02/2015.
 * Abstract activity extended by almost all DeepAnse activities. Give database access and common functions
 *
 * @author soywod
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
        groupDb.close();
        deepAnseDb.close();
        super.onDestroy();
    }

    /**
     *  Database initializer.
     *
     *  @param context      The main context
     */
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

        if (groupDb.select(1) == null) groupDb.insert(new DeepAnseGroup(1, "divers", getResources().getColor(R.color.ColorBlue)));
    }

    /**
     *  ActionBar initializer.
     */
    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu));
    }

    protected void setActionBarIcon(int drawableRes) {
        actionBar.setIcon(getResources().getDrawable(drawableRes));
    }

    protected void setActionBarTitle(int stringRes) {
        actionBar.setTitle(getResources().getString(stringRes));
    }

    /**
     *  Event triggered by clicking on the cancel button.
     *
     *  Close the current activity.
     *
     *  @param view     The view concerned
     */
    public void eventCancel(View view) {
        finish();
    }

    /**
     *  Function that transforms a GregorianCalendar in String (French)
     *
     *  @param date     The GregorianCalendar date
     *  @return         The date transformed in String
     */
    protected String dateToStringFrExplicit(GregorianCalendar date) {

        GregorianCalendar today = new GregorianCalendar();

        int today_year = today.get(GregorianCalendar.YEAR);
        int today_month = today.get(GregorianCalendar.MONTH);
        int today_day = today.get(GregorianCalendar.DAY_OF_MONTH);

        int date_year = date.get(GregorianCalendar.YEAR);
        int date_month = date.get(GregorianCalendar.MONTH);
        int date_day = date.get(GregorianCalendar.DAY_OF_MONTH);

        if (date_year == today_year && date_month == today_month && date_day == today_day)
            return getString(R.string.today);

        if (date_year == today_year && date_month == today_month && date_day == (today_day+1))
            return getString(R.string.tomorrow);

        if (date_year == today_year && date_month == today_month && date_day == (today_day-1))
            return getString(R.string.yesterday);

        return Conversion.dateToStringDayMonthYearFr(date);
    }

    /**
     *  Function that shows a short Toast.
     *
     *  @param stringRes     The string resource the Toast has to show.
     */
    public void showShortToast(int stringRes) {
        Toast.makeText(getApplicationContext(), getString(stringRes), Toast.LENGTH_SHORT).show();
    }
}
