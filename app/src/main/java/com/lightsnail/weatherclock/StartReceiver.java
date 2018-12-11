package com.lightsnail.weatherclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (context != null) {
			context.startService(new Intent(context, PlayVoiceService.class));
		}
	}

}
