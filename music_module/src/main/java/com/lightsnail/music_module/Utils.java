package com.lightsnail.music_module;

import com.lightsnail.music.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public interface JsonDownloadListener{
        public  void onFailed(IOException e);
        public  void onSuccess(String json);
    };
    public interface JsonParseListener{
        public void onSuccess(Song song);
        public void onFailed();
    }
    public interface SongUrlRedirectListener {
        public void onFailed(IOException e);
        public void onSuccess(String s);
    }
    public static void getRedirectUrl(final String urlstring, final SongUrlRedirectListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlstring);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    System.out.println("返回码: " + connection.getResponseCode()) ;
                    //如果定向的地址经过重定向，
                    //那么conn.getURL().toString()显示的是重定向后的地址
                    String redirection_url = connection.getURL().toString();
                    // 关闭链接
                    connection.disconnect();
                    // 打印获取的结果
                    listener.onSuccess(redirection_url);
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed(e);
                }
            }
        }).start();
    }

    public static void parseJson(String json,JsonParseListener listener) {
        try {
            Song song = new Song();

            JSONArray jsonArray =   new JSONArray(json);
            JSONObject jsonObject =(JSONObject)jsonArray.get(0);
            song.setName(jsonObject.get("name").toString());
            song.setPicLink(jsonObject.get("picLink").toString());
            song.setId(jsonObject.get("id").toString());
            song.setSinger(jsonObject.get("singer").toString());
            song.setLyric(jsonObject.get("lyric").toString());
            song.setUrl(jsonObject.get("url").toString());

            listener.onSuccess(song);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void  downloadJson(final String urlstring,final JsonDownloadListener listener)   {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                // 我们需要进行请求的地址：
                String temp = urlstring;
                try {
                    // 1.URL类封装了大量复杂的实现细节，这里将一个字符串构造成一个URL对象
                    URL url = new URL(temp);
                    // 2.获取HttpURRLConnection对象
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    // 3.调用connect方法连接远程资源
                    connection.connect();
                    // 4.访问资源数据，使用getInputStream方法获取一个输入流用以读取信息
                    BufferedReader bReader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    // 对数据进行访问
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    // 关闭流
                    bReader.close();
                    // 关闭链接
                    connection.disconnect();
                    // 打印获取的结果
                    listener.onSuccess(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed(e);
                }
            }
        }).start();

    }
}
