package com.warm.tect.lightsnail.robot;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Brain {
    private static String APIKEY = "e3d8ed67b2d34525a4c9c805c81f54d4";
    private Handler handler;
    private HandlerThread mHandlerThread;
    private Runnable mRunnable;
    private Handler mainHandler ;//将思考结果返回到主线程UI使用
    private BrainStatusCallBack mBrainStatusCallBack;//

    public Brain(){
        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                if(mBrainStatusCallBack != null){
                    mBrainStatusCallBack.OnThinkingFinish(result);
                }
            }
        };

        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());


    }
    public static String GetString(String question){
        String out = null;
        try {
            String info = URLEncoder.encode(question,"utf-8");
            URL url = new URL("http://www.tuling123.com/openapi/api?key="
                    + APIKEY + "&info=" + info);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if (code == 200){
                InputStream inputStream = connection.getInputStream();
                String result = StreamToString(inputStream);
                JSONObject object = new JSONObject(result);
                out = object.getString("text");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
    public static String StreamToString(InputStream in) {
        String result = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                out.flush();
            }
            result = new String(out.toByteArray(), "utf-8");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void Thinking(final String request,final BrainStatusCallBack brainStatusCallBack) {
        this.mBrainStatusCallBack = brainStatusCallBack;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                String  thinkingResult =  GetString(request);
                Message message = mainHandler.obtainMessage();
                message.obj = thinkingResult;
                message.sendToTarget();
            }
        };
        handler.removeCallbacks(mRunnable);
        handler.post(mRunnable);
    }

    public interface BrainStatusCallBack {
        void OnThinkingFinish(String thinkResult);
    }
}
