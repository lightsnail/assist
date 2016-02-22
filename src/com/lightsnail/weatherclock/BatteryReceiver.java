package com.lightsnail.weatherclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
	        
		if(intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){

			  int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
		                            status == BatteryManager.BATTERY_STATUS_FULL;
		 
		        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

				Toast.makeText(context, "已连接电源!", Toast.LENGTH_LONG).show();
		}else if(intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){

			  int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
		                            status == BatteryManager.BATTERY_STATUS_FULL;
		 
		        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
				Toast.makeText(context, "已断开电源!", Toast.LENGTH_LONG).show();
				
		}
		
		if (Intent.ACTION_BATTERY_OKAY.equals(intent.getAction())) {
			Toast.makeText(context, "电量已恢复，可以使用!", Toast.LENGTH_LONG).show();
		}
		if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
			Toast.makeText(context, "电量过低，请尽快充电！", Toast.LENGTH_LONG).show();
		}
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
			Bundle bundle = intent.getExtras();
			// 获取当前电量
			int current = bundle.getInt("level");
			// 获取总电量
			int total = bundle.getInt("scale");
			StringBuffer sb = new StringBuffer();
			sb.append("当前电量为：" + current * 100 / total + "%" + "  ");
			// 如果当前电量小于总电量的15%
			if (current * 1.0 / total < 0.15) {
				sb.append("电量过低，请尽快充电！");
			} else {
				sb.append("电量足够，请放心使用！");
			}
			Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
		}

	}

}