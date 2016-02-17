package com.lightsnail.weatherclock;

import com.lightsnail.utils.AppLog;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends  AccessibilityService{

	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		AppLog.d("onServiceConnected()");
	}
	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
		AppLog.d("onAccessibilityEvent = "+arg0.toString());
	}

	@Override
	public void onInterrupt() {
		AppLog.d("“onInterrupt()”");
	}
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
    private void setServiceInfo(int feedbackType) {
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        // We are interested in all types of accessibility events.
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        // We want to provide specific type of feedback.
//        info.feedbackType = feedbackType;
//        // We want to receive events in a certain interval.
//        info.notificationTimeout = EVENT_NOTIFICATION_TIMEOUT_MILLIS;
//        // We want to receive accessibility events only from certain packages.
//        info.packageNames = PACKAGE_NAMES;
//        setServiceInfo(info);
    }
}
