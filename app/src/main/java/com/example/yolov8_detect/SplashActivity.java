package com.example.yolov8_detect;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 핸들러를 생성합니다.
        Handler handler = new Handler();

        // 5초(5000밀리초) 후에 실행되는 지연 작업(Runnable)을 생성합니다.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // MainActivity로 이동하기 위한 Intent를 생성합니다.
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                // MainActivity를 시작합니다.
                startActivity(intent);
                // 현재 열려있는 SplashActivity를 종료합니다.
                finish();
            }
        }, 1500);
        // 5초 동안 보여지는 스플래시 화면을 화면에 표시한 후에 MainActivity로 이동합니다.
        // MainActivity가 실행되면 현재 열려있는 SplashActivity를 종료하여 사용자가 뒤로가기 버튼을 눌렀을 때 SplashActivity로 돌아가지 않도록 합니다.
    }
}