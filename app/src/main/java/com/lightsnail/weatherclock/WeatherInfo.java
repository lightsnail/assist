package com.lightsnail.weatherclock;

import java.util.ArrayList;
import java.util.List;

import com.lightsnail.utils.AppLog;
import com.lightsnail.utils.XMLNode;


public class WeatherInfo {

	public String city;
	public String updatetime;
	public String wendu;
	public String fengli;
	public String shidu;
	public String fengxiang;
	public String sunrise_1;
	public String sunset_1;
	public String sunrise_2;
	public String sunset_2;	
	public Environment mEnvironment;
	public Alarm mAlarm;
	public Yesterday mYesterday;
	public Forecast mForecast;
	public Zhishus mZhiShus;

	private void setZhiShus(List<XMLNode> childList) {
		mZhiShus = new Zhishus();
		for (int i = 0; i < childList.size(); i++) {
			XMLNode ccnode = childList.get(i);
			if(ccnode.getNodeName() .equals("zhishu")){
				mZhiShus.setZhiShus(ccnode.getChildList());
			}
		}
	}
	private void setForecast(List<XMLNode> childList) {
		mForecast = new Forecast();
		for (int i = 0; i < childList.size(); i++) {
			XMLNode ccnode = childList.get(i);
			if(ccnode.getNodeName() .equals("weather")){
				mForecast.setWeather(ccnode.getChildList());
			}
		}
	}


	private void setYesterday(List<XMLNode> childList) {
		mYesterday = new Yesterday();
		for (int i = 0; i < childList.size(); i++) {
			XMLNode ccnode = childList.get(i);
			if(ccnode.getNodeName() .equals("date_1")){
				mYesterday.date_1 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("high_1")){
				mYesterday.high_1 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("low_1")){
				mYesterday.low_1 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("day_1")){
				mYesterday.setDay(ccnode.getChildList());
			}else if(ccnode.getNodeName() .equals("night_1")){
				mYesterday.setNight(ccnode.getChildList());
			}
		}
	}
	private void setAlarm(List<XMLNode> childList) {
		mAlarm = new Alarm();
		for (int i = 0; i < childList.size(); i++) {
			XMLNode ccnode = childList.get(i);
			if(ccnode.getNodeName() .equals("cityKey")){
				mAlarm.cityKey = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("cityName")){
				mAlarm.cityName = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("alarmType")){
				mAlarm.alarmType = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("alarmDegree")){
				mAlarm.alarmDegree = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("alarmText")){
				mAlarm.alarmText = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("alarm_details")){
				mAlarm.alarm_details = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("standard")){
				mAlarm.standard = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("suggest")){
				mAlarm.suggest = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("imgUrl")){
				mAlarm.imgUrl = ccnode.getNodeValue();
			}else if(ccnode.getNodeName().equals("time")){
				mAlarm.time = ccnode.getNodeValue();
			}
		}
	}
	public void setEnvironment(List<XMLNode> childList) {
		mEnvironment = new Environment();
		for (int i = 0; i < childList.size(); i++) {
			XMLNode ccnode = childList.get(i);
			if(ccnode.getNodeName() .equals("aqi")){
				mEnvironment.aqi = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("pm25")){
				mEnvironment.pm25 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("suggest")){
				mEnvironment.suggest = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("quality")){
				mEnvironment.quality = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("MajorPollutants")){
				mEnvironment.MajorPollutants = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("o3")){
				mEnvironment.o3 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("co")){
				mEnvironment.co = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("pm10")){
				mEnvironment.pm10 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName() .equals("so2")){
				mEnvironment.so2 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName().equals("no2")){
				mEnvironment.no2 = ccnode.getNodeValue();
			}else if(ccnode.getNodeName().equals("time")){
				mEnvironment.time = ccnode.getNodeValue();
			}
			
		}
		
	}
	public void fill(XMLNode ccnode) {

//		AppLog.d("fill："+ccnode.getNodeName());
		if(ccnode.getNodeName() .equals("city")){
			city = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("updatetime")){
			updatetime = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("wendu")){
			wendu = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("fengli")){
			fengli = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("shidu")){
			shidu = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("fengxiang")){
			fengxiang = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("sunrise_1")){
			sunrise_1 = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("sunset_1")){
			sunset_1 = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("sunrise_2")){
			sunrise_2 = ccnode.getNodeValue();
		}else if(ccnode.getNodeName() .equals("sunset_2")){
			sunset_2 = ccnode.getNodeValue();
		}else if(ccnode.getNodeName().equals("environment")){
			setEnvironment(ccnode.getChildList());
		}else if(ccnode.getNodeName().equals("alarm")){
			setAlarm(ccnode.getChildList());
		}else if(ccnode.getNodeName().equals("yesterday")){
			setYesterday(ccnode.getChildList());
		}else if(ccnode.getNodeName().equals("forecast")){
			setForecast(ccnode.getChildList());
		}else if(ccnode.getNodeName().equals("zhishus")){
			setZhiShus(ccnode.getChildList());
		}
	}
	









