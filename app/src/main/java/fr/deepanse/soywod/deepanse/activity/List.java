package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ExpListView;
import fr.deepanse.soywod.deepanse.adapter.SpinnerDate;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DateFR;

/**
 * Created by soywod on 27/02/2015.
 * Activity that permits user to list expense by day, month or year, extends activity.DeepAnse
 *
 * @author soywod
 */
public class List extends DeepAnse {

    /**
     * int of current state (by year, by month or by day)
     */
    private static int ACTIVE_DISPLAY;

    /**
     * List of group
     */
    private java.util.List<Object[]> groupList;

    /**
     * List of child
     */
    private java.util.List childList;

    /**
     * Collection of group / child
     */
    private Map<Object[], java.util.List<Object[]>> deepAnseCollection;

    /**
     * ExpandableListView of data
     */
    private ExpandableListView expandableListView;

    /**
     * ExpandableListView's adapter
     */
    private ExpListView adapter;

    /**
     * Spinners of month and year
     */
    private Spinner spinnerMonth, spinnerYear;

    /**
     * ArrayList<String> of available months
     */
    private ArrayList<String> arraySpinnerMonth;

    /**
     * ArrayList<String> of available years
     */
    private ArrayList<String> arraySpinnerYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initComponent();
        initData();
    }

    /**
     * Components initializer.
     */
    public void initComponent() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        spinnerYear = (Spinner) findViewById(R.id.spinner_year);

        expandableListView.setDividerHeight(1);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String group = adapter.getChild(groupPosition, childPosition)[0].toString();

                Intent intent = new Intent(List.this, ListDetail.class);
                intent.putExtra("title", R.string.title_detailed_list);
                intent.putExtra("group", group);

                switch (ACTIVE_DISPLAY) {
                    case 0:
                        intent.putExtra("year", Integer.parseInt(((String) adapter.getGroup(groupPosition)[0])));
                        break;

                    case 1:
                        intent.putExtra("year", Integer.parseInt((String) spinnerYear.getSelectedItem()));
                        intent.putExtra("month", DateFR.findDateNumeric(((String) adapter.getGroup(groupPosition)[0]).toLowerCase()));
                        break;

                    case 2:
                        intent.putExtra("year", Integer.parseInt((String) spinnerYear.getSelectedItem()));
                        intent.putExtra("month", DateFR.findDateNumeric(((String) adapter.getGroup(groupPosition)[0]).split(" ")[1]));
                        intent.putExtra("day", Integer.parseInt(((String) adapter.getGroup(groupPosition)[0]).split(" ")[0]));
                        break;
                }

                startActivity(intent);
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

    /**
     * Data initializer.
     */
    public void initData() {
        ACTIVE_DISPLAY = 2;

        groupList = new ArrayList<>();
        childList  = new ArrayList<>();
        deepAnseCollection = new LinkedHashMap<>();
        arraySpinnerMonth = new ArrayList<>();
        arraySpinnerYear = new ArrayList<>();

        adapter = new ExpListView(this, groupDb, groupList, deepAnseCollection);

        expandableListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * Function that refreshes all data
     */
    public void refreshData() {
        initSpinnerYear();
        initSpinnerMonth();

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

    /**
     * Function that init year spinner
     */
    public void initSpinnerYear() {
        arraySpinnerYear.clear();
        ArrayList<String> tmpArrayYear = deepAnseDb.selectAllYearWithOutSum();
        if (tmpArrayYear != null) {
            for (String item : tmpArrayYear)
                arraySpinnerYear.add(item);
            spinnerYear.setAdapter(new SpinnerDate(List.this, arraySpinnerYear));
        }
    }

    /**
     * Function that init month spinner
     */
    public void initSpinnerMonth() {
        arraySpinnerMonth.clear();
        ArrayList<String> tmpArrayMonth = deepAnseDb.selectAllMonth((String) spinnerYear.getSelectedItem());
        if (tmpArrayMonth != null) {
            for (String item : tmpArrayMonth)
                arraySpinnerMonth.add(item);

            spinnerMonth.setAdapter(new SpinnerDate(List.this, arraySpinnerMonth));
        }
    }

    /**
     * Function that collapse all group
     */
    public void collapseAll() {
        for (int i=0 ; i<adapter.getGroupCount() ; i++)
            expandableListView.collapseGroup(i);
    }

    /**
     * Function that create by year collection
     */
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

    /**
     * Function that create by month collection
     */
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

    /**
     * Function that create by day collection
     */
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

    /**
     * Function that load a child in the collection
     */
    private void loadChild(ArrayList<Object[]> array) {
        childList = new ArrayList<>();

        for (Object[] item : array)
            childList.add(new Object[]{groupDb.select((Long) item[1]).getName(), String.valueOf(item[2])});
    }

    /**
     *  Event triggered by clicking on the by year button.
     *
     *  Init all data for a by year showing
     *
     *  @param view     The view concerned
     */
    public void eventListByYear(View view) {
        ACTIVE_DISPLAY = 0;

        findViewById(R.id.spinner_month).setEnabled(false);
        findViewById(R.id.spinner_year).setEnabled(false);

        groupList.clear();
        deepAnseCollection.clear();

        createCollectionForYear();

        adapter.notifyDataSetChanged();

        setTitle(R.string.title_by_year);

        collapseAll();
    }

    /**
     *  Event triggered by clicking on the by month button.
     *
     *  Init all data for a by month showing
     *
     *  @param view     The view concerned
     */
    public void eventListByMonth(View view) {
        ACTIVE_DISPLAY = 1;

        findViewById(R.id.spinner_month).setEnabled(false);
        findViewById(R.id.spinner_year).setEnabled(true);

        groupList.clear();
        deepAnseCollection.clear();

        Object tmpYear = ((Spinner) findViewById(R.id.spinner_year)).getSelectedItem();

        createCollectionForMonth(tmpYear);

        adapter.notifyDataSetChanged();

        setTitle(R.string.title_by_month);

        collapseAll();
    }

    /**
     *  Event triggered by clicking on the by day button.
     *
     *  Init all data for a by day showing
     *
     *  @param view     The view concerned
     */
    public void eventListByDay(View view) {
        ACTIVE_DISPLAY = 2;

        findViewById(R.id.spinner_month).setEnabled(true);
        findViewById(R.id.spinner_year).setEnabled(true);

        groupList.clear();
        deepAnseCollection.clear();

        Object tmpYear = ((Spinner) findViewById(R.id.spinner_year)).getSelectedItem();

        if (tmpYear != null) {
            String tmpMonth = (DateFR.findDateNumeric(((String)((Spinner) findViewById(R.id.spinner_month)).getSelectedItem()).toLowerCase())+1)+"";
            if (tmpMonth.length() == 1) tmpMonth = "0" + tmpMonth;

            createCollectionForDay(tmpYear, tmpMonth);

            adapter.notifyDataSetChanged();

            setTitle(R.string.title_by_day);

            collapseAll();
        }
        else {
            showShortToast(R.string.toast_no_result);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collapse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // If collapse all clicked, collapse all group
            case R.id.menu_collapse:
                collapseAll();
                break;

            // If back arrow clicked, close this activity
            case android.R.id.home:
                finish();
                break;

            // If home clicked, start new Home activity deleting the others and close this one
            case R.id.menu_home :
                Intent intent = new Intent(List.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
