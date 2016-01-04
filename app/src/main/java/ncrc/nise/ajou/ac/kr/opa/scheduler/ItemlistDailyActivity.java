package ncrc.nise.ajou.ac.kr.opa.scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;


public class ItemlistDailyActivity extends ActionBarActivity {


    private Button buttonAddFood;
    private ListView listView;
    private DBAdapter dbAdapter;

    ArrayList<String> datas = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private TextView textViewItemlistTitle;
    private int selectedFoodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist_daily);

        listView = (ListView) findViewById(R.id.listView);

        // 인텐트로 날짜 string 받기 ex) 2015-12-30
        Intent intent = getIntent();
        final String currDate = intent.getExtras().getString("currDate");
        final String type = intent.getExtras().getString("type");

        Log.i("type", type);

        // DB 어댑터 생성
        dbAdapter = new DBAdapter(getApplicationContext());

        textViewItemlistTitle = (TextView)findViewById(R.id.textViewItemlistTitle);
        if(type.equals("breakfast")) {
            textViewItemlistTitle.setText("아침 식사");
        } else if(type.equals("lunch")) {
            textViewItemlistTitle.setText("점심 식사");
        } else if(type.equals("dinner")) {
            textViewItemlistTitle.setText("저녁 식사");
        } else if(type.equals("exercise")) {
            textViewItemlistTitle.setText("운동 정보");
        }

        // type 확인
        if(type.equals("breakfast") || type.equals("lunch") || type.equals("dinner")) {
            Cursor c = dbAdapter.readDailyFood(currDate, type);
            try {
                if(c.getCount() > 0) {
                    while(c.moveToNext()) {
                        datas.add(c.getString(1) + " (" + c.getString(2) + ")");
                    }
                } else {

                }
            } finally {
                c.close();
            }
        } else if(type == "exercise") {

        } else {

        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemlistDailyActivity.this);
                alert.setTitle("삭제");
                alert.setMessage(datas.get(position) +" 을 삭제하시겠습니까?");
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 삭제 선택 시 해당 정보를 제거한다.
                        datas.remove(position);
                        //db 삭제

                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

        buttonAddFood = (Button)findViewById(R.id.buttonAddFood);
        buttonAddFood.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = { "한식", "중식", "일식", "양식", "직접 입력" };

                AlertDialog.Builder ab = new AlertDialog.Builder(ItemlistDailyActivity.this);
                ab.setTitle("음식 카테고리");
                ab.setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을 때
                                switch (whichButton) {
                                    case 0:
                                        //type
                                        selectedFoodType=0;
                                        break;
                                    case 1:
                                        selectedFoodType=1;
                                        break;
                                    case 2:
                                        selectedFoodType=2;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });

                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),items[which]+"를 선택했습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ItemlistDailyActivity.this, ItemAddActivity.class);
                        intent.putExtra("foodType", selectedFoodType);
                        startActivity(intent);
                        Log.i("dialog", which + "");
                    }
                });
                ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                ab.show();




//                AlertDialog.Builder alert = new AlertDialog.Builder(ItemlistDailyActivity.this);
//                alert.setTitle("음식 정보 추가");
//                // 전체 레이아웃
//                LinearLayout layout = new LinearLayout(ItemlistDailyActivity.this);
//                layout.setOrientation(LinearLayout.VERTICAL);
//                layout.setGravity(Gravity.CENTER);
//
//                // 그 안의 레이아웃 1
//                LinearLayout foodNameLayout = new LinearLayout(ItemlistDailyActivity.this);
//                foodNameLayout.setOrientation(LinearLayout.HORIZONTAL);
//                TextView textViewFoodname = new TextView(ItemlistDailyActivity.this);
//                textViewFoodname.setText("음식 이름 : ");
//                final EditText editTextFoodname = new EditText(ItemlistDailyActivity.this);
//                editTextFoodname.setEms(5);
//                foodNameLayout.addView(textViewFoodname);
//                foodNameLayout.addView(editTextFoodname);
//
//                // 그 안의 레이아웃 2
//                LinearLayout kcalLayout = new LinearLayout(ItemlistDailyActivity.this);
//                kcalLayout.setOrientation(LinearLayout.HORIZONTAL);
//                TextView textViewKcal = new TextView(ItemlistDailyActivity.this);
//                textViewKcal.setText("칼로리(kcal) : ");
//                final EditText editTextKcal = new EditText(ItemlistDailyActivity.this);
//                editTextKcal.setEms(5);
//                editTextKcal.setInputType(InputType.TYPE_CLASS_NUMBER);
//                kcalLayout.addView(textViewKcal);
//                kcalLayout.addView(editTextKcal);
//
//                layout.addView(foodNameLayout);
//                layout.addView(kcalLayout);
//                alert.setView(layout);
//
//                alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        String foodname = editTextFoodname.getText().toString();
//                        String kcal = editTextKcal.getText().toString();
//
//                        // 리스트 추가하기
//                        datas.add(foodname + " (" + kcal + ")");
//
//                        // db 저장
//                        dbAdapter.insertDailyFood(currDate, type, foodname, Double.parseDouble(kcal));
//                        Log.i("dailyfood", currDate + "," + type + "," + foodname + "," + Double.parseDouble(kcal));
//                        arrayAdapter.notifyDataSetChanged();
//                    }
//                });
//
//                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//                });
//
//                alert.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.notifyDataSetChanged();
    }
}
