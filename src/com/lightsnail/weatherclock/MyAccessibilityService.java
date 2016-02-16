package com.lightsnail.weatherclock;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends  AccessibilityService{

	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
		
	}

	@Override
	public void onInterrupt() {
		
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
