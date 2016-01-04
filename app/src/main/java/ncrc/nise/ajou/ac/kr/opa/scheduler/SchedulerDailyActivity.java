package ncrc.nise.ajou.ac.kr.opa.scheduler;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;


public class SchedulerDailyActivity extends ActionBarActivity {
    private Button buttonBreakfast;
    private Button buttonLunch;
    private Button buttonDinner;
    private Button buttonExercise;

    private TextView textViewDate;
    private TextView textViewManbo;
    private DBAdapter dbAdapter;

    String currDate;
    private TextView textViewBreakfast;
    private TextView textViewLunch;
    private TextView textViewDinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_daily);

        // dbadapter
        dbAdapter = new DBAdapter(getApplicationContext());

        // 인텐트로 날짜 string 받기 ex) 2015-12-30
        Intent intent = getIntent();
        currDate = intent.getExtras().getString("currDate");

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(currDate);

        textViewManbo = (TextView) findViewById(R.id.textViewManbo);
        Cursor c = dbAdapter.readManbo(currDate);
        if(c.getCount() > 0) {
            c.moveToFirst();
            textViewManbo.setText("만보 칼로리 : " + c.getInt(2) + " kcal");
        }

        textViewBreakfast = (TextView)findViewById(R.id.textViewBreakfast);
        c = dbAdapter.sumDailyFoodKcal(currDate, "breakfast");
        Log.i("asdf", Double.toString(c.getCount()));
        if(c.getCount() > 0) {
            c.moveToFirst();
            textViewBreakfast.setText("아침 식사 : " + c.getDouble(0) + " kcal");
        }

        textViewLunch = (TextView)findViewById(R.id.textViewLunch);
        c = dbAdapter.sumDailyFoodKcal(currDate, "lunch");
        if(c.getCount() > 0) {
            c.moveToFirst();
            textViewLunch.setText("점심 식사 : " + c.getDouble(0) + " kcal");
        }

        textViewDinner = (TextView)findViewById(R.id.textViewDinner);
        c = dbAdapter.sumDailyFoodKcal(currDate, "dinner");
        if(c.getCount() > 0) {
            c.moveToFirst();
            textViewDinner.setText("저녁 식사 : " + c.getDouble(0) + " kcal");
        }

        buttonBreakfast = (Button)findViewById(R.id.buttonBreakfast);
        buttonLunch = (Button)findViewById(R.id.buttonLunch);
        buttonDinner = (Button)findViewById(R.id.buttonDinner);

        buttonExercise = (Button)findViewById(R.id.buttonExercise);


        buttonBreakfast.setOnClickListener(mClickListener);
        buttonLunch.setOnClickListener(mClickListener);
        buttonDinner.setOnClickListener(mClickListener);
        buttonExercise.setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            String type = "";
            switch (v.getId()) {
                case R.id.buttonBreakfast:
                    type = "breakfast";
                    break;
                case R.id.buttonLunch:
                    type = "lunch";
                    break;
                case R.id.buttonDinner:
                    type = "dinner";
                    break;
                case R.id.buttonExercise:
                    type = "exercise";
                    break;
            }

            Intent intent = new Intent(SchedulerDailyActivity.this, ItemlistDailyActivity.class);
            intent.putExtra("currDate", currDate);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = dbAdapter.sumDailyFoodKcal(currDate, "breakfast");
        if(c.getCount() > 0) {
            c.moveToFirst();
            textViewBreakfast.setText("아침 식사 : " + c.getInt(0) + " kcal");
        }
    }
}
