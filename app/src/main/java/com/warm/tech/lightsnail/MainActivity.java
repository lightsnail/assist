package com.warm.tech.lightsnail;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.warm.tech.float_manager.FloatViewService;
import com.warm.tech.float_manager.GlobleFloatManager;


public class MainActivity extends AppCompatActivity {
    private BaiDuASR mBaiduASR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_main);
        //浮动窗口单例，目前把窗口暂时放在service里面
        GlobleFloatManager.GetInstance(this,this) ;

        //尝试测试百度代码
        mBaiduASR = new BaiDuASR(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mBaiduASR.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduASR.onDestroy();
    }
}
