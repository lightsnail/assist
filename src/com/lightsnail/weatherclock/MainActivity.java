package com.lightsnail.weatherclock;

import com.lightsnail.utils.AppLog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;


public class MainActivity extends Activity {

    private ServiceConnection	mServiceConnection;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		 Intent serviceIntent = new Intent(MainActivity.this, PlayVoiceService.class);
		 startService(serviceIntent);
		AppLog.d("MainActivity------------onCreate()");
		 mServiceConnection = new ServiceConnection() {

				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					 AppLog.d("---onServiceConnected " );
//					mPowerTimeService = ((MyBinder) service).getService();
//					if (mPowerTimeService != null) {
//						mBindServiceListener.connected(mPowerTimeService);
//					}
				}
				@Override
				public void onServiceDisconnected(ComponentName name) {
					 AppLog.d("---onServiceDisconnected " );
//					mPowerTimeService = null;
//					if (mPowerTimeService != null) {
//						mBindServiceListener.disconnected();
//					}
				}

			};
			bindService(new Intent(MainActivity.this, PlayVoiceService.class),
					mServiceConnection, Context.BIND_AUTO_CREATE);

		 finish();
    }
	@Override
	protected void onDestroy() {
//		unbindService(mServiceConnection);
		super.onDestroy();
	}

}
