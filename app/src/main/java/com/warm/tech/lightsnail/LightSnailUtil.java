package com.warm.tech.lightsnail;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class LightSnailUtil {

    public  static String getMetaDataValue(String key,Context context) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString(key);
            Log.e("LightSnail","key = "+key);
            Log.e("LightSnail","msg = "+msg);
            return msg;
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }
}
