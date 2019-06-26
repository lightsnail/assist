package com.warm.tect.lightsnail.robot;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.lightsnail.music_module.LightSnailMusicManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Brain {
    private   Context mContext;
    private Handler handler;
    private HandlerThread mHandlerThread;
    private Runnable mRunnable;
    private Handler mainHandler ;//将思考结果返回到主线程UI使用
    private BrainStatusCallBack mBrainStatusCallBack;//
    private RegularExpression localRegularExpression;

    public Brain(Context context){
        mContext = context;
        localRegularExpression = new RegularExpression();
        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                if(mBrainStatusCallBack != null){
                    mBrainStatusCallBack.OnThinkingFinish(result,Robot.MessageType.Normal);
                }
            }
        };

        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());


    }

    public void Thinking(final String request,final BrainStatusCallBack brainStatusCallBack) {
        this.mBrainStatusCallBack = brainStatusCallBack;

        //本地判断，若本地判断成功，则不访问云端
        if(localRegularExpression.checkMusicPlay(RegularExpression.P_COMM,request,brainStatusCallBack)){
            return ;
        }
        if(localRegularExpression.checkMusicPlay(RegularExpression.P_COMM2,request,brainStatusCallBack)){
            return ;
        }
        if(localRegularExpression.checkMusicStop(RegularExpression.P_COMM_STOP,request,brainStatusCallBack)){
            return ;
        }
        if(localRegularExpression.checkMusicStop(RegularExpression.P_COMM2_STOP,request,brainStatusCallBack)){
            return ;
        }

        //云端判断
        mRunnable = new Runnable() {
            @Override
            public void run() {
                //String  thinkingResult =  TulinRobot_V1.GetCloudResult(request);
                String  thinkingResult =  TulinRobot_V2.GetCloudResult(request);
                thinkingResult= thinkingResult.replaceAll("图灵机器人","小爱机器人");
                thinkingResult= thinkingResult.replaceAll("图灵","小爱");
                Message message = mainHandler.obtainMessage();
                message.obj = thinkingResult;
                message.sendToTarget();
            }
        };
        handler.removeCallbacks(mRunnable);
        handler.post(mRunnable);
    }

    public interface BrainStatusCallBack {
        void OnThinkingFinish(String thinkResult,Robot.MessageType messageType);
    }
      class RegularExpression{

        public static final String P_COMM = "播放歌曲(\\S+)";
        public static final String P_COMM2 = "播放(\\S+)";
        public   boolean checkMusicPlay(String comm, String requestString, BrainStatusCallBack brainStatusCallBack) {

            Pattern p=Pattern.compile(comm);
            Matcher m=p.matcher(requestString);

            if(m.find()){
                String song = m.group(1);
                LightSnailMusicManager.getInstance(mContext ).playMusic(song);

                String thinkingResult = "即将为您播放歌曲["+song+"]";
                brainStatusCallBack.OnThinkingFinish(thinkingResult,Robot.MessageType.Song);
                return true;
            }
            return false;
        }
        public static final String P_COMM_STOP = "停止播放(\\S*)";
        public static final String P_COMM2_STOP = "播放停止(\\S*)";
        public   boolean checkMusicStop(String comm, String requestString, BrainStatusCallBack brainStatusCallBack) {

            Pattern p=Pattern.compile(comm);
            Matcher m=p.matcher(requestString);

            if(m.find()){
                //String song = m.group(1);
                LightSnailMusicManager.getInstance(mContext ).stopMusic();

                String thinkingResult = "已为您停止歌曲";
                brainStatusCallBack.OnThinkingFinish(thinkingResult,Robot.MessageType.Normal);
                return true;
            }
            return false;
        }

    }
}
