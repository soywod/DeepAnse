package fr.deepanse.soywod.deepanse.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.adapter.ReportByDay;
import fr.deepanse.soywod.deepanse.model.AlertBox;
import fr.deepanse.soywod.deepanse.model.Conversion;
import fr.deepanse.soywod.deepanse.model.DeepAnse;

/**
 * Created by soywod on 11/02/2015.
 */
public class ViewByDay extends fr.deepanse.soywod.deepanse.activity.DeepAnse {

    private ArrayList<DeepAnse> arrayDeepAnse;
    private ReportByDay adapterReportByDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);

        Intent intent = getIntent();

        mainDate = ((intent != null)?(Conversion.stringToDate(intent.getStringExtra("main_date"))):(new GregorianCalendar()));
        arrayDeepAnse = new ArrayList<>();
        adapterReportByDay = new ReportByDay(this, arrayDeepAnse);

        ListView listViewDeepAnse = (ListView) findViewById(R.id.listview);
        listViewDeepAnse.setAdapter(adapterReportByDay);
        listViewDeepAnse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewByDay.this, 2);
                builder.setMessage(getString(R.string.prompt_del_deepanse));
                builder.setPositiveButton("Oui", new AlertBox() {
                    @Override
                    public void execute() {
                        deepAnseDb.delete(arrayDeepAnse.get(position).getId());
                        arrayDeepAnse.remove(position);
                        forwardMainDate(0);
                    }
                });
                builder.setNegativeButton("Non", null);
                builder.show();

                return false;
            }
        });

        listViewDeepAnse.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                startActivity(new Intent(ViewByDay.this, EditDeepAnse.class));
            }
        });
    }

    @Override
    protected void forwardMainDate(int count) {

        mainDate.add(GregorianCalendar.DAY_OF_MONTH, count);

        ArrayList<DeepAnse> tmpArrayDeepAnse = deepAnseDb.selectAllByDay(mainDate);
        double total = 0;

        arrayDeepAnse.clear();

        if (tmpArrayDeepAnse != null) {
            for (DeepAnse item : tmpArrayDeepAnse) {
                arrayDeepAnse.add(item);
                total += item.getAmount();
            }
        }

        Collections.sort(arrayDeepAnse);

        adapterReportByDay.notifyDataSetChanged();

        refreshMainDate(Conversion.dateToStringDayMonthYearFr(mainDate));
        refreshTotal(total);
        refreshRegexGroup();
    }

    public void eventBack(View v) {
        Intent intent = new Intent(ViewByDay.this, ViewByMonth.class);
        intent.putExtra("main_date", Conversion.dateToString(mainDate));
        startActivity(intent);
        finish();
    }
}
