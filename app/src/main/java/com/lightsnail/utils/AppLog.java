package com.lightsnail.utils;

import android.util.Log;

public class AppLog {

	private static final boolean	DEBUG	= true;
	private static final String	TAG	= "debug";

	public static void d(String string) {

		if(DEBUG){
			Log.d(TAG, string);
		}
		
	}

}
