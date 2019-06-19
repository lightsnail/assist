package com.warm.tect.lightsnail.robot;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Package: com.mydream.tulinrobot.controller
 * Description: TODO
 * Author: 李鹏伟
 * Date: Created in 2018/8/2016:58
 * Company: 公司
 * Copyright: Copyright (C) 2018
 * Version: 0.0.1
 * Modified By: 修改者
 */

public class TulinRobot_V2 {

    //存储APIkey
    public static final String API_KEY = "e3d8ed67b2d34525a4c9c805c81f54d4";
    //存储接口请求地址
    public static final String API_URL = "http://openapi.tuling123.com/openapi/api/v2";
    // 用户id
    public static final String USER_ID = "395040";

    public static String GetCloudResult(String request) {
        String requestMes = getReqMes(request);
        String responseString = tulinPost(API_URL,requestMes);

        return getResultMes(responseString);
    }

    /**
     * 获取可以传输的正确的json格式的请求字符串
     * @param reqMes 输入内容
     * @return
     */
    public static String getReqMes(String reqMes){
        // 请求json，里面包含reqType，perception，userInfo
        JSONObject reqJson = new JSONObject();
        try {
            // 输入类型:0-文本(默认)、1-图片、2-音频
            int reqType = 0;
            reqJson.put("reqType",reqType);

            // 输入信息,里面包含inputText，inputImage，selfInfo
            JSONObject perception = new JSONObject();
            // 输入的文本信息
            JSONObject inputText = new JSONObject();
            inputText.put("text",reqMes);
            perception.put("inputText",inputText);
//        // 输入的图片信息
//        JSONObject inputImage = new JSONObject();
//        inputImage.put("url","");
//        perception.put("inputImage",inputImage);
//        // 个人信息，里面包含location
//        JSONObject selfInfo = new JSONObject();
//        // 包含city，province，street
//        JSONObject location = new JSONObject();
//        location.put("city","");
//        location.put("province","");
//        location.put("street","");
//        selfInfo.put("location",location);
//        perception.put("selfInfo",selfInfo);
            // 用户信息
            JSONObject userInfo = new JSONObject();

            userInfo.put("apiKey",API_KEY);
            userInfo.put("userId",USER_ID);

            reqJson.put("perception",perception);
            reqJson.put("userInfo",userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reqJson.toString();
    }

    public static String tulinPost(String url, String reqMes) {

        String status = "";
        String responseStr = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            // 设置请求属性
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("x-adviewrtb-version", "2.1");
            // 发送POST请求必须设置如下两行
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConnection.getOutputStream());
            // 发送请求参数
            out.write(reqMes);
            // flush输出流的缓冲
            out.flush();
            httpUrlConnection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                responseStr += line;
            }
            status = new Integer(httpUrlConnection.getResponseCode()).toString();
//            System.out.println("status=============="+status);
//            System.out.println("response=============="+responseStr);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) { out.close();}
                if (in != null) {in.close();}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseStr;
    }

    public static String getResultMes(String tulinPostStr){

        Log.e("LightSnail","tulinPostStr "+tulinPostStr);
        String result = "";
        try {
            JSONObject thesultStr = new JSONObject(tulinPostStr);
            JSONArray results = (JSONArray )thesultStr.get("results");
            JSONObject resultObj =    (JSONObject)results.get(0) ;
            JSONObject values = (JSONObject)resultObj.get("values");
            result =   values.get("text").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}