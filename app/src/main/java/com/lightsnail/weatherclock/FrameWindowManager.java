package com.lightsnail.weatherclock;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.lightsnail.specturm.VisualizerView;
import com.lightsnail.utils.VoiceTool;

@SuppressLint("NewApi") public class FrameWindowManager {

	private View						mFrameLayout			= null;
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
	private final static int			CAN_PLAY_VOICE				= 1;
	private final static int			ALPHATIME			= 3 * 1000;
	private int mScreenWidth;
	private int mScreenHeight;
	private boolean	mCanPlayVoice = true;
	private PlayVoiceService	mPlayVoiceService;

	@SuppressWarnings("deprecation")
	public  FrameWindowManager(Context context,PlayVoiceService playVoiceService ){
		this.mContext = context;
		this.mPlayVoiceService = playVoiceService;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display  display = wm.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		
		initData();
		initView(context);
		show();
		changeAlpha();
	}

	private void initData() {
		VoiceTool.getInstance(mContext);
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ALPHA:
					mVisualizerView.setAlpha(0.75f);
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
		mVisualizerView.setAlpha(1f);
		mHandler.removeMessages(ALPHA);
		mHandler.sendEmptyMessageDelayed(ALPHA, ALPHATIME);
	}

	private void initView(final Context context) {
		mFrameLayout = View.inflate(context, R.layout.frame_view, null);
		mFrameLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		mVisualizerView =  (VisualizerView)mFrameLayout.findViewById(R.id.frame_view_icon);
		mVisualizerView.setService(mPlayVoiceService,this);
//		mVisualizerView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//				mVisualizerView.setClickFlag(true);
//				if(mCanPlayVoice){
//					mCanPlayVoice = false;
////					Toast.makeText(context, "   Hello   光能蜗牛  ", Toast.LENGTH_SHORT).show();
//					int index = (int) (Math.random() * 6);
//					mHandler.removeMessages(CAN_PLAY_VOICE);
//					mHandler.sendEmptyMessageDelayed(CAN_PLAY_VOICE, 2000);
//					switch (index) {
//						case 0:
//
//							VoiceTool.getInstance(mContext).playVoice("101");
//						break;
//						case 1:
//
//							VoiceTool.getInstance(mContext).playVoice("102");
//
//						break;
//						case 2:
//							VoiceTool.getInstance(mContext).playVoice("101");
//
//						break;
//						case 3:
//							VoiceTool.getInstance(mContext).playVoice("102");
//						break;
//						case 4:
//							VoiceTool.getInstance(mContext).playVoice("103");
//
//						break;
//						case 5:
//							VoiceTool.getInstance(mContext).playVoice("104");
//						break;
//
//						default:
//						break;
//					}
//					
//					if(mPlayVoiceService.mBinder.getAccessibilityService() != null){
//						   mPlayVoiceService.mBinder.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//					 }else{
//							mVisualizerView.resetTaskIndex();
//						   Intent intent = new Intent(Intent.ACTION_MAIN);
//					        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
//					        intent.addCategory(Intent.CATEGORY_HOME);
//					        mContext.startActivity(intent);
//					 }
//				}
//			}
//		});
//		mVisualizerView.setOnLongClickListener(new OnLongClickListener() {
//		
//			@Override
//			public boolean onLongClick(View arg0) {
//				// TODO Auto-generated method stub
//				mVisualizerView.setClickFlag(true);
////				Toast.makeText(context, "   Hello   光能蜗牛 onLongClick ", Toast.LENGTH_SHORT).show();
//				return true;
//			}
//		});
//		mVisualizerView.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View v, MotionEvent event) {
//				changeAlpha();
//				mLastX = event.getRawX();
//				mLasty = event.getRawY() - mStatusBarHeight ;
////				AppLog.d("mLastX  "+mLastX+" & "+mLasty);
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					mTouchStartX = event.getRawX() - mWindLayoutParams.x;
//					mTouchStartY = event.getRawY() - mStatusBarHeight - mWindLayoutParams.y;
////					mTouchStartX = event.getX() ;
////					mTouchStartY = event.getY() ;
////					updateViewPosition();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					if(mVisualizerView.isClicked()){
//						updateViewPosition();
//					}
//					break;
//
//				case MotionEvent.ACTION_UP:
////					updateViewPosition();
//					mTouchStartX = mTouchStartY = 0;
//					mVisualizerView.setClickFlag(false);
//					break;
//				}
//				return false;
//			}
//		});
	}
	
	public void updateViewPosition(PointF mLastPointF, PointF mTouchPointF) {
		mWindLayoutParams.x = (int) (mLastPointF.x - mTouchPointF.x);
		mWindLayoutParams.y = (int) (mLastPointF.y - mTouchPointF.y);
		mManager.updateViewLayout(mFrameLayout, mWindLayoutParams);
	}

	public void setWendu(String wendu) {
		mVisualizerView.setWendu(wendu);
	}
	private void show() {
		mManager = (WindowManager) mContext.getSystemService("window");
		mWindLayoutParams = new WindowManager.LayoutParams();  
		mWindLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR  ;
		mWindLayoutParams.flags |= 8;
		mWindLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindLayoutParams.x = (int) (mScreenWidth  *4f/5);
		mWindLayoutParams.y = (int) (mScreenHeight *2f/5);
		
		mWindLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindLayoutParams.format = 1;
		mManager.addView(mFrameLayout, mWindLayoutParams);
	}

	private void dismmis() {
		mManager.removeView(mFrameLayout);
	}
	public void resetTaskIndex() {
		// TODO Auto-generated method stub
		mVisualizerView.resetTaskIndex();
	}


}
