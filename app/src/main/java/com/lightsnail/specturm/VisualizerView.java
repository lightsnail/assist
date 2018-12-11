package com.lightsnail.specturm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.lightsnail.utils.AppLog;
import com.lightsnail.utils.VoiceTool;
import com.lightsnail.weatherclock.FrameWindowManager;
import com.lightsnail.weatherclock.PlayVoiceService;
import com.lightsnail.weatherclock.TranslucentActivity;

@SuppressLint("NewApi") 
public class VisualizerView extends TextView {

	private int mHeight;
	private int mWidth;
	private int mCenterX;
	private int mCenterY;
	private Paint mPaint;
	private boolean mInit;
	private float mStartR = .54f;
	private float mEndR = 1.15f;
	private float mR = 0.50f;
	private float mBackR = 0.80f;
	private ArrayList< Line> mArrayList = new ArrayList<VisualizerView.Line>();
	private int mCaptureSize = Visualizer.getCaptureSizeRange()[0] ;
	private int mModelSize = mCaptureSize/2 - 1;
	private float[] mModel = new float[mModelSize] ;
	private float[] mLastModel = new float[mModelSize] ;
	private Visualizer mVisualizer;
	private int mStaticCount = 0;
	
	private int mStatusBarHeight;
	
	private float mMax = ((float) Math.log(127) );
	private int mMainColor;
//	private int mMainColorHalf;
	
	private int mBackColor;
	private int	mTextColor;
	private boolean	mNormalShow;
	private ZidooScroller mZidooScrollerX;
	private ZidooScroller mZidooScrollerY;
	private Context	mContext;
	private List<String>	mHomeList;
	private int mCompareX;
	private int mCompareY;
	
