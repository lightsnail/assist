package com.lightsnail.weatherclock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.lightsnail.utils.AppLog;
import com.lightsnail.utils.HttpConnectionUtil;
import com.lightsnail.utils.XMLAnalysisManager;
import com.lightsnail.utils.XMLType;
import com.lightsnail.weatherclock.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		 Intent serviceIntent = new Intent(MainActivity.this, PlayVoiceService.class);
		 startService(serviceIntent);
		 
		 finish();
    }

}
