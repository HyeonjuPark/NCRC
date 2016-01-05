package ncrc.nise.ajou.ac.kr.opa.scheduler;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;
import ncrc.nise.ajou.ac.kr.opa.data.OpaDataDBAdapter;


public class ItemAddActivity extends ActionBarActivity {

    private ListView itemAddView;
    private DBAdapter dbAdapter;

    ArrayList<FoodData> foods = new ArrayList<FoodData>();
    ArrayList<String> foodlist = new ArrayList<String>();

    private ArrayAdapter<String> arrayAdapter;
    private TextView textViewItemAddTitle;
    private OpaDataDBAdapter opaDataDbAdapter;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add_daily);

        // 인텐트로 날짜 string, food type string 받기 ex) 2015-12-30, chinese
        Intent intent = getIntent();
        final String type = intent.getExtras().getString("type");
        String foodType = intent.getExtras().getString("foodType");
        final String currDate = intent.getExtras().getString("currDate");

        Log.i("ItemAddActivity", foodType);
        opaDataDbAdapter = new OpaDataDBAdapter(getApplicationContext());
        c = opaDataDbAdapter.readFood(foodType);

        try {
            if(c.getCount() > 0) {
                while(c.moveToNext()) {
                    FoodData foodData = new FoodData(c.getString(1), foodType, c.getDouble(3));
                    foods.add(foodData);
                    foodlist.add(c.getString(1) + " (" + c.getDouble(3) + " )");
                    Log.i("ItemAddActivity", c.getString(1) + " (" + c.getDouble(3) + " )");
                }
            } else {

            }
        } finally {
            c.close();
        }

        itemAddView = (ListView) findViewById(R.id.itemAddView);

        dbAdapter = new DBAdapter(getApplicationContext());

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodlist);
        itemAddView.setAdapter(arrayAdapter);

        itemAddView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                FoodData tempData = foods.get(position);
                // db 저장
                dbAdapter.insertDailyFood(currDate, type, tempData.getFoodName(), tempData.getKcal());
                Log.i("dailyfood", currDate + "," + type + "," + tempData.getFoodName() + "," + tempData.getKcal());
                finish();
            }
        });


    }
}
