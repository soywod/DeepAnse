package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnseGroup;
import fr.deepanse.soywod.deepanse.model.Report;

/**
 * Created by soywod on 11/02/2015.
 */
public class ViewByYear extends fr.deepanse.soywod.deepanse.activity.DeepAnse {

    private ArrayList<Report> arrayReport;
    private fr.deepanse.soywod.deepanse.adapter.ViewByYear adapterViewByYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_year);

        if (groupDb.select(1) == null) groupDb.insert(new DeepAnseGroup(1, "default", Color.parseColor("#CCCCCC")));

        mainDate = new GregorianCalendar();
        arrayReport = new ArrayList<>();
        adapterViewByYear = new fr.deepanse.soywod.deepanse.adapter.ViewByYear(this, arrayReport);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.listview);
        listViewDeepAnse.setAdapter(adapterViewByYear);

        listViewDeepAnse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (adapterViewByYear.isViewSelected()) {
                        adapterViewByYear.setColorSelectedView(getResources().getColor(R.color.ColorBlue));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!adapterViewByYear.isViewSelected()) {
                        adapterViewByYear.removeSelectedView();
                    }
                } else {
                    adapterViewByYear.removeSelectedView();
                }

                return false;
            }
        });

        listViewDeepAnse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainDate.set(GregorianCalendar.MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.MONTH));
                mainDate.set(GregorianCalendar.YEAR, arrayReport.get(position).getDate().get(GregorianCalendar.YEAR));

                Intent intent = new Intent(ViewByYear.this, ViewByMonth.class);
                intent.putExtra("main_date", Conversion.dateToString(mainDate));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void mainRefresh(int count) {

        mainDate.add(GregorianCalendar.YEAR, count);

        ArrayList<Report> tmpArrayReport = deepAnseDb.selectAllByYear(mainDate);
        double total = 0;

        arrayReport.clear();

        if (tmpArrayReport != null) {
            for(Report item : tmpArrayReport) {
                arrayReport.add(item);
                total += item.getTotal();
            }
        }

        adapterViewByYear.notifyDataSetChanged();

        refreshMainDate(mainDate.get(GregorianCalendar.YEAR)+"");
        refreshTotal(total);
    }

    public void eventBack(View v) {
        finish();
    }
}
