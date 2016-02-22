package com.lightsnail.weatherclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("short")){

			 Intent serviceIntent = new Intent(context, PlayVoiceService.class);
			 serviceIntent.setAction("android.voice.play");
			 serviceIntent.putExtra("voice", "嗨，早上好，现在是北京时间 7点整，美好一天开始了呢~~");
			 context.startService(serviceIntent); 
			 
			  serviceIntent = new Intent(context, PlayVoiceService.class);
			 serviceIntent.setAction("android.voice.play");
			 serviceIntent.putExtra("voice", PlayVoiceService.mWeatherString);
			 context.startService(serviceIntent); 

			 AlarmUtil.setAlarm(context);
//		      Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
		}else{
//		       Toast.makeText(context, "repeating alarm",Toast.LENGTH_LONG).show();
		  }
	}


}
