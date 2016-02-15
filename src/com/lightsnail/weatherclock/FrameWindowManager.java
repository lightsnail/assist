package com.lightsnail.weatherclock;

import java.util.List;

import android.R.mipmap;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.tts.d.l;
import com.lightsnail.specturm.VisualizerView;

@SuppressLint("NewApi") public class FrameWindowManager {

	private View						mFrameview			= null;
	private VisualizerView				mVisualizerView			= null;
	private float						mTouchStartX		= 0;
	private float						mTouchStartY		= 0;
	private float						mLastX				= 0;
	private float						mLasty				= 0;
	private WindowManager				mManager			= null;
	private WindowManager.LayoutParams	mWindLayoutParams	= null;
	private Handler						mHandler			= null;
	private Context mContext;
	private final static int			ALPHA				= 0;
	private final static int			ALPHATIME			= 3 * 1000;
	private OnClickListener mClickListener;
	private int mStatusBarHeight;

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
	 
	    int statusHeight = -1;
	    try {
	        Class  clazz = Class.forName("com.android.internal.R$dimen");
	        Object object = clazz.newInstance();
	        int height = Integer.parseInt(clazz.getField("status_bar_height")
	                .get(object).toString());
	        statusHeight = context.getResources().getDimensionPixelSize(height);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return statusHeight;
	}
	public  FrameWindowManager(Context context,OnClickListener l){
		this.mContext = context;
		mClickListener= l;
		mStatusBarHeight = getStatusHeight(context);;
		
		
		initData();
		initView(context);
		show();
		changeAlpha();
	}

	private void initData() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ALPHA:
					mVisualizerView.setAlpha(0.75f);
					break;

				default:
					break;
				}
			}
		};
	}

	private void changeAlpha() {
		mVisualizerView.setAlpha(1f);
		mHandler.removeMessages(ALPHA);
		mHandler.sendEmptyMessageDelayed(ALPHA, ALPHATIME);
	}

	private void initView(final Context context) {
		mFrameview = View.inflate(context, R.layout.frame_view, null);
		mVisualizerView =  (VisualizerView)mFrameview.findViewById(R.id.frame_view_icon);
		mFrameview.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				changeAlpha();
				mLastX = event.getRawX();
				mLasty = event.getRawY() - mStatusBarHeight ;
//				AppLog.d("mLastX  "+mLastX+" & "+mLasty);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mTouchStartX = event.getX() ;
					mTouchStartY = event.getY() ;
//					AppLog.d("event.getX()  "+event.getX()+" & "+event.getY());
					updateViewPosition();
					break;
				case MotionEvent.ACTION_MOVE:
					if(mOperateMove){
						
						updateViewPosition();
					}
					break;

				case MotionEvent.ACTION_UP:
//					updateViewPosition();
					mTouchStartX = mTouchStartY = 0;
					mOperateMove = false;
					break;
				}
				return false;
			}
		});
		mFrameview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(context, "   Hello   光能蜗牛  ", Toast.LENGTH_SHORT).show();
				isActivityRunning(context);
//				mClickListener.onClick(v);
//				Runtime runtime = Runtime.getRuntime();
//				try {
//					runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
//                pm.goToSleep(SystemClock.uptimeMillis());
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						
//						new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//					}
//				}).start();
			}
		});
		mFrameview.setOnLongClickListener(new OnLongClickListener() {
			

			@Override
			public boolean onLongClick(View arg0) {
				mOperateMove = true;
				gosub(mContext);
				return false;
			}
		});
	}
	private boolean	mOperateMove;

	private ComponentName  mLastComponentActivity;
	private int mIndex = 1;
	public   boolean isActivityRunning(Context cxt ) {
		ActivityManager activityManager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		  List<RunningTaskInfo> list = activityManager.getRunningTasks(100);

		  mIndex++;
		  mIndex %= list.size();
		  for (int i = 0; i < list.size(); i++) {
			  RunningTaskInfo l = list.get(i);
//			   Log.d("debug", "  l.numActivities = "+l.numActivities + ",, mIndex = "+mIndex+ ",,"+"l.topActivity = "+l.topActivity);
			 if(i == mIndex){

				 
				  Intent intent = new Intent();
//				  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//				  intent.setAction(Intent.ACTION_MAIN);
				  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
				  intent.setComponent(l.topActivity);
				  mContext.startActivity(intent);

				 mLastComponentActivity = l.topActivity;
			 }
		  }
		// Log.d("debug", "activityName[i] = "+activityName[0]);
		// Log.d("debug", "componentName = "+componentName.getClassName());
		
		return false;
	}
	public   boolean gosub(Context cxt ) {
		ActivityManager activityManager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		  List<RunningTaskInfo> list = activityManager.getRunningTasks(100);
		   Log.d("debug", "gosub l.size = "+list.size()  );
		   if(mLastComponentActivity != null){
				  Intent intent = new Intent();
//			  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			  intent.setAction(Intent.ACTION_MAIN);
			  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
			  intent.setComponent(mLastComponentActivity);
			  mContext.startActivity(intent);
		   }
		   
//			  mIndex--;
//			  if(mIndex == -1){
//				  mIndex = list.size() - 1;
//			  }
//		  for (int i = 0; i < list.size(); i++) {
//			  RunningTaskInfo l = list.get(i);
//			   Log.d("debug", "l.numActivities = "+l.numActivities + ",, mIndex = "+mIndex+ ",,"+"l.topActivity = "+l.topActivity);
//			 if(i == 0){
////				  Intent intent = new Intent();
//////				  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//////				  intent.setAction(Intent.ACTION_MAIN);
////				  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
////				  intent.setComponent(l.topActivity);
////				  mContext.startActivity(intent);
//			 }
//		  }
		// Log.d("debug", "activityName[i] = "+activityName[0]);
		// Log.d("debug", "componentName = "+componentName.getClassName());
		
		return false;
	}
	private void updateViewPosition() {
		mWindLayoutParams.x = (int) (mLastX - mTouchStartX);
		mWindLayoutParams.y = (int) (mLasty - mTouchStartY);
		mManager.updateViewLayout(mFrameview, mWindLayoutParams);
	}

	public void setWendu(String wendu) {
		mVisualizerView.setWendu(wendu);
	}
	private void show() {
		mManager = (WindowManager) mContext.getSystemService("window");
		mWindLayoutParams = new WindowManager.LayoutParams();
		mWindLayoutParams.type = 2002;
		mWindLayoutParams.flags |= 8;
		mWindLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
//		mWindLayoutParams.x = 40;
//		mWindLayoutParams.y = 40;
		mWindLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.format = 1;
		mManager.addView(mFrameview, mWindLayoutParams);
	}

	private void dismmis() {
		mManager.removeView(mFrameview);
	}


}
