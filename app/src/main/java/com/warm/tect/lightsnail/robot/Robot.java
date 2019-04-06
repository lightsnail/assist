package com.warm.tect.lightsnail.robot;

import android.app.Activity;

public class Robot {
    private final Activity mActivity;
    private final RobotStatusCallBack mRobotStatusCallBack;
    private Ear mEar;
    private Mouth mMouth;

    private String[] responseList = new String[]{
            "我在听",
            "什么事",
            "我在这里",
            "有何指教",
            "请指示",
            "呵,愚蠢的人类",
    };
    private String[] cantHearList = new String[]{
            "请再说一遍",
            "我听不见",
            "我听不清",
            "可以大声一些吗",
    };
    public Robot(Activity activity,RobotStatusCallBack robotStatusCallBack) {
        this.mActivity = activity;
        this.mRobotStatusCallBack = robotStatusCallBack;
        mMouth = new Mouth(activity, new Mouth.MouthStatusCallBack()  {
            @Override
            public void OnSpeakFinish() {
                mEar.StartListening();
                mRobotStatusCallBack.OnRobotListenning();
            }
        });
        mEar = new Ear(activity,new Ear.EarStatusCallBack(){
            @Override
            public void BeWakeUp() {
                String robotSay =  responseList[(int)(responseList.length*Math.random())];
                mMouth.Speak(robotSay);
                mRobotStatusCallBack.OnRobotBeWakeUp();
                mRobotStatusCallBack.OnRobotSpeaking(robotSay);
            }
            @Override
            public void UnHeard() {
                String robotSay =  cantHearList[(int)(cantHearList.length*Math.random())];
                mMouth.Speak(robotSay);
                mRobotStatusCallBack.OnRobotUnHeard();
                mRobotStatusCallBack.OnRobotSpeaking(robotSay);
            }
            @Override
            public void HeardSuccess(String result) {
                mRobotStatusCallBack.OnRobotHeardYourSuccess(result);
            }
        });
        mEar.WaitingHumanRing();
        mRobotStatusCallBack.OnRobotWaitingForHumanRing();
    }

    public interface RobotStatusCallBack {

        void OnRobotBeWakeUp();//机器人被你叫醒了
        void OnRobotListenning();//机器人正在听你讲话
        void OnRobotWaitingForHumanRing();//机器人在等人唤醒它
        void OnRobotUnHeard();//机器人没听到你的讲话
        void OnRobotHeardYourSuccess(String result);//机器人听到你在说的某句话
        void OnRobotSpeaking(String robotSay);//机器人在说话
    }
}
