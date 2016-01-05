package ncrc.nise.ajou.ac.kr.opa.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.TimeZone;

import ncrc.nise.ajou.ac.kr.opa.data.DBAdapter;
import ncrc.nise.ajou.ac.kr.opa.data.ManboValues;

public class PersistentService extends Service implements Runnable, SensorEventListener
{
    //LOG 출력을 위한 tag
    private String TAG = "PersistentService";

    private static final int REBOOT_DELAY_TIMER = 10 * 1000; // 서비스 재시작 연기 시간 10초(Activity 활성화 시간을 염두)
    private static final int LOCATION_UPDATE_DELAY = 3 * 1000; // 주기 시간 3초

    private Handler mHandler; //핸들러 선언
    private boolean mIsRunning;  // 상태 체크할 불리언 변수 선언

    /* 가속도 센서 기반 행동 탐지를 위한 변수들 */

    // 센서 얻어오기 위한 변수
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;


    private float speed; // 가속도 speed
    //private float stairsGap; // 걷기 X축 - Z축 사이 gap

    private long lastTime; // 이전 Time

    private float lastX, lastY, lastZ; // 이전 time의 X, Y, Z축 값
    private float x, y, z; // 현재 time의 X, Y, Z축 값

    // 가속도 X,Y,Z축 데이터
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    // DB 연동을 위한 변수
    DBAdapter dbAdapter;
    Cursor c;

    // 기본 개념 : onBind()가 추상메소드 이므로 문법적으로 반드시 재정의 해주어야 하고 모든 서비스는 onCreate()에서 생성되고 onDestroy()에서 종료
	/*
	최초에 서비스를 실행하고 서비스를 죽일 경우
	1. AlarmManager 등록
	2. 정해진 시간이 흐른 뒤 Intent를 BroadCasting
	3. BroadcastReceiver는 Broadcast된 Intent를 받고 미리 정의한 행동(PersistentService 살리기)을 수행
	*/

    @Override
    public IBinder onBind(Intent intent) {  //추상메소드 재정의는 필수
        return null;
    }

