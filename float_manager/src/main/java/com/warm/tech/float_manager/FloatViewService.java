package com.warm.tech.float_manager;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by LightSnail on 2019/2/7.
 */

public class FloatViewService extends Service {
    private FrameWindowManager mFrameWindowManager;//浮动窗口
    private Activity mMainActivity;
    private Context mContext;
    public  LocalBinder mBinder = new LocalBinder(this);

    @Override
    public void onCreate() {
        super.onCreate();
        mContext =this;
        AppLog.e("FloatViewService---onCreate " );
        mFrameWindowManager = new FrameWindowManager(mContext);
        mFrameWindowManager.showWaitGif();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AppLog.e("FloatViewService---onBind " );
        return mBinder;
    }
    public void setActivity(Activity mainActivity) {
        this.mMainActivity = mainActivity;
    }
}
