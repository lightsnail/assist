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
			 serviceIntent.putExtra("voice", 
					 "报告:" +
					 "尊敬的宇宙突出贡献者，" +
			 		"人类文明的探索者，" +
			 		"时间旅行的先驱者，" +
			 		"光能蜗牛勇士 ，，" +
			 		"你所漂流的星球-地球， ，" +
			 		"当前时间格林泥治时间晚上0点，" +
			 		"北京时间早上八点，" +
			 		"结束休眠模式，" +
			 		"开启系统恢复倒计时 ~10~9~8~7~6~5~4~3~2~1~0，" +
			 		"系统恢复完成," +
			 		"报告完毕！"
			 		);
			 context.startService(serviceIntent); 
			 
//			  serviceIntent = new Intent(context, PlayVoiceService.class);
//			 serviceIntent.setAction("android.voice.play");
//			 serviceIntent.putExtra("voice", PlayVoiceService.mWeatherString);
//			 context.startService(serviceIntent); 

			 AlarmUtil.setAlarm(context);
//		      Toast.makeText(context, "short alarm", Toast.LENGTH_LONG).show();
		}else{
//		       Toast.makeText(context, "repeating alarm",Toast.LENGTH_LONG).show();
		  }
	}


}
