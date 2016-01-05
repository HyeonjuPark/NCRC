package ncrc.nise.ajou.ac.kr.opa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import ncrc.nise.ajou.ac.kr.opa.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 스플래시 화면 종료 시간 설정
        finishSplashAfter(3);
    }

    /* 스플래시 화면 종료 시간 설정 : 초 */
    private void finishSplashAfter(long seconds) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, seconds * 1000);
    }
}
