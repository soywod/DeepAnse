package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 13/02/2015.
 */
public class Home extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.buttonViewByDay).setOnClickListener(this);
        findViewById(R.id.buttonViewByMonth).setOnClickListener(this);
        findViewById(R.id.buttonViewByYear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.buttonViewByDay :
                intent = new Intent(Home.this, ViewByDay.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                break;
            case R.id.buttonViewByMonth :
                intent = new Intent(Home.this, ViewByMonth.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                break;
            case R.id.buttonViewByYear :
                intent = new Intent(Home.this, ViewByYear.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                break;
        }

        if (intent != null) startActivity(intent);
    }
}
