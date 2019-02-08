package com.warm.tech.float_manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by LightSnail on 2019/2/6.
 */

public class GlobleFloatManager {
    private static GlobleFloatManager s_globle_float_manager;
    //成员变量
    private Context mContext;
    private FloatViewService mFloatViewService ;//浮动服务
    public static GlobleFloatManager GetInstance(Context context ,Activity activity){
        if(s_globle_float_manager == null){
            s_globle_float_manager= new GlobleFloatManager(context,activity);
        }
        return  s_globle_float_manager;
    }
    private GlobleFloatManager(Context context,final Activity activity){
        this.mContext = context;
        Intent serviceIntent = new Intent(activity, FloatViewService.class);
        mContext. startService(serviceIntent);
        ServiceConnection mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AppLog.e("MainActivity---onServiceConnected " );
                mFloatViewService = ((LocalBinder) service).getService();
                mFloatViewService.setActivity(activity);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                  AppLog.e("MainActivity---onServiceDisconnected " );
                mFloatViewService = null;
            }

        };
        mContext.bindService(new Intent(mContext, FloatViewService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