    @Override
    public void onCreate()
    {  //  onCreate()에서 서비스가 생성됨
        unregisterRestartAlarm();  //이미 등록된 알람은 제거
        super.onCreate(); //서비스 생성

        Log.i(TAG, "Service is created");

        mIsRunning = false; //상태 지정

        // DB 생성
        dbAdapter  = new DBAdapter(getApplicationContext());

        // 해당 날짜의 만보 데이터 읽기
        c = dbAdapter.readManbo(getTodayDate());
        if(c.getCount() == 0) { // 데이터가 없을 경우 만보 데이터를 0으로 초기화하여 삽입
            dbAdapter.insertScheduler(getTodayDate(), 0);
            Log.i(TAG, "Today's manbo was inserted");
            // 다시 만보 데이터 읽기
            c = dbAdapter.readManbo(getTodayDate());
        } else { // 데이터가 있을 경우
            c.moveToFirst();
            ManboValues.Step = c.getInt(2); // Manbo 숫자를 읽어 Step에 저장
            ManboValues.Run = c.getInt(3); // Run 숫자를 읽어 Step에 저장
        }

        // Sensor 설정
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // lastTime 초기화
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy()
    {
        registerRestartAlarm(); //서비스가 죽을 때 알람 등록

        super.onDestroy(); //서비스 종료

        mIsRunning = false;  //상태 지정

        // 센서 해제
        if(sensorManager != null)
            sensorManager.unregisterListener((SensorEventListener) this);
    }

    // 오늘 날짜 가져오기 ex) "2016-01-02"
    private String getTodayDate() {
        // 타임존 설정
        TimeZone timezone = TimeZone.getTimeZone("Etc/GMT-9");
        TimeZone.setDefault(timezone);

        long _timeMillis = System.currentTimeMillis();
        return DateFormat.format("yyyy-MM-dd", _timeMillis).toString();
    }


    @Override
    public void onStart(Intent intent, int startId) {  // 서비스 시작을 위한 메소드

        super.onStart(intent, startId); //서비스는 시작된다

        mHandler = new Handler(); //핸들러 선언
        mHandler.postDelayed(this, LOCATION_UPDATE_DELAY); // 잠시 보관 뒤 실행.(여기선 3초로 set) 일정 시간이 지난후에 메소드를 동작시킬 수 있다.
        mIsRunning = true; //상태 지정

        // 센서 등록
        if(accelerometerSensor != null)
            sensorManager.registerListener((SensorEventListener) this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void run()
    { //running 중인 상태에서 지속적으로 확인(실행)하는 코드

        // 만보, run DB에 업데이트
        dbAdapter.updateManbo(getTodayDate(), ManboValues.Step, ManboValues.Run);
        Log.i(TAG, "Updating Manbo : " + ManboValues.Step + ", Run : " + ManboValues.Run);

        if(!mIsRunning)  //서비스가 실행되고 있지 않다면
        {
            return;

        } else { //서비스가 실행 중이라면
            mHandler.postDelayed(this, LOCATION_UPDATE_DELAY);// 잠시 보관 뒤 실행.(여기선 3초로 set) 일정 시간이 지난후에 메소드를 동작시킬 수 있다.
            mIsRunning = true; //상태 지정
        }
    }

    private void registerRestartAlarm()
    { //재시작 알람을 등록하는 코드(메소드)

        Intent intent = new Intent(PersistentService.this, RestartService.class);//intent객체 생성
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);//intent 객체의 action 지정(서비스 재시작)
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);// 브로드케스트할 Intent

        long firstTime = SystemClock.elapsedRealtime(); // 현재 시간
        firstTime += REBOOT_DELAY_TIMER; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE); // 알람 서비스 등록
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, REBOOT_DELAY_TIMER, sender);//장치가 부팅된 이후로 지정된 길이의 시간이 지나면 intent발생 후 필요한 경우 장치를 깨움
    }

    private void unregisterRestartAlarm()
    {  //등록된 재시작 알람을 제거하는 코드(메소드)

        Intent intent = new Intent(PersistentService.this, RestartService.class); //intent객체 생성
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE); //intent 객체의 action 지정(서비스 재시작)
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);  //브로드케스트할 intent

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);  //알람 서비스 변수에 등록
        am.cancel(sender);  // 등록된 알람 제거
    }

    // 센서 변화시
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // 가속도 변화시
            // 현재 시간 가져오기
            long currentTime = System.currentTimeMillis();

            long gabOfTime = (currentTime - lastTime);

            if(gabOfTime > 100) { // 100ms에 한 번
                // lastTime 업데이트
                lastTime = currentTime;

                // X,Y,Z축 값 받아오기
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                // X,Y,Z축의 (현재 값-이전 값) 합의 절대값을 threshold로 사용하여
                // 현재 speed와 stairsGap을 구함.
                speed = Math.abs(x+y+z-lastX-lastY-lastZ) / gabOfTime * 10000;
                //stairsGap = Math.abs(2*(z-lastZ)-(x-lastZ)-(y-lastZ)) / gabOfTime * 10000;

                // 현재 Speed, StairsGap 업데이트
                ManboValues.Speed = speed;
                //ManboValues.StairsGap = stairsGap;

                if(speed > ManboValues.RUN_THRESHOLD) { // speed가 RUN_THRESHOLD 이상이면
                    // Run +1 증가
                    ManboValues.Run++;

                    // serviceDataRun intent로 run 수 보내기
                    Intent myFilteredResponse = new Intent("ncrc.nise.ajou.ac.kr.opa.run");
                    myFilteredResponse.putExtra("serviceDataRun", ManboValues.Run + "");
                    sendBroadcast(myFilteredResponse);

                    Log.i(TAG, "User is running : " + ManboValues.Run + " runs");
                } else if(speed > ManboValues.WALK_THRESHOLD) { // Walk
                    //if(stairsGap < ManboValues.STAIRS_THRESHOLD) { // Walking
                        // Step +1 증가
                        ManboValues.Step++;

                        // serviceDataWalk intent로 step 수 보내기
                        Intent myFilteredResponse = new Intent("ncrc.nise.ajou.ac.kr.opa.step");
                        myFilteredResponse.putExtra("serviceDataWalk", ManboValues.Step + "");
                        sendBroadcast(myFilteredResponse);

                        Log.i(TAG, "User is walking : " + ManboValues.Step + " steps");
                    //}  else { // Upstairs
                        // Upstairs +1 증가
                        //ManboValues.Upstairs++;

                        // serviceDataStairs intent로 upstairs 수 보내기
                        //Intent myFilteredResponse = new Intent("ncrc.nise.ajou.ac.kr.opa.stairs");
                        //myFilteredResponse.putExtra("serviceDataStairs", ManboValues.Upstairs + "");
                        //sendBroadcast(myFilteredResponse);

                        //Log.i(TAG, "User is going upstairs : " + ManboValues.Upstairs + " stairs");
                    //}
                } else {
                }

                // 현재  X,Y,Z 값을 이전 time의 X,Y,Z로 저장
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}