	public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mStatusBarHeight = getStatusHeight(context);;
	}

	public VisualizerView(Context context) {
		super(context);
		this.mContext = context;
	}
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
	
	/** 
	* 获得属于桌面的应用的应用包名称 
	* @return 返回包含所有包名的字符串列表 
	*/
	private List<String> getHomes() { 
	  List<String> names = new ArrayList<String>(); 
	  PackageManager packageManager = mContext.getPackageManager(); 
	  //属性 
	  Intent intent = new Intent(Intent.ACTION_MAIN);
	  intent.addCategory(Intent.CATEGORY_HOME); 
	  List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, 
	    PackageManager.MATCH_DEFAULT_ONLY); 
	  for(ResolveInfo ri : resolveInfo){ 
		   names.add(ri.activityInfo.packageName); 
		   AppLog.d("packageName = " + ri.activityInfo.packageName);
	  } 
	  names.add(mContext.getApplicationContext().getPackageName());//排除应用自身
	  
	  return names;
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(mInit == false){

			mInit = true;
			mHomeList = getHomes();
			
			this.mWidth = getWidth();
			this.mHeight = getHeight();;
			this.mCompareX = mWidth/5;
			this.mCompareY = mHeight/5;
			this.mCenterX = mWidth/2;
			this.mCenterY = mHeight/2;
			this.mPaint= new Paint();
			this.mPaint.setColor(Color.WHITE);
			this.mPaint.setAntiAlias(true);
			int alpha = 255;
			int red = 100;
			int green = 255;
			int blue = 0;

			
			this.mMainColor =Color.argb(alpha, red, green, blue);
//			this.mMainColorHalf =Color.argb((int) (alpha/1.5f), red, green, blue);
			this.mTextColor = Color.argb(255, (red+100)%255, (green+100)%255, (blue+100)%255);
			this.mBackColor = Color.parseColor("#66000000");
			float count = mModelSize ;
			for (int i = 0; i <count; i++) {
				float angel = (float) ((i + 0.5f)/count*Math.PI* 2 );
				float starAngle = angel- (float)(1 /count*Math.PI* 2)*.5f;
				float endAngle = angel+ (float)(1 /count*Math.PI* 2)*.5f;
				float startR = this.mCenterX *mStartR;
				float endR = this.mCenterX * mEndR ;
				Vector  vector = new Vector(startR,endR,angel);
				mArrayList.add(new Line(starAngle,endAngle,startR,endR,vector));
			}
			
			mVisualizer = new Visualizer(0);
			mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {


				 public   double llog(double value,double base){
					return Math.log(value) /Math.log(base);
				}
				@Override
				public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
					// TODO Auto-generated method stub
		            Log.d("debug", "onWaveFormDataCapture ");
	            	invalidate();
				}
				
				@Override
				public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

//					AppLog.d("fft.lenth  = "+(fft.length-2)/2 + ",,,mCaptureSize = "+mCaptureSize);
					int index = 0;
		            for (int i = 2; i < fft.length;i+=2) {  
		            	mModel[index] = (float) Math.hypot(fft[i], fft[i+1]) ; 
		            	mModel[index] =   (float) Math.log(mModel[index]+1);
//		            	mModel[index] =   (float) llog(mModel[index]+1,Math.E);
		            	
//		            	mModel[index] = mLastModel[index] + (mModel[index]  - mLastModel[index]) * 0.4f ;
//		            	mLastModel[index] = mModel[index];
//		            	mModel[index] =  mModel[index];
		            	index++;
//		                string += mModel[i/2-1]+"__";
		            }  	
//		            Log.d("debug", "onFftDataCapture "+string);
//					Log.d("debug", "-------samplingRate "+samplingRate );
					
		            if(mModel[0] == 0){
		            	mStaticCount ++ ;
		            	if(mStaticCount < 10){
//							Log.d("debug", "---quite--"+mStaticCount );
		            		invalidate();
		            	}else if(mStaticCount < 20){
		            		mNormalShow = true;
		            		invalidate();
		            	}
		            	if(mStaticCount > 1000){
		            		mStaticCount = 10;
		            	}
						
		            }else{
	            		mNormalShow = false;
		            	mStaticCount = 0;
		            	invalidate();
		            }
//					mIEffect.draw(mModel,mMax);
				}
			}, Visualizer.getMaxCaptureRate(), false, true);
			mVisualizer.setCaptureSize(mCaptureSize);
			mVisualizer.setEnabled(true);
			mZidooScrollerX = new ZidooScroller(mContext, 0, 1, 
					 new OvershootInterpolator()  
					//new BounceInterpolator ()
			);
			mZidooScrollerY = new ZidooScroller(mContext, 0, 1, 
					 new OvershootInterpolator()  
					//new BounceInterpolator ()
			);
			setTextSize(10);
			setTextColor(mTextColor);
			setGravity(Gravity.CENTER);

		}
	}    
	public void OpenNotify() {
		// TODO Auto-generated method stub
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		try {
			Object service =mContext. getSystemService("statusbar");
			Class<?> statusbarManager = Class
					.forName("android.app.StatusBarManager");
			Method expand = null;
			if (service != null) {
				if (currentApiVersion <= 16) {
					expand = statusbarManager.getMethod("expand");
				} else {
					expand = statusbarManager
							.getMethod("expandNotificationsPanel");
				}
				expand.setAccessible(true);
				expand.invoke(service);
			}

		} catch (Exception e) {
		}

	}
	public void showRecentlyAppPanel() {
		Class serviceManagerClass;
		try {
			serviceManagerClass = Class.forName("android.os.ServiceManager");
			Method getService = serviceManagerClass.getMethod("getService",
					String.class);
			IBinder retbinder = (IBinder) getService.invoke(
					serviceManagerClass, "statusbar");
			Class statusBarClass = Class.forName(retbinder
					.getInterfaceDescriptor());
			Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
					"asInterface", IBinder.class).invoke(null,
					new Object[] { retbinder });
			Method clearAll = statusBarClass.getMethod("toggleRecentApps");
			clearAll.setAccessible(true);
			clearAll.invoke(statusBarObject);
			
//			AppLog.d(ReflectUtils.printMethods(statusBarClass));; 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void closeRecentlyAppPanel() {
		Class serviceManagerClass;
		try {
			serviceManagerClass = Class.forName("android.os.ServiceManager");
			Method getService = serviceManagerClass.getMethod("getService",
					String.class);
			IBinder retbinder = (IBinder) getService.invoke(
					serviceManagerClass, "statusbar");
			Class statusBarClass = Class.forName(retbinder
					.getInterfaceDescriptor());
			Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
					"asInterface", IBinder.class).invoke(null,
					new Object[] { retbinder });
			Method clearAll = statusBarClass.getMethod("collapsePanels");
			clearAll.setAccessible(true);
			clearAll.invoke(statusBarObject);
			
//			AppLog.d(ReflectUtils.printMethods(statusBarClass));; 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
		public void CloseNotify() {
			// TODO Auto-generated method stub
			int currentApiVersion = android.os.Build.VERSION.SDK_INT;
			try {
				Object service =mContext. getSystemService("statusbar");
				Class<?> statusbarManager = Class
						.forName("android.app.StatusBarManager");
				Method collapsePanels = null;
				if (service != null) {
					if (currentApiVersion <= 16) {
						collapsePanels = statusbarManager.getMethod("expand");
					} else {
						collapsePanels = statusbarManager
								.getMethod("collapsePanels");
					}
					collapsePanels.setAccessible(true);
					collapsePanels.invoke(service);
				}

			} catch (Exception e) {
			}

		}
	/** 
     * 判断当前界面是否是桌面 
     */ 
    public boolean isHome(){ 
        ActivityManager mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        if(mHomeList != null && mHomeList.size() > 0){
            return mHomeList.contains(rti.get(0).topActivity.getPackageName());
        }else{
            return false;
        }
    }
	public void setWendu(String wendu) {

		setText(wendu+"°");
	}
	@Override
	public void computeScroll() {
		super.computeScroll();
		boolean sx = mZidooScrollerX.computeOffset();
		boolean sy = mZidooScrollerY.computeOffset();
		
		if(sx || sy){
			invalidate();
		}
	}
	PointF mTouchPointF = new PointF();
	private float mOffsetX = 0;
	private float mOffsetY = 0;
	private boolean mNotityShowing;
	private boolean mRecentlyAppShowing;
	private boolean mCanClick = true; //是否可点击
	private boolean mCanLongClick = true;//是否可长按
	private boolean mCanLeft  = false;//是否可 左滑
	private boolean mCanRight  = false;//是否可 右滑
	private boolean mClickActive = false;//点击激活
	private boolean mLongClickActive = false;//长按激活
	private int mTouchCount = 0;
	private final int TOUCH_COUNT = 7;
	private PointF mGloablePointF = new PointF();
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Log.d("debug", "onTouchEvent = "+event.getAction() + ",,mLongClickActive = "+mLongClickActive+",," + "mCanLongClick = "+mCanLongClick + ",,mTOuchcCount = "+mTouchCount);
		changeAlpha();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mCanClick = true;
				mCanLongClick  = true;
				mCanLeft = true;
				mCanRight = true;
				mClickActive = false;
				mLongClickActive = false;
				mTouchCount = 0;
				mTouchPointF.x = event.getX();
				mTouchPointF.y = event.getY();
				
				mGloablePointF.x = event.getRawX();
				mGloablePointF.y = event.getRawY() - mStatusBarHeight ;
			break;
			case MotionEvent.ACTION_MOVE:
				
				if(mLongClickActive){
					mGloablePointF.x = event.getRawX();
					mGloablePointF.y = event.getRawY() - mStatusBarHeight ;
					mFrameWindowManager.updateViewPosition(mGloablePointF,mTouchPointF);
				}

				
				  mOffsetX = event.getX() - mTouchPointF.x;
				  mOffsetY = event.getY() - mTouchPointF.y;
				  if(Math.abs(mOffsetX) >  mCompareX || Math.abs(mOffsetY) > mCompareY ){
					  mCanClick = false;
					  mCanLongClick = false;
					  mHandler.removeMessages(TRT_LONG_CLICK_MESSAGE);
				  }else{
					  mTouchCount ++ ;
				  }
				  
				  if(/*mTouchCount > TOUCH_COUNT &&*/  mCanLongClick && !mLongClickActive){
//					  mLongClickActive = true;
//					  mCanClick = false;
					  mHandler.sendEmptyMessageDelayed(TRT_LONG_CLICK_MESSAGE, 400);
				  }
//				mTouchPointF.x = event.getX();
//				mTouchPointF.y = event.getY();
			    if(mLongClickActive){
			    	mOffsetX = 0;
			    	mOffsetY = 0;
				} 
			    {
					float holdX = mOffsetX;
					float holdY = mOffsetY;
					
					if(Math.abs(holdX) > Math.abs(holdY) ){
						if(mOffsetX > mCompareX ){	
							mOffsetX = mCompareX;
						}else if(mOffsetX < -mCompareX){
							mOffsetX = -mCompareX;
						}
						mOffsetY = 0;
					}else{
						if(mOffsetY > mCompareX ){	
							mOffsetY = mCompareX;
						}else if(mOffsetY < -mCompareX){
							mOffsetY = -mCompareX;
						}
						mOffsetX = 0;
					}
					mZidooScrollerX.setCurrentIndex(mOffsetX);
					mZidooScrollerY.setCurrentIndex(mOffsetY);
					
					invalidate();
					
					if(mCanLeft && mOffsetX >= mCompareX*.2f){
						mCanLeft = false;
						goPreTask(mContext);
					}else if(mCanRight && mOffsetX <= -mCompareX*.2f){
						mCanRight = false;
						goNextTask(mContext);
					} 
				}

			break;
			
			case MotionEvent.ACTION_UP:
				if(!mLongClickActive && mCanClick /*&& mTouchCount < TOUCH_COUNT*/){
					mClickActive = true;
					goClick();
				}
				mHandler.removeMessages(TRT_LONG_CLICK_MESSAGE);
				mTouchPointF.x = event.getX();
				mTouchPointF.y = event.getY();
				mZidooScrollerX.scrollToTargetIndex(0, 600);
				mZidooScrollerY.scrollToTargetIndex(0, 600);
				postInvalidate();
				  if(mOffsetY >= mCompareY){
						goTopDown();
					}else if(mOffsetY <= - mCompareY){
						goBottomUp();
					}
			break;
			case MotionEvent.ACTION_CANCEL:
			break;

			default:
			break;
		}
		return true;
	}
	private final static int			ALPHATIME			= 3 * 1000;
	private final static int			ALPHA				= 0;
	private final static int			CAN_PLAY_VOICE				= 1;
	private final static int			TRT_LONG_CLICK_MESSAGE				=2;
	private boolean mCanPlayVoice = true;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ALPHA:
				setAlpha(0.75f);
			break;
			case CAN_PLAY_VOICE:
				mCanPlayVoice = true;
			break;
			case TRT_LONG_CLICK_MESSAGE:
				mLongClickActive = true;
				AppLog.d("goLongClick");
				Vibrate(mContext,50);
