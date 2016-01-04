package ncrc.nise.ajou.ac.kr.opa.scheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;


public class ItemAddActivity extends ActionBarActivity {

    private ListView itemAddView;
    private DBAdapter dbAdapter;

    ArrayList<String> datas = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private TextView textViewItemAddTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add_daily);

        Intent intent = getIntent();
        final String foodType = intent.getExtras().getString("foodType");

        final ArrayList<String> foods = new ArrayList<String>();
        foods.add("김밥");
        foods.add("떡만두국");
        foods.add("순두부찌개");
        foods.add("쫄면");
        foods.add("비빔밥");

        itemAddView = (ListView) findViewById(R.id.itemAddView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        itemAddView.setAdapter(arrayAdapter);

        itemAddView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",foods.get(position));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        // DB 어댑터 생성
        dbAdapter = new DBAdapter(getApplicationContext());

//        Cursor c = dbAdapter.readFoodList(foodType);
//        try {
//            if(c.getCount() > 0) {
//                while(c.moveToNext()) {
//                    datas.add(c.getString(1) + " (" + c.getString(2) + ")");
//                }
//            } else {
//
//            }
//        } finally {
//            c.close();
//        }
//        itemAddView.add
    }
}