	class Environment{

		public String time;
		public String no2;
		public String so2;
		public String pm10;
		public String co;
		public String o3;
		public String MajorPollutants;
		public String quality;
		public String suggest;
		public String pm25;
		public String aqi;
		
	}
	class Alarm{

		public String time;
		public String imgUrl;
		public String suggest;
		public String standard;
		public String alarm_details;
		public String alarmText;
		public String alarmDegree;
		public String alarmType;
		public String cityName;
		public String cityKey;
		
	}
	class Yesterday{

		public String low_1;
		public String high_1;
		public String date_1;
		public Day mDay;
		public Night mNight;
		
		public void setDay(List<XMLNode> childList) {
			mDay = new Day();
			for (int i = 0; i < childList.size(); i++) {
				XMLNode ccnode = childList.get(i);
				if(ccnode.getNodeName() .equals("type_1")){
					mDay.type_1 = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("fx_1")){
					mDay.fx_1 = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("fl_1")){
					mDay.fl_1 = ccnode.getNodeValue();
				}
			}
		}
		public void setNight(List<XMLNode> childList) {
			mNight = new Night();
			for (int i = 0; i < childList.size(); i++) {
				XMLNode ccnode = childList.get(i);
				if(ccnode.getNodeName() .equals("type_1")){
					mNight.type_1 = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("fx_1")){
					mNight.fx_1 = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("fl_1")){
					mNight.fl_1 = ccnode.getNodeValue();
				}
			}
		}
		class Day{

			public String fl_1;
			public String fx_1;
			public String type_1;
			
		}
		class Night{

			public String fl_1;
			public String fx_1;
			public String type_1;
			
		}
		
	}
	class Forecast{

		List<Weather> mWeathers = new ArrayList<Weather>();
		public void setWeather(List<XMLNode> childList) {
			Weather weather = new Weather();
			mWeathers.add(weather);
			for (int i = 0; i < childList.size(); i++) {
				XMLNode ccnode = childList.get(i);
				if(ccnode.getNodeName() .equals("date")){
					weather.date = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("high")){
					weather.high = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("low")){
					weather.low = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("day")){
					weather.setDay(ccnode.getChildList());
				}else if(ccnode.getNodeName() .equals("night")){
					weather.setNight(ccnode.getChildList());
				}
			}			
		}
		class Weather{

			public String low;
			public String high;
			public String date;
			public Day mDay;
			public Night mNight;
			public void setDay(List<XMLNode> childList) {
				mDay = new Day();
				for (int i = 0; i < childList.size(); i++) {
					XMLNode ccnode = childList.get(i);
					if(ccnode.getNodeName() .equals("type")){
						mDay.type = ccnode.getNodeValue();
					}else if(ccnode.getNodeName() .equals("fengxiang")){
						mDay.fengxiang = ccnode.getNodeValue();
					}else if(ccnode.getNodeName() .equals("fengli")){
						mDay.fengli = ccnode.getNodeValue();
					}
				}			
			}
			public void setNight(List<XMLNode> childList) {
				mNight = new Night();
				for (int i = 0; i < childList.size(); i++) {
					XMLNode ccnode = childList.get(i);
					if(ccnode.getNodeName() .equals("type")){
						mNight.type = ccnode.getNodeValue();
					}else if(ccnode.getNodeName() .equals("fengxiang")){
						mNight.fengxiang = ccnode.getNodeValue();
					}else if(ccnode.getNodeName() .equals("fengli")){
						mNight.fengli = ccnode.getNodeValue();
					}
				}			
			}
			class Day{
				public String fengli;
				public String fengxiang;
				public String type;
			}
			class Night{
				public String fengli;
				public String fengxiang;
				public String type;
				
			}
		}
	}
	public static class Zhishus{

		List<Zhishu> mZhishus = new ArrayList<Zhishu>();
		public void setZhiShus(List<XMLNode> childList) {
			Zhishu zhishu = new Zhishu();
			mZhishus.add(zhishu);
			for (int i = 0; i < childList.size(); i++) {
				XMLNode ccnode = childList.get(i);
				if(ccnode.getNodeName() .equals("name")){
					zhishu.name = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("value")){
					zhishu.value = ccnode.getNodeValue();
				}else if(ccnode.getNodeName() .equals("detail")){
					zhishu.detail = ccnode.getNodeValue();
				}
			}			
		}
		public static class Zhishu{

			public String detail;
			public String value;
			public String name;
			
		}
		public List<Zhishu> getZhishuS() {
			return mZhishus;			
		}


	}
}
