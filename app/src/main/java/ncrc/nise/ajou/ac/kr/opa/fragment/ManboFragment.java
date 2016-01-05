package ncrc.nise.ajou.ac.kr.opa.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.activity.MainActivity;
import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;
import ncrc.nise.ajou.ac.kr.opa.data.ManboValues;
import ncrc.nise.ajou.ac.kr.opa.myavatar.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManboFragment extends Fragment {

    private String TAG = "ManboFragment";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // service data
    String serviceDataWalk;
    String serviceDataRun;
    //String serviceDataStairs;

    // 리시버
    BroadcastReceiver receiverWalk;
    BroadcastReceiver receiverRun;
    BroadcastReceiver receiverStairs;

    // UI views
    private TextView walkCount;
    private TextView runCount;
    private TextView stairsCount;

    private Button buttonRun;
    //private Button buttonStairs;
    private Button buttonWalk;
    private Button buttonReset;
    private User user;

    // db
    private DBAdapter dbAdapter;
    private Cursor c;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ManboFragment newInstance(int sectionNumber) {
        ManboFragment fragment = new ManboFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ManboFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manbo, container, false);

        //DB Adapter
        dbAdapter = new DBAdapter(getActivity().getApplicationContext());
        c = dbAdapter.read_user_info();

        // Textview 설정
        walkCount = (TextView) rootView.findViewById(R.id.stepCount);
        runCount = (TextView) rootView.findViewById(R.id.runCount);
        //stairsCount = (TextView)rootView.findViewById(R.id.stairsCount);

        if(c != null) {
            if(c.getCount() != 0) {
                c.moveToFirst();
                Log.i("USER", c.getString(0));

                // set user
                user = new User(c.getDouble(1), c.getDouble(2), c.getInt(3), c.getInt(4));

            }
        }

        walkCount.setText("0 걸음, 0 kcal.");
        runCount.setText(" 0 뜀, 0 kcal.");
        //stairsCount.setText("0 오름, 0 kcal.");


        // Button 설정
        buttonRun = (Button)rootView.findViewById(R.id.buttonRun);
        //buttonStairs = (Button)rootView.findViewById(R.id.buttonStairs);
        buttonWalk = (Button)rootView.findViewById(R.id.buttonWalk);
        buttonReset = (Button)rootView.findViewById(R.id.buttonReset);

        buttonRun.setOnClickListener(mClickListener);
        //buttonStairs.setOnClickListener(mClickListener);
        buttonWalk.setOnClickListener(mClickListener);
        buttonReset.setOnClickListener(mClickListener);

        // 리시버 생성
        receiverWalk = new MyMainLocalReceiver("serviceDataWalk");
        receiverRun = new MyMainLocalReceiver("serviceDataRun");
        //receiverStairs = new MyMainLocalReceiver("serviceDataStairs");

        try {
            IntentFilter mainFilterWalk = new IntentFilter("ncrc.nise.ajou.ac.kr.opa.step");
            getActivity().registerReceiver(receiverWalk, mainFilterWalk);

            IntentFilter mainFilterRun = new IntentFilter("ncrc.nise.ajou.ac.kr.opa.run");
            getActivity().registerReceiver(receiverRun, mainFilterRun);

            /*
            IntentFilter mainFilterStairs = new IntentFilter("ncrc.nise.ajou.ac.kr.opa.stairs");
            getActivity().registerReceiver(receiverStairs, mainFilterStairs);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    // Button 클릭시 이벤트
    Button.OnClickListener mClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            int TIME = 100;
            float max = 0;

            float activityArray[] = new float[TIME];
            long lastTime = System.currentTimeMillis();
            long currentTime = System.currentTimeMillis();

            switch (v.getId()) {
                case R.id.buttonRun:
                    for (int i = 0; i < TIME; i++) {
                        while((currentTime - lastTime) < 100) { // 0.1초마다
                            currentTime = System.currentTimeMillis();
                        }
                        // Run 값을 넣는다.
                        activityArray[i] = ManboValues.Speed;

                        // Run의 max 갱신
                        if(activityArray[i] > max) {
                            max = activityArray[i];
                        }
                    }
                    //ManboValues.RUN_THRESHOLD = (int)max;
                    ManboValues.RUN_THRESHOLD = (int)((ManboValues.RUN_THRESHOLD * 0.9) + (max * 0.1));
                    Toast.makeText(getActivity(), "뛰기 조정 : " + ManboValues.RUN_THRESHOLD, Toast.LENGTH_LONG).show();

                    break;
                /*
                case R.id.buttonStairs:
                    for (int i = 0; i < TIME; i++) {
                        while((currentTime - lastTime) < 100) { // 0.1초마다
                            currentTime = System.currentTimeMillis();
                        }
                        // Run 값을 넣는다.
                        activityArray[i] = ManboValues.StairsGap;

                        // Run의 max 갱신
                        if(activityArray[i] > max) {
                            max = activityArray[i];
                        }
                    }
                    //ManboValues.STAIRS_THRESHOLD = (int)max;
                    ManboValues.STAIRS_THRESHOLD = (int)((ManboValues.STAIRS_THRESHOLD * 0.9) + (max * 0.1));
                    Toast.makeText(getActivity(), "계단오르기 조정 : " + ManboValues.RUN_THRESHOLD, Toast.LENGTH_LONG).show();
                    break;
                    */
                case R.id.buttonWalk:
                    for (int i = 0; i < TIME; i++) {
                        while((currentTime - lastTime) < 100) { // 0.1초마다
                            currentTime = System.currentTimeMillis();
                        }
                        // Run 값을 넣는다.
                        activityArray[i] = ManboValues.Speed;

                        // Run의 max 갱신
                        if(activityArray[i] > max) {
                            max = activityArray[i];
                        }
                    }
                    //ManboValues.WALK_THRESHOLD = (int)max;
                    ManboValues.WALK_THRESHOLD = (int)((ManboValues.WALK_THRESHOLD * 0.9) + (max * 0.1));
                    Toast.makeText(getActivity(), "걷기 조정 : " + ManboValues.WALK_THRESHOLD, Toast.LENGTH_LONG).show();
                    break;
                case R.id.buttonReset:
                    ManboValues.WALK_THRESHOLD = 800;
                    ManboValues.RUN_THRESHOLD = 1200;
                    //ManboValues.STAIRS_THRESHOLD = 3000;
                    Toast.makeText(getActivity(), "초기화 : 걷기-" + ManboValues.WALK_THRESHOLD +", 뛰기-" + ManboValues.RUN_THRESHOLD, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    // 서비스 브로드캐스트 리시버
    class MyMainLocalReceiver extends BroadcastReceiver {
        String datatype;

        public MyMainLocalReceiver(String datatype) {
            this.datatype = datatype;
        }

        public void onReceive(Context context, Intent intent) {
            if(datatype.equals("serviceDataWalk")) {
                serviceDataWalk = intent.getStringExtra(datatype);
                walkCount.setText(serviceDataWalk + " 걸음, " + (int)(0.0001 * Float.parseFloat(serviceDataWalk) * user.getWeight()) + " kcal.");

                Log.i(TAG, "serviceDataWalk received: " + serviceDataWalk);
            } else if (datatype.equals("serviceDataRun")) {
                serviceDataRun = intent.getStringExtra(datatype);
                runCount.setText(serviceDataRun + " 뜀, " + (int)(0.00016 * Float.parseFloat(serviceDataRun) * user.getWeight()) + " kcal.");

                Log.i(TAG, "serviceDataRun received: " + serviceDataRun);
            } /*else if (datatype.equals("serviceDataStairs")) {
                serviceDataStairs = intent.getStringExtra(datatype);
                stairsCount.setText(serviceDataStairs + "오름, "+ (int)(0.00023 * Float.parseFloat(serviceDataStairs) * user.getWeight()) + " kcal.");

                Log.i(TAG, "serviceDataStairs received: " + serviceDataStairs);
            }*/
        }
    }

}