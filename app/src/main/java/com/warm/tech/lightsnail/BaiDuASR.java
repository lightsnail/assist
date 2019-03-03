package com.warm.tech.lightsnail;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.util.MyLogger;
import com.baidu.aip.asrwakeup3.core.wakeup.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.RecogWakeupListener;
import com.baidu.speech.asr.SpeechConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_NONE;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAITING_READY;

/**
 * Created by LightSnail on 2019/2/17.
 */

public class BaiDuASR {
    protected TextView txtLog;
    protected Button btn;
    protected Button setting;
    protected TextView txtResult;
    private int status = STATUS_NONE;
    //
    private  MyWakeup myWakeup;
    protected Handler handler;
    BaiDuASR(Activity activity){
        InFileStream.setContext(activity);
        initPermission(activity);

        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("LightSnail","MyWakeup handleMessage ");
                handleMsg(msg);
            }

        };
        MyLogger.setHandler(handler);
        initView(activity);
        IWakeupListener listener = new RecogWakeupListener(handler);
        myWakeup = new MyWakeup(activity, listener);
    }

    protected void initView(Activity activity) {
        txtResult = (TextView) activity.findViewById(R.id.txtResult);
        txtLog = (TextView) activity.findViewById(R.id.txtLog);
        btn = (Button) activity.findViewById(R.id.btn);
       // setting = (Button)activity. findViewById(R.id.setting);
        String descText = "";
        try {
            InputStream is = activity.getResources().openRawResource(R.raw.normal_wakeup);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            descText = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtLog.setText(descText);
        txtLog.append("\n");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (status) {
                    case STATUS_NONE:
                        start();
                        status = STATUS_WAITING_READY;
                        updateBtnTextByStatus();
                        txtLog.setText("");
                        txtResult.setText("");
                        break;
                    case STATUS_WAITING_READY:
                        stop();
                        status = STATUS_NONE;
                        updateBtnTextByStatus();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void updateBtnTextByStatus() {
        switch (status) {
            case STATUS_NONE:
                btn.setText("启动唤醒");
                break;
            case STATUS_WAITING_READY:
                btn.setText("停止唤醒");
                break;
            default:
                break;
        }
    }
    // 点击“开始识别”按钮
    // 基于DEMO唤醒词集成第2.1, 2.2 发送开始事件开始唤醒
    private void start() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }
    // 基于DEMO唤醒词集成第4.1 发送停止事件
    protected void stop() {
        myWakeup.stop();
    }
    protected void onDestroy() {
        // 基于DEMO唤醒词集成第5 退出事件管理器
        myWakeup.release();
    }
    protected void handleMsg(Message msg) {
        if (txtLog != null && msg.obj != null) {
            txtLog.append(msg.obj.toString() + "\n");
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission(Activity activity) {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), 123);
        }
    }
}
