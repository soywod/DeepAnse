package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ReportByMonth;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.Report;

/**
 * Created by soywod on 11/02/2015.
 */
public class ViewByMonth extends fr.deepanse.soywod.deepanse.activity.DeepAnse {

    private ArrayList<Report> arrayReport;
    private ReportByMonth adapterReportByMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globalview);

        Intent intent = getIntent();

        mainDate = ((intent != null)?(Conversion.stringToDate(intent.getStringExtra("main_date"))):(new GregorianCalendar()));
        arrayReport = new ArrayList<>();
        adapterReportByMonth = new ReportByMonth(this, arrayReport);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);
        listViewDeepAnse.setAdapter(adapterReportByMonth);
        listViewDeepAnse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mainDate.set(GregorianCalendar.DAY_OF_MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.DAY_OF_MONTH));
                mainDate.set(GregorianCalendar.MONTH, arrayReport.get(position).getDate().get(GregorianCalendar.MONTH));
                mainDate.set(GregorianCalendar.YEAR, arrayReport.get(position).getDate().get(GregorianCalendar.YEAR));

                Intent intent = new Intent(ViewByMonth.this, ViewByDay.class);
                intent.putExtra("main_date", Conversion.dateToString(mainDate));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void forwardMainDate(int count) {

        mainDate.add(GregorianCalendar.MONTH, count);

        ArrayList<Report> tmpArrayReport = deepAnseDb.selectAllByMonth(mainDate);
        double total = 0;

        arrayReport.clear();

        if (tmpArrayReport != null) {
            for (Report item : tmpArrayReport) {
                arrayReport.add(item);
                total += item.getTotal();
            }
        }

        adapterReportByMonth.notifyDataSetChanged();

        refreshMainDate(Conversion.dateToStringMonthYearFr(mainDate));
        refreshTotal(total);
    }
}
