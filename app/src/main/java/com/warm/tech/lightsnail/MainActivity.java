package com.warm.tech.lightsnail;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.warm.tech.float_manager.FloatViewService;
import com.warm.tech.float_manager.GlobleFloatManager;


public class MainActivity extends AppCompatActivity {
    private  FloatViewService mFloatViewService;
    private ServiceConnection mServiceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
         setContentView(R.layout.activity_main);
        GlobleFloatManager.GetInstance(this,this) ;

    }
}
