package com.warm.tech.lightsnail.session;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warm.tech.lightsnail.R;

import java.util.ArrayList;

public class SessionActivity extends AppCompatActivity {

    private ContentView mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_main);
        mContentView = new ContentView(this,getWindow().getDecorView());
    }

}
