package com.lightsnail.weatherclock;

import android.os.Binder;

public class LocalBinder extends Binder{

	private PlayVoiceService	mPlayerVoiceService;
	private MyAccessibilityService	mMyAccessibilityService;

	public LocalBinder(PlayVoiceService playVoiceService) {
		this.mPlayerVoiceService = playVoiceService;
	}

	public PlayVoiceService getService() {
		return mPlayerVoiceService;
	}

	public void setMyAccessibilityService(MyAccessibilityService myAccessibilityService) {
		this.mMyAccessibilityService = myAccessibilityService;
	}

	public MyAccessibilityService getAccessibilityService() {
		return mMyAccessibilityService;
	}

}
