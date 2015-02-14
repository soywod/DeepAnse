package fr.deepanse.soywod.deepanse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.R;
import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 13/02/2015.
 */
public class Home extends Activity implements View.OnClickListener {

    private final static int RESULT_ADD_DEEPANSE_BY_VOICE = 0;
    private final static int RESULT_ADD_DEEPANSE_BY_HAND = 1;

    private final static int RESULT_ADD_GROUP_BY_VOICE = 2;
    private final static int RESULT_ADD_GROUP_BY_HAND = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.buttonAddDeepanseByVoice).setOnClickListener(this);
        findViewById(R.id.buttonAddDeepanseByHand).setOnClickListener(this);
        findViewById(R.id.buttonEditDeepanse).setOnClickListener(this);

        findViewById(R.id.buttonViewByDay).setOnClickListener(this);
        findViewById(R.id.buttonViewByMonth).setOnClickListener(this);
        findViewById(R.id.buttonViewByYear).setOnClickListener(this);

        findViewById(R.id.buttonAddGroupByVoice).setOnClickListener(this);
        findViewById(R.id.buttonAddGroupByHand).setOnClickListener(this);
        findViewById(R.id.buttonEditGroup).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.buttonAddDeepanseByVoice :
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_deepanse));
                startActivityForResult(intent, RESULT_ADD_DEEPANSE_BY_VOICE);
                break;
            case R.id.buttonAddDeepanseByHand :
                intent = new Intent(Home.this, EditDeepAnse.class);
                intent.putExtra("new_deepanse", true);
                startActivityForResult(intent, RESULT_ADD_DEEPANSE_BY_HAND);
                break;
            case R.id.buttonEditDeepanse :
            case R.id.buttonViewByDay :
                intent = new Intent(Home.this, ViewByDay.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                startActivity(intent);
                break;
            case R.id.buttonViewByMonth :
                intent = new Intent(Home.this, ViewByMonth.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                startActivity(intent);
                break;
            case R.id.buttonViewByYear :
                intent = new Intent(Home.this, ViewByYear.class);
                intent.putExtra("main_date", Conversion.dateToString(new GregorianCalendar()));
                startActivity(intent);
                break;
            case R.id.buttonAddGroupByVoice :
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt_add_group));
                startActivityForResult(intent, RESULT_ADD_GROUP_BY_VOICE);
                break;
            case R.id.buttonAddGroupByHand :
                intent = new Intent(Home.this, EditGroup.class);
                intent.putExtra("new_group", true);
                startActivityForResult(intent, RESULT_ADD_GROUP_BY_HAND);
                break;
            case R.id.buttonEditGroup :
                intent = new Intent(Home.this, ViewGroup.class);
                intent.putExtra("new_deepanse", true);
                startActivityForResult(intent, RESULT_ADD_DEEPANSE_BY_HAND);
                break;
        }
    }
}
