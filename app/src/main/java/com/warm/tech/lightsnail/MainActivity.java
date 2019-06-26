package com.warm.tech.lightsnail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lightsnail.music_module.TestMusicActiivty;
import com.warm.tech.float_manager.GlobleFloatManager;
import com.warm.tech.lightsnail.session.SessionManager;


public class MainActivity extends Activity {
    private SessionManager mSessionManager;//人类和机器人的会话管理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        //
        //setContentView(R.layout.main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.session_main);
        //浮动窗口单例，目前把窗口暂时放在service里面,暂时注释掉
        GlobleFloatManager.GetInstance(this,this) ;


        mSessionManager = new SessionManager(this,getWindow().getDecorView());

        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),TestMusicActiivty.class);
                startActivity(intent);
            }
        });
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this, SessionActivity.class);
//        startActivity(intent);

//        findViewById(R.id.tts).setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, com.baidu.tts.sample.MySynthActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //mBaiduASR.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mBaiduASR.onDestroy();
    }
}
