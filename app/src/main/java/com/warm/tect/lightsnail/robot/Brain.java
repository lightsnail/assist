package com.warm.tect.lightsnail.robot;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.lightsnail.music_module.LightSnailMusicManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Brain {
    private   Context mContext;
    private Handler handler;
    private HandlerThread mHandlerThread;
    private Runnable mRunnable;
    private Handler mainHandler ;//将思考结果返回到主线程UI使用
    private BrainStatusCallBack mBrainStatusCallBack;//

    public Brain(Context context){
        mContext = context;
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
        if(RegularExpression.checkSuccess(request,brainStatusCallBack)){
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
    static class RegularExpression{

        public static final String P_COMM = "播放歌曲(\\S+)";
        public static boolean checkSuccess(String requestString,BrainStatusCallBack brainStatusCallBack) {

            Pattern p=Pattern.compile(P_COMM);
            Matcher m=p.matcher(requestString);

            if(m.find()){
                String song = m.group(1);
                LightSnailMusicManager.getInstance( ).playMusic(song);

                String thinkingResult = "即将为您播放歌曲["+song+"]";
                brainStatusCallBack.OnThinkingFinish(thinkingResult,Robot.MessageType.Song);
                return true;
            }
            return false;
        }

    }
}
