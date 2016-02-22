package com.lightsnail.weatherclock;

import com.lightsnail.utils.AppLog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;


public class MainActivity extends Activity {

    private ServiceConnection	mServiceConnection;
	private PlayVoiceService mPlayVoiceService;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
		 Intent serviceIntent = new Intent(MainActivity.this, PlayVoiceService.class);
		 startService(serviceIntent);
		AppLog.d("MainActivity------------onCreate()");
		 mServiceConnection = new ServiceConnection() {

				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					 AppLog.d("MainActivity---onServiceConnected " );
					mPlayVoiceService = ((LocalBinder) service).getService();
					mPlayVoiceService.setActivity(MainActivity.this);
//					if (mPowerTimeService != null) {
//						mBindServiceListener.connected(mPowerTimeService);
//					}
				}
				@Override
				public void onServiceDisconnected(ComponentName name) {
					 AppLog.d("MainActivity---onServiceDisconnected " );
					 mPlayVoiceService = null;
//					mPowerTimeService = null;
//					if (mPowerTimeService != null) {
//						mBindServiceListener.disconnected();
//					}
				}

			};
			
			bindService(new Intent(MainActivity.this, PlayVoiceService.class),
					mServiceConnection, Context.BIND_AUTO_CREATE);

//			Intent intent = new Intent();
//			ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.ApplicationSettings");
//			intent.setComponent(cm);
//			intent.setAction("android.intent.action.VIEW");
//			startActivityForResult(intent, 0);
			
			
//		 finish();
    }
	@Override
	protected void onResume() {
		AppLog.d("MainActivity------------onResume()");
		super.onResume();
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				finish();
//			}
//		}, 500);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onTouchEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		finish();
		return false;
	}
	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		super.onDestroy();
	}

}
