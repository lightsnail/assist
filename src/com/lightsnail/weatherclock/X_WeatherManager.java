package com.lightsnail.weatherclock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.content.Context;

import com.lightsnail.utils.AppLog;
import com.lightsnail.utils.HttpConnectionUtil;
import com.lightsnail.utils.XMLAnalysisManager;
import com.lightsnail.utils.XMLType;

public class X_WeatherManager {

	private Context mContext;

	public X_WeatherManager(Context context,final OnWeatherStringListenner l){

		this.mContext = context;
        new Thread(new Runnable() {
			
			@SuppressLint("NewApi") @Override
			public void run() {

				String location;
				try {
					location = URLEncoder.encode("深圳", "utf-8");	
//					String ak = "MfmNGMP4RkqdnL6rGzTCSNYI";
					String url = "http://wthrcdn.etouch.cn/WeatherApi?city="+location;
					
					AppLog.d(url);
					String resultString = HttpConnectionUtil.getHttpContent(url, "utf-8");
					
					String dir =  mContext.getFilesDir().getAbsolutePath();
					String weatherPath = dir + "/weather.xml";
					AppLog.d(weatherPath);
					File file = new File(weatherPath);
					if(file.exists()){
						file.delete();
					}
					file = new File(weatherPath);
					
//					AppLog.d(resultString);
					FileWriter fw  =	new FileWriter(weatherPath);
					fw.write(resultString);
					fw.flush();
					fw.close();
					 XMLAnalysisManager.getXMLNode(mContext, weatherPath, XMLType.SDCARD,new OnWeatherPaserListenner() {
						@Override
						public void onFinish(WeatherInfo weatherInfo) {
							String text;
							text = "";
							text += "你有一条天气信息!";
							text += weatherInfo.city + ",";
							text += weatherInfo.updatetime + ",";
							text += "温度："+weatherInfo.wendu + ",";
							text += "风力："+weatherInfo.fengli + ",";
							text += "湿度："+weatherInfo.shidu + ",";
							l.onWeatherString(text);
							l.onWeatherWendu(weatherInfo.wendu);
						}
					});
					
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
