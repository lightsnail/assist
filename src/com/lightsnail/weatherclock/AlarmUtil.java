package com.lightsnail.weatherclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lightsnail.utils.AppLog;

public class AlarmUtil {

	public static void setAlarm(Context mContext) {
		//操作：发送一个广播，广播接收后Toast提示定时操作完成
				Intent intent =new Intent(mContext, AlarmReceiver.class);
				intent.setAction("short");
				PendingIntent sender=
				PendingIntent.getBroadcast(mContext, 0, intent, 0);

				Calendar c_cur=Calendar.getInstance();
				Calendar c_set=Calendar.getInstance();
				c_set.set(Calendar.HOUR_OF_DAY,  6);
				c_set.set(Calendar.MINUTE,  59);
				c_set.set(Calendar.SECOND,  59);

				if(c_cur.getTimeInMillis()> c_set.getTimeInMillis()){
					c_set.add(Calendar.DAY_OF_YEAR , 1);
				}

				long  timestart = c_cur.getTimeInMillis();
				AppLog.d("start--time------------"+timestart);
				long enddd= c_set.getTimeInMillis();
				AppLog.d("enddd--time------------"+enddd);
				
				AppLog.d("count--time------------"+(enddd-timestart)/1000/60+" minute ");
				AlarmManager alarm=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
				alarm.set( AlarmManager.RTC_WAKEUP, c_set.getTimeInMillis(), sender);
	}

}
