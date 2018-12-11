package com.lightsnail.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class HttpConnectionUtil {

	public static String getHttpContent(String url) {
		return getHttpContent(url, "GB2312");
	}

	public static String getHttpContent(String url, String charSet) {
		Log.v("lm", "url = " + url);
		HttpURLConnection connection = null;
		String content = "";
		try {
			URL address_url = new URL(url);
			connection = (HttpURLConnection) address_url.openConnection();
			// connection.setRequestMethod("GET");
			// 设置访问超时时间及读取网页流的超市时�?,毫秒�?
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");

			// after JDK 1.5
			// connection.setConnectTimeout(10000);
			// connection.setReadTimeout(10000);
			// 得到访问页面的返回�??
			int response_code = connection.getResponseCode();
			if (response_code == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();
				// InputStreamReader reader = new
				// InputStreamReader(in,charSet);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, charSet));
				String line = null;
				while ((line = reader.readLine()) != null) {
					content += line ;
				}
				return content;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return "";
	}
	public static void download(String urlString, String filename,String savePath) throws Exception {  
        // 构�?�URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时�?5s  
        con.setConnectTimeout(5*1000);  
        // 输入�?  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓�?  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // �?始读�?  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链�?  
        os.close();  
        is.close();  
    }   

}