//				ObjectAnimator.ofFloat(VisualizerView.this, "scaleX", 1f,1.2f,1f);
//				ObjectAnimator.ofFloat(VisualizerView.this, "scaleY", 1f,1.2f,1f);
			break;
			default:
			break;
			}
		}
	};
	
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
	public static void Vibrate(final Context context ,long[]pattern,boolean isRepeat)
	{
		Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, isRepeat ? 1:-1);
	}
	private void changeAlpha() {
		setAlpha(1f);
		mHandler.removeMessages(ALPHA);
		mHandler.sendEmptyMessageDelayed(ALPHA, ALPHATIME);
	}

	
	private void goClick() {
		AppLog.d("goClick");
		if(mCanPlayVoice){
			mCanPlayVoice = false;
//			Toast.makeText(context, "   Hello   光能蜗牛  ", Toast.LENGTH_SHORT).show();
			int index = (int) (Math.random() * 6);
			mHandler.removeMessages(CAN_PLAY_VOICE);
			mHandler.sendEmptyMessageDelayed(CAN_PLAY_VOICE, 2000);
			switch (index) {
				case 0:

					VoiceTool.getInstance(mContext).playVoice("101");
				break;
				case 1:

					VoiceTool.getInstance(mContext).playVoice("102");

				break;
				case 2:
					VoiceTool.getInstance(mContext).playVoice("101");

				break;
				case 3:
					VoiceTool.getInstance(mContext).playVoice("102");
				break;
				case 4:
					VoiceTool.getInstance(mContext).playVoice("103");

				break;
				case 5:
					VoiceTool.getInstance(mContext).playVoice("104");
				break;

				default:
				break;
			}
			
			if(mPlayVoiceService.mBinder.getAccessibilityService() != null){
				   mPlayVoiceService.mBinder.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
			 }else{
//					mVisualizerView.resetTaskIndex();
				   Intent intent = new Intent(Intent.ACTION_MAIN);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			        intent.addCategory(Intent.CATEGORY_HOME);
			        mContext.startActivity(intent);
			 }
		}
	}

	private void goOnLongClick() {
		
	}

	private void goTopDown() {
		AppLog.d("goTopDown");
		if(mRecentlyAppShowing){
//			if(mPlayVoiceService.mBinder.getAccessibilityService() != null){
//				   mPlayVoiceService.mBinder.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//			 }
			closeRecentlyAppPanel();
			mRecentlyAppShowing = false;
		}else{
			OpenNotify();
			mNotityShowing = true;
		}
	}

	private void goBottomUp() {
		AppLog.d("goBottomUp");
		if(mNotityShowing ){
			CloseNotify();
			mNotityShowing = false;
		}else{
				CloseNotify();
				showRecentlyAppPanel();
				mRecentlyAppShowing = true;
		}
	}
	private int mIndex = 0;
	  List<RecentTag> list =  new ArrayList<VisualizerView.RecentTag>();
	public   boolean goNextTask(Context cxt ) {
		AppLog.d("goNextTask");
//		ActivityManager activityManager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
//		  List<RunningTaskInfo> list = activityManager.getRunningTasks(100);

		if(list.size() == 0){
			list =  getRecentTaskInfos();
		}
		  if(mIndex+ 1 >= list.size()){
			  mIndex = list.size() - 1;
			  return false;
		  }
		  mIndex++; 
		  RecentTag curRecentTag = list.get(mIndex);
		  
		  boolean contain = false;
		  List<RecentTag> templist =   getRecentTaskInfos();
		  for (int i = 0; i < templist.size(); i++) {
			  RecentTag rt = templist.get(i);
			  if(rt. intent.getComponent().getPackageName().equals(curRecentTag.intent.getComponent().getPackageName())){
				  contain  = true;
				   Intent intent = new Intent();
				   intent.setClass(mContext,  TranslucentActivity.class);
				   intent.putExtra("hold_packagename", curRecentTag.intent.getComponent().getPackageName());
				   intent.putExtra("direction", "next");
				   intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP );
				   mContext.startActivity(intent);
				  break;
			  }
		  }
		  if( templist.size() != list.size() ||  contain == false){
			  list = templist;
			  mIndex = -1;
			  goNextTask(mContext);
		  }
		
		return true;
	}

	private boolean jump(RecentTag tag) {
		 AppLog.d("tag id - "+tag.info.id);
			
		 tag.intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                 | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
         try {
             getContext().startActivity(tag.intent);
         } catch (ActivityNotFoundException e) {
             Log.w("Recent", "Unable to launch recent task", e);
             return false;
         }

//		  Intent intent = new Intent();
//		  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		  intent.setAction(Intent.ACTION_MAIN);
//		  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP );
//		  intent.setComponent(tag.intent.getComponent());
//		  getContext().startActivity(intent);
//		 try {
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return false;
//		}
		return true;

	}
	private void jump(ComponentName topActivity) {
		 try {
			  
			  Intent intent = new Intent();
//			  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			  intent.setAction(Intent.ACTION_MAIN);
			  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP );
			  intent.setComponent(topActivity);
			  mContext.startActivity(intent);

			 mLastComponentActivity = topActivity;
			 
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	private ComponentName  mLastComponentActivity;
	public   boolean goPreTask(Context cxt ) {

		AppLog.d("goPreTask");
		

		if(list.size() == 0){
			list =  getRecentTaskInfos();
		}
		  if(mIndex- 1 <  0){
			  mIndex = 0;
			  return false;
		  }
		  mIndex--;
		  
		  RecentTag curRecentTag = list.get(mIndex);
		  boolean contain = false;
		  List<RecentTag> templist =   getRecentTaskInfos();
		  for (int i = 0; i < templist.size(); i++) {
			  RecentTag rt = templist.get(i);
			  if(rt. intent.getComponent().getPackageName().equals(curRecentTag.intent.getComponent().getPackageName())){
				  contain  = true;
				   Intent intent = new Intent();
				   intent.setClass(mContext,  TranslucentActivity.class);
				   intent.putExtra("hold_packagename", curRecentTag.intent.getComponent().getPackageName());
				   intent.putExtra("direction", "pre");
				   intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP );
				   mContext.startActivity(intent);
				  break;
			  }
		  }
		  if( templist.size() != list.size() || contain == false){
			  list = templist;
			  mIndex = list.size();
			  goPreTask(mContext);
		  }
		  
		return true;
	}

	
	private static final int	MAX_RECENT_TASKS	= 100;
	private List<RecentTag> getRecentTaskInfos() {

        final Context context = mContext;
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
                
        //拿到最近使用的应用的信息列表
        final List<ActivityManager.RecentTaskInfo> recentTasks =
                am.getRecentTasks(MAX_RECENT_TASKS, ActivityManager.RECENT_IGNORE_UNAVAILABLE);

        //自制一个home activity info，用来区分
//        ActivityInfo homeInfo = 
//            new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
//                    .resolveActivityInfo(pm, 0);

        //拿到最近使用的应用的信息列表
        final List<RecentTag> recentTags = new ArrayList<VisualizerView.RecentTag>();

        int index = 0;
        int numTasks = recentTasks.size();
        //开始初始化每个任务的信息
        for (int i = 0; i < numTasks  ; ++i) {
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            //复制一个任务的原始Intent
            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            //跳过home activity
//          Set<String> cee = intent.getCategories();
//          if(cee != null){
//        	  for (String s:cee) {
//        		  
//        		  AppLog.d("cate = " + s);
//        	  }
//          }
//            if (homeInfo != null) {
//                if (homeInfo.packageName.equals(
//                        intent.getComponent().getPackageName())
//                        && homeInfo.name.equals(
//                                intent.getComponent().getClassName())) {
//                    continue;
//                }
//            }
            boolean isHomeActivity = false;
            for (int j = 0; j < mHomeList.size(); j++) {
              if (mHomeList.get(j).equals(intent.getComponent().getPackageName())) {
            	  isHomeActivity = true;
                  break;
              }
			}
            if(isHomeActivity){
            	continue;
            }
            intent.setFlags((intent.getFlags()&~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final String title = activityInfo.loadLabel(pm).toString();
                Drawable icon = activityInfo.loadIcon(pm);

                if (title != null && title.length() > 0 && icon != null) {
                    //new一个Tag，保存这个任务的RecentTaskInfo和Intent
                    RecentTag tag = new RecentTag();
                    tag.info = info;
                    tag.intent = intent;
                    ++index;
                    recentTags.add(tag);
                }
            }
        }
        return recentTags;

    }
	class RecentTag{

		public RecentTaskInfo	info;
		public Intent	intent;
		
	}
	private boolean switchTo(RecentTag tag) {
		AppLog.d("tag id - "+tag.info.id);
        if (tag.info.id >= 0) {
            // 这个Task没有退出，直接移动到前台
            final ActivityManager am = (ActivityManager)
                    getContext().getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(tag.info.id, ActivityManager.MOVE_TASK_WITH_HOME);

        } else if (tag.intent != null) {
            //task退出了的话，id为-1，则使用RecentTag中的Intent重新启动
            tag.intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                    | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            try {
                getContext().startActivity(tag.intent);
            } catch (ActivityNotFoundException e) {
                Log.w("Recent", "Unable to launch recent task", e);
                return false;
            }
        }
        return true;
    }
	class Vector {

		private float vX;
		private float vY;

		public Vector(float startR, float endR, float angel) {
			float endX = endR * (float) Math.cos(angel);
			float endY = endR * (float) Math.sin(angel);
			
			float startX = startR * (float) Math.cos(angel);
			float  startY = startR * (float) Math.sin(angel);
			
			this.vX = endX - startX;
			this.vY = endY - startY;
		}
		
	}
	class Line{
		float mStartAngle = 0;
		float mEndAngle = 0;
		private float mEndR;
		private float mStartR;
		private Path mPath;
		private Vector mVector;
		public Line(float startAngle, float endAngle, float startR, float endR, Vector vector) {
			this.mStartAngle = startAngle;
			this.mEndAngle = endAngle;
			this.mStartR = startR;
			this.mEndR = endR;
			this.mVector = vector;
			mPath = new Path();
		}
		public void draw(Canvas canvas, float d) { 
			
			float line1_x = mCenterX+(float) (mStartR * Math.cos(mStartAngle));
			float line1_y = mCenterY+(float) (mStartR * Math.sin(mStartAngle));
			
			float line1_far_x = line1_x + mVector.vX * d;
			float line1_far_y =  line1_y+mVector.vY * d;

			float line2_x =mCenterX+ (float) (mStartR * Math.cos(mEndAngle));
			float line2_y = mCenterY+ (float) (mStartR * Math.sin(mEndAngle));
			
			float line2_far_x = line2_x+ mVector.vX * d;
			float line2_far_y = line2_y+mVector.vY * d;
			
			mPath.reset();
			mPath.moveTo(line1_x, line1_y);
			mPath.lineTo(line1_far_x, line1_far_y);
			mPath.lineTo(line2_far_x, line2_far_y);
			mPath.lineTo(line2_x, line2_y);
			canvas.drawPath(mPath, mPaint);
			
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		mPaint.setColor(mBackColor);
		canvas.drawCircle(mCenterX, mCenterY, mCenterX*mBackR, mPaint);
		
		if(mNormalShow){
			
			
//			mPaint.setStyle(Style.STROKE);
			drawCenterCircle(canvas);
//			mPaint.setStyle(Style.FILL);
//			canvas.drawArc(new RectF(mCenterX-mCenterX*mR, mCenterY-mCenterY*mR,mCenterX+mCenterX*mR, mCenterY+mCenterY*mR), 0, 360, false, mPaint);
			super.onDraw(canvas);
			
		}else{

			drawCenterCircle(canvas);
//			super.onDraw(canvas);
			ArrayList<Float> list = new ArrayList<Float>();
			for (int i = 0; i < mModel.length; i++) {
				list.add(mModel[i]*1f/(mMax));
			}

			for (int i = 0; i < mArrayList.size()/2; i++) {
				list.add((float) Math.random());
			}
			for (int i = 0; i < mArrayList.size()/2; i++) {
				Line line = mArrayList.get(i);
				line.draw(canvas,list.get(i));
			}
			int index = 0 ;
			for (int i = mArrayList.size() - 1; i >= mArrayList.size()/2; i--) {

				Line line = mArrayList.get(i);
				line.draw(canvas,list.get(index));
				index++;
			}
		}
	
	}

	Path mCenterPath = new Path();
	float t30 = (float)Math.tan(Math.PI*2*30/360f);
	private void drawCenterCircle(Canvas canvas) {

		float offsetX = mZidooScrollerX.getCurrentPara();
		float offsetY = mZidooScrollerY.getCurrentPara();
		float scale = (mWidth - Math.abs(offsetX+offsetY))*1f/mWidth;
		
//		mCenterPath.reset();
//		PointF pTop 		 		= new PointF(offsetX+mCenterX  ,										 mCenterY -scale* mCenterY * mR);
//		PointF p_lt1_Control = new PointF(offsetX+mCenterX - mCenterX * mR * t30, mCenterY -scale* mCenterY * mR);
//		PointF p_lt2_Control = new PointF(offsetX+mCenterX - mCenterX * mR, 		 mCenterY -scale* mCenterY * mR * t30);
//		PointF pLeft 		 		= new PointF(offsetX+mCenterX - mCenterX * mR, 		 mCenterY);
//		PointF p_lb1_Control = new PointF(offsetX+mCenterX - mCenterX * mR, 		 mCenterY +scale* mCenterY * mR * t30);
//		PointF p_lb2_Control = new PointF(offsetX+mCenterX - mCenterX * mR * t30, mCenterY +scale* mCenterY * mR);
//		PointF pBottom 	     = new PointF(offsetX+mCenterX, 						 					mCenterY +scale* mCenterY * mR);
//		PointF p_rb1_Control = new PointF(offsetX+mCenterX + mCenterX * mR * t30, mCenterY +scale* mCenterY * mR);
//		PointF p_rb2_Control = new PointF(offsetX+mCenterX + mCenterX * mR, 		   mCenterY +scale* mCenterY * mR * t30);
//		PointF pRight 		 	  = new PointF(offsetX+mCenterX + mCenterX * mR, 		  mCenterY);
//		PointF p_rt1_Control   = new PointF(offsetX+mCenterX + mCenterX * mR, 		    mCenterY -scale* mCenterY * mR * t30);
//		PointF p_rt2_Control   = new PointF(offsetX+mCenterX + mCenterX * mR * t30, mCenterY -scale* mCenterY * mR);
//		mCenterPath.moveTo(pTop.x, pTop.y);

//		mCenterPath.cubicTo(p_lt1_Control.x, p_lt1_Control.y, p_lt2_Control.x, p_lt2_Control.y, pLeft.x, pLeft.y);
//		mCenterPath.cubicTo(p_lb1_Control.x, p_lb1_Control.y, p_lb2_Control.x, p_lb2_Control.y, pBottom.x, pBottom.y);
//		mCenterPath.cubicTo(p_rb1_Control.x, p_rb1_Control.y, p_rb2_Control.x, p_rb2_Control.y, pRight.x, pRight.y);
//		mCenterPath.cubicTo(p_rt1_Control.x, p_rt1_Control.y, p_rt2_Control.x, p_rt2_Control.y, pTop.x, pTop.y);
		mPaint.setColor(mMainColor);
//		canvas.drawPath(mCenterPath, mPaint);
		canvas.drawCircle(mCenterX+offsetX, mCenterY+offsetY, mCenterX*mR*scale, mPaint);		
	}

	private PlayVoiceService	mPlayVoiceService;
	private FrameWindowManager mFrameWindowManager;


	public void resetTaskIndex() {
//		mIndex = 0;
//		list =  getRecentTaskInfos();
	}

	public void setService(PlayVoiceService service, FrameWindowManager f) {
		this.mPlayVoiceService = service;
		this.mFrameWindowManager = f;
	}



}
