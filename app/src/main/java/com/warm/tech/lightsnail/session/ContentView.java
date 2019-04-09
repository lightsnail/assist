package com.warm.tech.lightsnail.session;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warm.tech.lightsnail.R;

import java.util.ArrayList;

public class ContentView {
    private Activity mActivity;
    private RecyclerView mRecyclerview;
    private EditText mEditText;
    private ArrayList<Msg> mMsgList;
    private MsgAdapter mMsgAdapter;

    private OnSendClickListener mOnSendClickListener;
    public ContentView(Activity activity,View layout) {
        this.mActivity = activity;
        initView(layout);
        initData();
        initAdapter();
    }

    private void initAdapter() {
        mMsgAdapter = new MsgAdapter(mMsgList);
        mRecyclerview.setAdapter(mMsgAdapter);
    }

    /**
     * 初始化数据源
     */
    private void initData() {
        mMsgList = new ArrayList<>();
//        mMsgList.add(new Msg("Hello!", Msg.TYPE_RECEIVE));
//        mMsgList.add(new Msg("Hello! Who is that?", Msg.TYPE_SEND));
//        mMsgList.add(new Msg("This is Jack,Nice to meet you!", Msg.TYPE_RECEIVE));
    }

    public void RobotMessage(String content) {
        // 如果用户没有输入,则是一个空串""
        if (!content.isEmpty()) {
            mMsgList.add(new Msg(content, Msg.TYPE_RECEIVE));
            // 通知数据适配器刷新界面
            mMsgAdapter.notifyDataSetChanged();
            // 定位到最后一行
            mRecyclerview.scrollToPosition(mMsgList.size() - 1);
        }
    }
    public void HumanMessage(String content) {
        // 如果用户没有输入,则是一个空串""
        if (!content.isEmpty()) {
            mMsgList.add(new Msg(content, Msg.TYPE_SEND));
            // 通知数据适配器刷新界面
            mMsgAdapter.notifyDataSetChanged();
            // 定位到最后一行
            mRecyclerview.scrollToPosition(mMsgList.size() - 1);
            // 输入框置空
             mEditText.setText("");
        }
    }
    public void AsideSpeakMessage(String speakAside) {
        if (!speakAside.isEmpty()) {
            mMsgList.add(new Msg(speakAside, Msg.TYPE_ASIDE));
            // 通知数据适配器刷新界面
            mMsgAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 初始化控件
     */
    private void initView(View layout) {
        mRecyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
        mEditText = (EditText) layout.findViewById(R.id.et_input);
        Button bt_send = (Button)layout. findViewById(R.id.bt_send);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditText.getText().toString().trim();
                //HumanMessage(content);
                if(mOnSendClickListener != null){
                    mOnSendClickListener.OnSend(content);
                }
            }

        });

    }

    public interface  OnSendClickListener{

        void OnSend(String content);
    }
    public void SetOnSendClick(OnSendClickListener runnable) {
        this.mOnSendClickListener = runnable;
    }
}
