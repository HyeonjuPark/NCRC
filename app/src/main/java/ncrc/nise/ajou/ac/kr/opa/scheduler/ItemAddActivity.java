package ncrc.nise.ajou.ac.kr.opa.scheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        itemAddView = (ListView) findViewById(R.id.itemAddView);

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
