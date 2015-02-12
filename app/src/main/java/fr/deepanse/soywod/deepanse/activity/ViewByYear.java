package fr.deepanse.soywod.deepanse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ReportByYear;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.Report;

/**
 * Created by soywod on 11/02/2015.
 */
public class ViewByYear extends fr.deepanse.soywod.deepanse.activity.DeepAnse {

    private ArrayList<Report> arrayReport;
    private ReportByYear adapterReportByYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globalview);

        mainDate = new GregorianCalendar();
        arrayReport = new ArrayList<>();
        adapterReportByYear = new ReportByYear(this, arrayReport);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.list_view);
        listViewDeepAnse.setAdapter(adapterReportByYear);
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
    protected void forwardMainDate(int count) {

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

        adapterReportByYear.notifyDataSetChanged();

        refreshMainDate(mainDate.get(GregorianCalendar.YEAR)+"");
        refreshTotal(total);
    }
}
