package com.warm.tect.lightsnail.robot;

import android.app.Activity;
import android.util.Log;

public class Robot {
    private final String TAG = "Robot";
    private final Activity mActivity;
    private final RobotStatusCallBack mRobotStatusCallBack;
    private Ear mEar;//wake up
    private Mouth mMouth;//tts
    private Brain mBrain;//

    private MessageType currentMessageType = MessageType.Normal;
    public enum MessageType{
        Normal,//普通对话
        Song,//播放歌曲的消息回调
    }

    private String[] responseList = new String[]{
            "我在听",
            "什么事",
            "我在这里",
            "有何指教",
            "请指示",
            "呵,愚蠢的人类",
            "干嘛",
            "呵呵",
            "在洗澡"
    };
    private String[] cantHearList = new String[]{
            "请再说一遍",
            "我听不见",
            "我听不清",
            "很抱歉我没听到",
    };
    private String[] cantHearAgainList = new String[]{
            "在忙吗",
            "还在吗",
            "有在听吗",
    };
    private String[] leaveList = new String[]{
            "那你去忙把",
            "那我先走了",
            "那我离开一会儿",
            "那我等下再来",
            "等下再叫我噢"
    };
    private int unHeardCount = 0;//耳朵没听到的次数计数
    public Robot(Activity activity,RobotStatusCallBack robotStatusCallBack) {
        this.mActivity = activity;
        this.mRobotStatusCallBack = robotStatusCallBack;
        mBrain = new Brain(activity);
        mMouth = new Mouth(activity, new Mouth.MouthStatusCallBack()  {
            @Override
            public void OnSpeakFinish() {
                if(currentMessageType == MessageType.Song){
                    mRobotStatusCallBack.OnRobotLeaving();
                    return;
                }
                if(unHeardCount > 2){
                    mRobotStatusCallBack.OnRobotLeaving();
                }else{
                    mEar.StartListening();
                    mRobotStatusCallBack.OnRobotListening();
                }
            }
        });
        mEar = new Ear(activity,new Ear.EarStatusCallBack(){
            @Override
            public void BeWakeUp() {
                unHeardCount = 0;
                currentMessageType = MessageType.Normal;
                String robotSay =  responseList[(int)(responseList.length*Math.random())];
                mMouth.Speak(robotSay);
                mRobotStatusCallBack.OnRobotBeWakeUp();
                mRobotStatusCallBack.OnRobotSpeaking(robotSay);
            }
            @Override
            public void UnHeard() {
                unHeardCount++;
                String[] sayList = cantHearList;
                if(unHeardCount > 1){
                    sayList = cantHearAgainList;
                }
                if (unHeardCount > 2){
                    sayList = leaveList;
                }
                String robotSay =  sayList[(int)(sayList.length*Math.random())];
                mMouth.Speak(robotSay);
                mRobotStatusCallBack.OnRobotUnHeard();
                mRobotStatusCallBack.OnRobotSpeaking(robotSay);
            }
            @Override
            public void HeardSuccess(String result) {
                unHeardCount = 0;
                ReceiveHumanText(result);
            }
        });
        mEar.WaitingHumanRing();
        mRobotStatusCallBack.OnRobotWaitingForHumanRing();
    }
    public void ReceiveHumanText(String result) {
        mRobotStatusCallBack.OnRobotHeardYourSuccess(result);
        Log.e(TAG,"CurrentThread "+Thread.currentThread());
        mBrain.Thinking(result,new Brain.BrainStatusCallBack(){
            @Override
            public void OnThinkingFinish(String thinkResult,MessageType messageType) {
                currentMessageType = messageType;
                mMouth.Speak(thinkResult);
                mRobotStatusCallBack.OnRobotSpeaking(thinkResult);
            }
        });
    }

    public interface RobotStatusCallBack {

        void OnRobotBeWakeUp();//机器人被你叫醒了
        void OnRobotListening();//机器人正在听你讲话
        void OnRobotWaitingForHumanRing();//机器人在等人唤醒它
        void OnRobotUnHeard();//机器人没听到你的讲话
        void OnRobotHeardYourSuccess(String result);//机器人听到你在说的某句话
        void OnRobotSpeaking(String robotSay);//机器人在说话
        void OnRobotLeaving();//机器人失望的离开了
    }
}
