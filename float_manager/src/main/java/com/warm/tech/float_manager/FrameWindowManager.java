package com.warm.tech.float_manager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

@SuppressLint("NewApi") public class FrameWindowManager {

	private View mFrameLayout			= null;
	private float						mTouchStartX		= 0;
	private float						mTouchStartY		= 0;
	private float						mLastX				= 0;
	private float						mLasty				= 0;
	private WindowManager mManager			= null;
	private WindowManager.LayoutParams	mWindLayoutParams	= null;
	private Handler mHandler			= null;
	private Context mContext;
	private final static int			ALPHA				= 0;
	private final static int			CAN_PLAY_VOICE				= 1;
	private final static int			ALPHATIME			= 3 * 1000;
	private int mScreenWidth;
	private int mScreenHeight;
	private boolean	mCanPlayVoice = true;
	private ViewGroup gifLayout;
	private ImageView gifWait;
	private ImageView gifPick;
	private GifViewListener gifViewListener = new GifViewListener() {
		@Override
		public void OnClick() {
			AppLog.e("OnClick");
		}

		@Override
		public void OnLongClick() {
			AppLog.e("OnLongClick");
			Vibrate(mContext,50);
			showPickGif();
		}

		@Override
		public void OnMove(PointF mGloablePointF, PointF mTouchPointF) {
			updateViewPosition(mGloablePointF,mTouchPointF);
		}

		@Override
		public void OnCancel() {
			AppLog.e("OnCancel");
			showWaitGif();
		}
	};
	@SuppressWarnings("deprecation")
	public FrameWindowManager(Context context ){
		this.mContext = context;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		
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
				//	mVisualizerView.setAlpha(0.75f);
					break;
				case CAN_PLAY_VOICE:
					mCanPlayVoice = true;
					break;
				default:
					break;
				}
			}
		};
	}

	private void changeAlpha() {
		//mVisualizerView.setAlpha(1f);
		mHandler.removeMessages(ALPHA);
		mHandler.sendEmptyMessageDelayed(ALPHA, ALPHATIME);
	}

	private void initView(final Context context) {
		mFrameLayout = (ViewGroup)View.inflate(context,R.layout.frame_layout,null);
		gifLayout = mFrameLayout.findViewById(com.warm.tech.float_manager.R.id.gif_layout);
		gifWait = gifLayout.findViewById(R.id.gif_wait);
		gifPick = gifLayout.findViewById(R.id.gif_pick);
		final GifViewTouch touchController = new GifViewTouch(context,gifViewListener);
		gifLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return touchController.onTouchEvent(motionEvent,gifLayout);
			}
		});
	}
	
	public void updateViewPosition(PointF mLastPointF, PointF mTouchPointF) {
		mWindLayoutParams.x = (int) (mLastPointF.x - mTouchPointF.x);
		mWindLayoutParams.y = (int) (mLastPointF.y - mTouchPointF.y);
		mManager.updateViewLayout(mFrameLayout, mWindLayoutParams);
	}

	public void showWaitGif(){
		Glide.with(mContext).load(R.drawable.m1911_wait_alpha).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifWait);
	}
	public void showPickGif(){
		Glide.with(mContext).load(R.drawable.m1911_pick_alpha).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifWait);
	}
	private void show() {
		mManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mWindLayoutParams = new WindowManager.LayoutParams();
		mWindLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST  ;
		mWindLayoutParams.flags |= 8;
		mWindLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindLayoutParams.x = (int) (mScreenWidth  *4f/5);
		mWindLayoutParams.y = (int) (mScreenHeight *0);
		
		mWindLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.format = 1;
		mManager.addView(mFrameLayout, mWindLayoutParams);
	}

	private void dismmis() {
		mManager.removeView(mFrameLayout);
	}
	/*
         *
         * long milliseconds :震动的时长，单位是毫秒
         * long[] pattern :自定义震动模式，数组中的数字的含义依次是，静止时长，震动时长，静止时长，震动时长。时长的单位是毫秒
         * boolean isRepeat:是否反复震动，如果是true,反复震动，如果是false,只震动一次
         */
	public static void Vibrate(final Context context ,long milliseconds){
		Vibrator vib = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

}
