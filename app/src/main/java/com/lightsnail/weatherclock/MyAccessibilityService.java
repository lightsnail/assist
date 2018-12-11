package com.lightsnail.weatherclock;

import com.lightsnail.utils.AppLog;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;

import com.lightsnail.utils.AppLog;
import com.lightsnail.utils.app.AppUtils;
import com.lightsnail.utils.string.StringUtil;

public class MyAccessibilityService extends AccessibilityService {

	public static final String	QQ_PACKAGENAME		= "com.tencent.mobileqq";
	public static final String	WEIXIN_PACKAGENAME	= "com.tencent.mm";
	public static final String	HONGBAO_PACKAGENAME	= QQ_PACKAGENAME + WEIXIN_PACKAGENAME;
	private ServiceConnection	mServiceConnection;
	private PlayVoiceService	mPlayVoiceService;
	private String	 mLastMessage;

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		AppLog.d("MyAccessibilityService------------bindService()");
		// TODO Auto-generated method stub
		return super.bindService(service, conn, flags);
		
	}
	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		AppLog.d("MyAccessibilityService------------onServiceConnected()");
		 mServiceConnection = new ServiceConnection() {
			 @Override
				public void onServiceConnected(ComponentName name, IBinder service) {

				 AppLog.d("MyAccessibilityService---onServiceConnected "  );
				 AppLog.d("onServiceConnected MyAccessibilityService.this = "+MyAccessibilityService.this );
					 mPlayVoiceService = ((LocalBinder) service).getService();
					 ((LocalBinder) service).setMyAccessibilityService(MyAccessibilityService.this);
//					if (mPowerTimeService != null) {
//						mBindServiceListener.connected(mPowerTimeService);
//					}
				}
				@Override
				public void onServiceDisconnected(ComponentName name) {

					 AppLog.d("MyAccessibilityService---onServiceDisconnected "  );
					mPlayVoiceService = null;
//					if (mPowerTimeService != null) {
//						mBindServiceListener.disconnected();
//					}
				}
				
			};
			bindService(new Intent(this, PlayVoiceService.class),
					mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	@SuppressLint("NewApi") @Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
//		AppLog.d(arg0.toString());
		AppLog.d(arg0.getPackageName().toString()+"");
//		AppLog.d(arg0.getText().toString()+"");
		switch (arg0.getEventType()) {
			case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
				String textMessage = StringUtil.stringFilter(arg0.getText().toString())  ;
				AppLog.d("textMessage = "+textMessage);
				String packName = arg0.getPackageName().toString();
				if(textMessage != null && !textMessage.isEmpty() && !textMessage.equals(mLastMessage)){
					
					 mLastMessage = textMessage;
					 Intent serviceIntent = new Intent(getApplicationContext(), PlayVoiceService.class);
					 serviceIntent.setAction("android.voice.play");
					 serviceIntent.putExtra("voice", textMessage) ;
					 PlayVoiceService.PLAYSYSTLE = PlayVoiceService.PLAYSYSTLE == 0 ? 1 : 0;
					 startService(serviceIntent);
				}
				if (HONGBAO_PACKAGENAME.contains(packName) &&  textMessage != null && textMessage.contains("红包") ) {
					AppUtils.startAPPFromPackageName(getApplicationContext(), packName);
				}
				
			break;
			case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
			break;
			default:
			break;
		}

		// if(getRootInActiveWindow()==null)
		// return;

		// //通过文字找到当前的节点
		// List<AccessibilityNodeInfo> nodes =
		// getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
		// for (int i = 0; i < nodes.size(); i++)
		// {
		// AccessibilityNodeInfo node = nodes.get(i);
		// // 执行按钮点击行为
		// if
		// (node.getClassName().equals("android.widget.Button")&&node.isEnabled())
		// {
		// node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
		// }
		// }
	}

	/**
	 * EventType: TYPE_NOTIFICATION_STATE_CHANGED; EventTime: 18863813;
	 * PackageName: com.tencent.mobileqq; MovementGranularity: 0; Action: 0 [
	 * ClassName: android.app.Notification; Text: [光能蜗牛: 123];
	 * ContentDescription: null; ItemCount: -1; CurrentItemIndex: -1; IsEnabled:
	 * false; IsPassword: false; IsChecked: false; IsFullScreen: false;
	 * Scrollable: false; BeforeText: null; FromIndex: -1; ToIndex: -1; ScrollX:
	 * -1; ScrollY: -1; MaxScrollX: -1; MaxScrollY: -1; AddedCount: -1;
	 * RemovedCount: -1; ParcelableData: Notification(pri=0
	 * contentView=com.tencent.mobileqq/0x1090066 vibrate=null sound=null
	 * defaults=0x0 flags=0x11 kind=[null]) ]; recordCount: 0
	 */

	/**
	 * EventType: TYPE_WINDOW_STATE_CHANGED; EventTime: 19548786; PackageName:
	 * com.android.systemui; MovementGranularity: 0; Action: 0 [ ClassName:
	 * android.widget.FrameLayout; Text: [通知栏。]; ContentDescription: null;
	 * ItemCount: -1; CurrentItemIndex: -1; IsEnabled: true; IsPassword: false;
	 * IsChecked: false; IsFullScreen: false; Scrollable: false; BeforeText:
	 * null; FromIndex: -1; ToIndex: -1; ScrollX: -1; ScrollY: -1; MaxScrollX:
	 * -1; MaxScrollY: -1; AddedCount: -1; RemovedCount: -1; ParcelableData:
	 * null ]; recordCount: 0
	 */

	/**
	 * EventType: TYPE_NOTIFICATION_STATE_CHANGED; EventTime: 19986798;
	 * PackageName: com.tencent.mm; MovementGranularity: 0; Action: 0 [
	 * ClassName: android.app.Notification; Text: [winnie: [呲牙]];
	 * ContentDescription: null; ItemCount: -1; CurrentItemIndex: -1; IsEnabled:
	 * false; IsPassword: false; IsChecked: false; IsFullScreen: false;
	 * Scrollable: false; BeforeText: null; FromIndex: -1; ToIndex: -1; ScrollX:
	 * -1; ScrollY: -1; MaxScrollX: -1; MaxScrollY: -1; AddedCount: -1;
	 * RemovedCount: -1; ParcelableData: Notification(pri=1
	 * contentView=com.tencent.mm/0x1090066 vibrate=[] sound=null defaults=0x0
	 * flags=0x1 kind=[null]) ]; recordCount: 0
	 */
	@Override
	public void onInterrupt() {

		AppLog.d("------------onInterrupt()");
	}

	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		AppLog.d("------------onUnbind()");
		unbindService(mServiceConnection);
		return super.onUnbind(intent);
	}

	private void setServiceInfo(int feedbackType) {
		// AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		// // We are interested in all types of accessibility events.
		// info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
		// // We want to provide specific type of feedback.
		// info.feedbackType = feedbackType;
		// // We want to receive events in a certain interval.
		// info.notificationTimeout = EVENT_NOTIFICATION_TIMEOUT_MILLIS;
		// // We want to receive accessibility events only from certain
		// packages.
		// info.packageNames = PACKAGE_NAMES;
		// setServiceInfo(info);
	}
}
