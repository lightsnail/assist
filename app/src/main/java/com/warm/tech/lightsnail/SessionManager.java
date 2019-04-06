package com.warm.tech.lightsnail;

import android.app.Activity;
import android.widget.TextView;

import com.warm.tect.lightsnail.robot.Robot;

public class SessionManager {

    private  TextView mContentView;
    private Activity mActivity;
    private Robot robot;
    private String robotName = "小爱同学";
    private String humanName = "光能蜗牛";
    public SessionManager(Activity activity, TextView textView){
        this.mActivity = activity;
        this.mContentView = textView;
        robot = new Robot(activity,new Robot.RobotStatusCallBack(){
            @Override
            public void OnRobotBeWakeUp() {
                SpeakAsideAppendToContentView(robotName+"被你叫醒了");
            }
            @Override
            public void OnRobotListenning() {
                SpeakAsideAppendToContentView(robotName+"在听你讲话");
            }
            @Override
            public void OnRobotWaitingForHumanRing() {
                SpeakAsideAppendToContentView(robotName+"在等待人类召唤");
            }
            @Override
            public void OnRobotUnHeard() {
                SpeakAsideAppendToContentView(robotName+"没听到你的声音");
            }
            @Override
            public void OnRobotHeardYourSuccess(String result) {
                AppendToContentView(humanName,result);
            }
            @Override
            public void OnRobotSpeaking(String robotSay) {
                AppendToContentView(robotName,robotSay);
            }
        });
    }
    private void AppendToContentView(String charactor,String text) {
        mContentView.setText(mContentView.getText()+charactor+":"+text + "\n");
    }
    private void SpeakAsideAppendToContentView(String speakAside) {
        mContentView.setText(mContentView.getText()+speakAside+ "\n");
    }

}
