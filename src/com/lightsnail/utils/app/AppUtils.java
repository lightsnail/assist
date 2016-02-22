package com.lightsnail.utils.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AppUtils {
	/* 通过packagename启动应用
	 * @param context
	 * @param packagename
	 * */
	public static void startAPPFromPackageName(Context context,String packagename){
		Intent intent=isexit(context,packagename); 
	       if(intent!=null){  
//	    	   intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_NEW_TASK);
	  	     context.startActivity(intent);  
	       }
	}
	/**
	 * 通过packagename判断应用是否安装
	 * @param context
	 * @param packagename
	 * 
	 * @return 跳转的应用主activity Intent
	 * */
	public static Intent isexit(Context context,String pk_name){
		PackageManager packageManager = context.getPackageManager(); 
		Intent it= packageManager.getLaunchIntentForPackage(pk_name);
		return it;
	}
}
