package com.warm.tech.lightsnail.session;

import android.app.Activity;
import android.view.View;

import com.warm.tect.lightsnail.robot.Robot;

public class SessionManager {

    private  ContentView mContentView;
    private Activity mActivity;
    private Robot robot;
    private String robotName = "爱丽丝酱";
    private String humanName = "光能蜗牛";
    public SessionManager(Activity activity, View view){
        this.mActivity = activity;
        this.mContentView = new ContentView(activity,view);
        this.mContentView.SetOnSendClick(new ContentView.OnSendClickListener() {
            @Override
            public void OnSend(String content) {
                robot.ReceiveHumanText(content);
            }
        });
        robot = new Robot(activity,new Robot.RobotStatusCallBack(){
            @Override
            public void OnRobotBeWakeUp() {
                SpeakAside(robotName+"被你叫醒了");
            }
            @Override
            public void OnRobotListening() {
                SpeakAside(robotName+"在听你讲话");
            }
            @Override
            public void OnRobotWaitingForHumanRing() {
                SpeakAside(robotName+"在等待人类召唤");
            }
            @Override
            public void OnRobotUnHeard() {
                SpeakAside(robotName+"没听到你的声音");
            }
            @Override
            public void OnRobotHeardYourSuccess(String result) {
                AppendToHumanMessage(result);
            }
            @Override
            public void OnRobotSpeaking(String robotSay) {
                AppendToRobotMessage(robotSay);
            }
            @Override
            public void OnRobotLeaving() {
                SpeakAside(robotName+"离开了,你需要重新唤醒她");
            }
        });
    }
    private void AppendToHumanMessage(String text) {
        mContentView.HumanMessage(text);
    }
    private void AppendToRobotMessage( String text) {
        mContentView.RobotMessage(text);
    }
    private void SpeakAside(String speakAside) {
        mContentView.AsideSpeakMessage(speakAside);
    }

}
