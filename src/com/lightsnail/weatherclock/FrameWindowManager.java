package com.lightsnail.weatherclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("NewApi") public class FrameWindowManager {

	private View						mFrameview			= null;
	private View					mIconview			= null;
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
					mIconview.setAlpha(0.7f);
					break;

				default:
					break;
				}
			}
		};
	}

	private void changeAlpha() {
		mIconview.setAlpha(1f);
		mHandler.removeMessages(ALPHA);
		mHandler.sendEmptyMessageDelayed(ALPHA, ALPHATIME);
	}

	private void initView(final Context context) {
		mFrameview = View.inflate(context, R.layout.frame_view, null);
		mIconview =  mFrameview.findViewById(R.id.frame_view_icon);
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
					updateViewPosition();
					break;

				case MotionEvent.ACTION_UP:
					updateViewPosition();
					mTouchStartX = mTouchStartY = 0;
					break;
				}
				return false;
			}
		});
		mFrameview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "   Hello   光能蜗牛  ", Toast.LENGTH_SHORT).show();
//				mClickListener.onClick(v);
				
			}
		});
	}

	private void updateViewPosition() {
		mWindLayoutParams.x = (int) (mLastX - mTouchStartX);
		mWindLayoutParams.y = (int) (mLasty - mTouchStartY);
		mManager.updateViewLayout(mFrameview, mWindLayoutParams);
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
