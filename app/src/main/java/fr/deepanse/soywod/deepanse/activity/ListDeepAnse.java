package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ExpandableListDeepAnse;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDeepAnse;
import fr.deepanse.soywod.deepanse.database.DeepAnseSQLiteOpenHelper;
import fr.deepanse.soywod.deepanse.model.*;
import fr.deepanse.soywod.deepanse.model.DeepAnse;

public class ListDeepAnse extends ActionBarActivity {

    private fr.deepanse.soywod.deepanse.database.DeepAnseGroup groupDb;
    private fr.deepanse.soywod.deepanse.database.DeepAnse deepAnseDb;

    private List<Object[]> groupList = new ArrayList<>();
    private List<Object[]> childList  = new ArrayList<>();
    private Map<Object[], List<Object[]>> deepAnseCollection = new LinkedHashMap<>();

    private ExpandableListView expandableListView;
    private ExpandableListDeepAnse adapter;

    private Spinner spinnerMonth, spinnerYear;

    private ArrayList<String> arraySpinnerMonth = new ArrayList<>();
    private ArrayList<String> arraySpinnerYear = new ArrayList<>();

    private static int ACTIVE_DISPLAY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_deepanse);
        setTitle(R.string.view_title_by_year);

        DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper = new DeepAnseSQLiteOpenHelper(this);
        groupDb = new fr.deepanse.soywod.deepanse.database.DeepAnseGroup(deepAnseSQLiteOpenHelper);
        deepAnseDb = new fr.deepanse.soywod.deepanse.database.DeepAnse(deepAnseSQLiteOpenHelper);

        spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);

        openDatabases();

        adapter = new ExpandableListDeepAnse(this, groupDb, groupList, deepAnseCollection);

        expandableListView.setDividerHeight(1);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (ACTIVE_DISPLAY == 2) {
                    GregorianCalendar date = new GregorianCalendar(
                            Integer.parseInt((String) spinnerYear.getSelectedItem()),
                            DateFR.findDateNumeric(((String) adapter.getGroup(groupPosition)[0]).split(" ")[1]),
                            Integer.parseInt(((String) adapter.getGroup(groupPosition)[0]).split(" ")[0]));
                    String group = (String) adapter.getChild(groupPosition, childPosition)[0];


                    Intent intent = new Intent(ListDeepAnse.this, ListDeepAnseDetail.class);
                    intent.putExtra("date", Conversion.dateToString(date));
                    intent.putExtra("group", group);
                    startActivity(intent);
                }

                return false;
            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ACTIVE_DISPLAY == 2)
                    eventListByDay(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                initSpinnerMonth();

                spinnerYear.setSelection(position);

                if (ACTIVE_DISPLAY == 1)
                    eventListByMonth(null);
                else if (ACTIVE_DISPLAY == 2)
                    eventListByDay(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initSpinnerYear();

        switch (ACTIVE_DISPLAY) {
            case 0:
                eventListByYear(null);
                break;
            case 1:
                eventListByMonth(null);
                break;
            case 2:
                eventListByDay(null);
                break;
        }
    }

    public void initSpinnerYear() {
        arraySpinnerYear.clear();
        ArrayList<String> tmpArrayYear = deepAnseDb.selectAllYearWithOutSum();
        for (String item : tmpArrayYear)
            arraySpinnerYear.add(item);
        spinnerYear.setAdapter(new SpinnerDeepAnse(ListDeepAnse.this, arraySpinnerYear));
    }

    public void initSpinnerMonth() {
        arraySpinnerMonth.clear();
        ArrayList<String> tmpArrayMonth = deepAnseDb.selectAllMonth((String) spinnerYear.getSelectedItem());
        for (String item : tmpArrayMonth)
            arraySpinnerMonth.add(item);

        spinnerMonth.setAdapter(new SpinnerDeepAnse(ListDeepAnse.this, arraySpinnerMonth));
    }

    public void collapseAll() {
        for (int i=0 ; i<adapter.getGroupCount() ; i++)
            expandableListView.collapseGroup(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDatabases();
    }

    public void openDatabases() {
        try
        {
            groupDb.open();
            deepAnseDb.open();
        }catch(SQLException e){e.printStackTrace();}

        if (groupDb.select(1) == null) groupDb.insert(new DeepAnseGroup(1, "default", Color.parseColor("#CCCCCC")));
        //groupDb.insert(new DeepAnseGroup(2, "chaise", Color.RED));
        //deepAnseDb.insert(new DeepAnse(0, 6, new GregorianCalendar(2016, 5, 20), groupDb.select(1), "comment5", false));
    }

    public void closeDatabases() {
        groupDb.close();
        deepAnseDb.close();
    }

    private void createCollectionForYear() {
        ArrayList<Object[]> tmpArrayYear = deepAnseDb.selectAllYear();
        if (tmpArrayYear != null) {
            for (Object[] item : tmpArrayYear)
                groupList.add(item);

            for (Object[] item : groupList) {
                ArrayList<Object[]> tmpArray = deepAnseDb.selectYearSumByGroup(item[0]);
                loadChild(tmpArray);
                deepAnseCollection.put(item, childList);
            }
        }
    }

    private void createCollectionForMonth(Object year) {
        ArrayList<Object[]> tmpArrayMonth = deepAnseDb.selectAllMonthByYear(year);
        if (tmpArrayMonth != null) {
            for (Object[] item : tmpArrayMonth) {
                item[0] = Conversion.firstCharToUpperCase(DateFR.findDateLitteral(Integer.parseInt((String) item[0]) - 1));
                groupList.add(item);
            }

            for (Object[] item : groupList) {
                String tmpMonth = (DateFR.findDateNumeric(((String) item[0]).toLowerCase()) + 1) + "";
                if (tmpMonth.length() == 1) tmpMonth = "0" + tmpMonth;
                ArrayList<Object[]> tmpArray = deepAnseDb.selectMonthSumByGroup(year, tmpMonth);
                loadChild(tmpArray);
                deepAnseCollection.put(item, childList);
            }
        }
    }

    private void createCollectionForDay(Object year, Object month) {
        ArrayList<Object[]> tmpArrayDay = deepAnseDb.selectAllDayByYearMonth(year, month);
        if (tmpArrayDay != null) {
            for (Object[] item : tmpArrayDay) {
                item[0] = Integer.parseInt((String) item[0]) + " " + DateFR.findDateLitteral(Integer.parseInt((String) month) - 1);
                groupList.add(item);
            }

            for (Object[] item : groupList) {
                String tmpDay = ((String) item[0]).split(" ")[0];
                if (tmpDay.length() == 1) tmpDay = "0" + tmpDay;
                ArrayList<Object[]> tmpArray = deepAnseDb.selectDaySumByGroup(year, month, tmpDay);
                loadChild(tmpArray);
                deepAnseCollection.put(item, childList);
            }
        }
    }

    private void loadChild(ArrayList<Object[]> array) {
        childList = new ArrayList<>();

        for (Object[] item : array)
            childList.add(new Object[]{groupDb.select((Long) item[1]).getName(), String.valueOf(item[2])});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_deepanse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_year:
                eventListByYear(null);
                break;

            case R.id.menu_month:
                eventListByMonth(null);
                break;

            case R.id.menu_day:
                eventListByDay(null);
                break;

            case R.id.menu_collapse:
                collapseAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void eventListByYear(View v) {
        ACTIVE_DISPLAY = 0;

        findViewById(R.id.spinner_month).setEnabled(false);
        findViewById(R.id.spinner_year).setEnabled(false);

        groupList.clear();
        deepAnseCollection.clear();

        adapter.setChildSelectable(false);

        createCollectionForYear();

        adapter.notifyDataSetChanged();

        setTitle(R.string.view_title_by_year);



        collapseAll();
    }

    public void eventListByMonth(View v) {
        ACTIVE_DISPLAY = 1;

        findViewById(R.id.spinner_month).setEnabled(false);
        findViewById(R.id.spinner_year).setEnabled(true);

        groupList.clear();
        deepAnseCollection.clear();

        Object tmpYear = ((Spinner) findViewById(R.id.spinner_year)).getSelectedItem();

        adapter.setChildSelectable(false);

        createCollectionForMonth(tmpYear);

        adapter.notifyDataSetChanged();

        setTitle(R.string.view_title_by_month);

        collapseAll();
    }

    public void eventListByDay(View v) {
        ACTIVE_DISPLAY = 2;

        findViewById(R.id.spinner_month).setEnabled(true);
        findViewById(R.id.spinner_year).setEnabled(true);

        groupList.clear();
        deepAnseCollection.clear();

        Object tmpYear = ((Spinner) findViewById(R.id.spinner_year)).getSelectedItem();

        String tmpMonth = (DateFR.findDateNumeric(((String)((Spinner) findViewById(R.id.spinner_month)).getSelectedItem()).toLowerCase())+1)+"";
        if (tmpMonth.length() == 1) tmpMonth = "0" + tmpMonth;

        adapter.setChildSelectable(true);

        createCollectionForDay(tmpYear, tmpMonth);

        adapter.notifyDataSetChanged();

        setTitle(R.string.view_title_by_day);

        collapseAll();
    }
}
