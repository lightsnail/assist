package com.warm.tech.float_manager;

import android.os.Binder;

public class LocalBinder extends Binder {

	private FloatViewService	mFloatViewService;
//	private MyAccessibilityService	mMyAccessibilityService;

	public LocalBinder(FloatViewService  playVoiceService) {
		this.mFloatViewService = playVoiceService;
	}

	public FloatViewService getService() {
		return mFloatViewService;
	}

//	public void setMyAccessibilityService(MyAccessibilityService myAccessibilityService) {
//		this.mMyAccessibilityService = myAccessibilityService;
//	}
//
//	public MyAccessibilityService getAccessibilityService() {
//		return mMyAccessibilityService;
//	}

}
