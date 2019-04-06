package com.warm.tect.lightsnail.robot;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;
import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.RecogWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult;
import com.baidu.speech.asr.SpeechConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ear {

    private Activity mActivity;
    protected MyWakeup myWakeup;
    private static String TAG = "Ear";

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    /**
     * 0: 方案1， backTrackInMs > 0,唤醒词说完后，直接接句子，中间没有停顿。
     *              开启回溯，连同唤醒词一起整句识别。推荐4个字 1500ms
     *          backTrackInMs 最大 15000，即15s
     *
     * >0 : 方案2：backTrackInMs = 0，唤醒词说完后，中间有停顿。
     *       不开启回溯。唤醒词识别回调后，正常开启识别。
     * <p>
     *
     */
    private int backTrackInMs = 0;
    private EarStatusCallBack mEarStatusCallBack;

    public interface EarStatusCallBack {
        void BeWakeUp();//唤醒后的回调
        void UnHeard();//唤醒后没听到人声的回调
        void HeardSuccess(String result);//成功听到识别到人声的文字
    }
    public Ear(Activity activity, EarStatusCallBack earStatus){
        this.mActivity = activity;
        this.mEarStatusCallBack = earStatus;
        InFileStream.setContext(activity);
        //初始化权限
        initPermission();
        //初始化语音识别
        myRecognizer = new MyRecognizer(activity,  new MessageStatusRecogListener(new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    //Log.e(TAG,"msg.what "+msg.what);
                }
            }
        ){
            @Override
            public void onAsrFinalResult(String[] results, RecogResult recogResult) {
                super.onAsrFinalResult(results, recogResult);
                Log.e(TAG,"onAsrFinalResult " +results[0]);
                mEarStatusCallBack.HeardSuccess(results[0]);
            }

            @Override
            public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
                super.onAsrFinishError(errorCode, subErrorCode, descMessage, recogResult);
                Log.e(TAG,"onAsrFinishError "  );
                mEarStatusCallBack.UnHeard();
            }
        });
        //初始化识别唤醒
        myWakeup = new MyWakeup(activity,new RecogWakeupListener(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == IStatus.STATUS_WAKEUP_SUCCESS) { // 唤醒词识别成功的回调，见RecogWakeupListener

                    Log.e(TAG,"唤醒成功");
                    mEarStatusCallBack.BeWakeUp();
                }
            }
        }) );
    }
    public void StartListening() {
        // 此处 开始正常识别流程
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // 如识别短句，不需要需要逗号，使用1536搜索模型。其它PID参数请看文档
        params.put(SpeechConstant.PID, 1536);
        if (backTrackInMs > 0) {
            // 方案1  唤醒词说完后，直接接句子，中间没有停顿。开启回溯，连同唤醒词一起整句识别。
            // System.currentTimeMillis() - backTrackInMs ,  表示识别从backTrackInMs毫秒前开始
            params.put(SpeechConstant.AUDIO_MILLS, System.currentTimeMillis() - backTrackInMs);
        }
        myRecognizer.cancel();
        myRecognizer.start(params);
    }


    // 点击“开始识别”按钮
    // 基于DEMO唤醒词集成第2.1, 2.2 发送开始事件开始唤醒
    public void WaitingHumanRing() {
        Map<String, Object> params = new HashMap<String, Object>();
        // params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp20190303.bin");
        //params.put(SpeechConstant.APP_ID,LightSnailUtil.getMetaDataValue("com.baidu.speech.APP_ID",getApplicationContext()));
        params.put(SpeechConstant.APP_ID,8582573);

        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, toApplyList.toArray(tmpList), 123);
        }

    }

}
