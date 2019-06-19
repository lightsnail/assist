package com.warm.tect.lightsnail.robot;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TulinRobot_V1 {

    private static String APIKEY = "e3d8ed67b2d34525a4c9c805c81f54d4";
    public static String GetCloudResult(String question){
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
}
