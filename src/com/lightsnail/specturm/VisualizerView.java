package com.lightsnail.specturm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lightsnail.utils.AppLog;
import com.lightsnail.weatherclock.MyAccessibilityService;
import com.lightsnail.weatherclock.PlayVoiceService;
import com.zidoo.custom.sound.ZidooHomeKeyTool;
import com.zidoo.custom.sound.ZidooHomeKeyTool.HomeKeyListener;

import android.R.color;
import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi") public class VisualizerView extends TextView {

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
	private Visualizer mVisualizer;
	private int mStaticCount = 0;
	
	private float mMax = (float) ((float) Math.log(127) );
	private int mMainColor;
//	private int mMainColorHalf;
	
	private int mBackColor;
	private int	mTextColor;
	private boolean	mNormalShow;
	private ZidooScroller mZidooScroller;
	private Context	mContext;
	private List<String>	mHomeList;
	
	public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public VisualizerView(Context context) {
		super(context);
		this.mContext = context;
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
			this.mCenterX = mWidth/2;
			this.mCenterY = mHeight/2;
			this.mPaint= new Paint();
			this.mPaint.setColor(Color.WHITE);
			this.mPaint.setAntiAlias(true);
			int alpha = 238;
			int red = 0;
			int green = 255;
			int blue = 60;

			
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


				@Override
				public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
					// TODO Auto-generated method stub
		            Log.d("debug", "onWaveFormDataCapture ");
				}
				
				@Override
				public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

//					String string = "";
					int index = 0;
		            for (int i = 2; i < fft.length;i+=2) {  
		            	mModel[index] = (float) Math.hypot(fft[i], fft[i+1]) ; 
		            	mModel[index] = (float) Math.log(mModel[index]+1);
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
			mZidooScroller = new ZidooScroller(mContext, 0, 1, 
					 new BounceInterpolator()  
					//new OvershootInterpolator ()
			);
			setTextSize(10);
			setTextColor(mTextColor);
			setGravity(Gravity.CENTER);

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
		if(mZidooScroller.computeOffset()){
			invalidate();
		}
	}
	PointF mTouchPointF = new PointF();
	private float mHuaOffset = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchPointF.x = event.getX();
				mTouchPointF.y = event.getY();
			break;
			case MotionEvent.ACTION_MOVE:
				float mOffsetX = event.getX() - mTouchPointF.x;
				float mOffsetY = event.getY() - mTouchPointF.y;
				mTouchPointF.x = event.getX();
				mTouchPointF.y = event.getY();
			    if(mHasLongClicked){
					
				}else {
					if(mZidooScroller.getCurrentPara() + (int) mOffsetX/2 > mWidth/5 ){	
						mZidooScroller.setCurrentIndex(mWidth/5);
						invalidate();
					}else if( mZidooScroller.getCurrentPara()  + (int) mOffsetX/2 < -mWidth/5){
						mZidooScroller.setCurrentIndex(-mWidth/5);
						invalidate();
					}else {

						mZidooScroller.dragBy((int) mOffsetX/2);
						invalidate();
					}
				}

				mHuaOffset =  mZidooScroller.getCurrentPara();
			break;
			case MotionEvent.ACTION_UP:
				mTouchPointF.x = event.getX();
				mTouchPointF.y = event.getY();
				mZidooScroller.scrollToTargetIndex(0, 600);
				postInvalidate();
				if(mHuaOffset >= mWidth/6){
					goNextTask(mContext);
				}else if(mHuaOffset <= -mWidth/6){
					goPreTask(mContext);
				}
				mHuaOffset = 0;
			break;

			default:
			break;
		}
		return super.onTouchEvent(event);
	}
	private int mIndex = 0;
	public   boolean goNextTask(Context cxt ) {
//		ActivityManager activityManager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
//		  List<RunningTaskInfo> list = activityManager.getRunningTasks(100);

		  List<RecentTag> list =  getRecentTaskInfos();
//		  mIndex++;
//		  mIndex %= list.size();
		  boolean isHome = isHome();
		  for (int i = 0; i < list.size(); i++) {
			  RecentTag l = list.get(i);
			  if(isHome){
					 if(i == 0){
						 AppLog.d(l.info.baseIntent.toString());
						 jump(l);
					   break;
					 }
			  }else{
					 if(i == 1){
					   AppLog.d(l.info.baseIntent.toString());
					   switchTo(l);
					   break;
					 }
			  }
		  }
		// Log.d("debug", "activityName[i] = "+activityName[0]);
		// Log.d("debug", "componentName = "+componentName.getClassName());
		
		return false;
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
         
//		 try {
//			  Intent intent = new Intent();
//			  intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			  intent.setAction(Intent.ACTION_MAIN);
//			  intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP );
//			  intent.setComponent(tag.intent.getComponent());
//			  mContext.startActivity(intent);
//		} catch (Exception e) {
//			// TODO: handle exception
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
		if(mPlayVoiceService.mBinder.getAccessibilityService() != null){
			mPlayVoiceService.mBinder.getAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
		}else{
			goNextTask(mContext);
			mIndex = 0;
		}
		return false;
	}

	private static final int	MAX_RECENT_TASKS	= 5;
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

//			for (int i = 0; i < mArrayList.size()/2; i++) {
//				list.add((float) Math.random());
//			}
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
//			removeCallbacks(mRunable);
//			postDelayed(mRunable, 50);
		}
	
	}

	Path mCenterPath = new Path();
	float t30 = (float)Math.tan(Math.PI*2*30/360f);
	private void drawCenterCircle(Canvas canvas) {

		float offset = mZidooScroller.getCurrentPara();
		float scale = (mWidth - Math.abs(offset))*1f/mWidth;
		
		mCenterPath.reset();
		PointF pTop 		 = new PointF(offset+mCenterX  ,					 mCenterY -scale* mCenterY * mR);
		PointF p_lt1_Control = new PointF(offset+mCenterX - mCenterX * mR * t30, mCenterY -scale* mCenterY * mR);
		PointF p_lt2_Control = new PointF(offset+mCenterX - mCenterX * mR, 		 mCenterY -scale* mCenterY * mR * t30);
		PointF pLeft 		 = new PointF(offset+mCenterX - mCenterX * mR, 		 mCenterY);
		PointF p_lb1_Control = new PointF(offset+mCenterX - mCenterX * mR, 		 mCenterY +scale* mCenterY * mR * t30);
		PointF p_lb2_Control = new PointF(offset+mCenterX - mCenterX * mR * t30, mCenterY +scale* mCenterY * mR);
		PointF pBottom 	     = new PointF(offset+mCenterX, 						 mCenterY +scale* mCenterY * mR);
		PointF p_rb1_Control = new PointF(offset+mCenterX + mCenterX * mR * t30, mCenterY +scale* mCenterY * mR);
		PointF p_rb2_Control = new PointF(offset+mCenterX + mCenterX * mR, 		 mCenterY +scale* mCenterY * mR * t30);
		PointF pRight 		 = new PointF(offset+mCenterX + mCenterX * mR, 		 mCenterY);
		PointF p_rt1_Control = new PointF(offset+mCenterX + mCenterX * mR, 		 mCenterY -scale* mCenterY * mR * t30);
		PointF p_rt2_Control = new PointF(offset+mCenterX + mCenterX * mR * t30, mCenterY -scale* mCenterY * mR);
		mCenterPath.moveTo(pTop.x, pTop.y);
//		mCenterPath.lineTo(p_lt1_Control.x, p_lt1_Control.y);
//		mCenterPath.lineTo(p_lt2_Control.x, p_lt2_Control.y);
//		mCenterPath.lineTo(pLeft.x, pLeft.y);

		mCenterPath.cubicTo(p_lt1_Control.x, p_lt1_Control.y, p_lt2_Control.x, p_lt2_Control.y, pLeft.x, pLeft.y);
		mCenterPath.cubicTo(p_lb1_Control.x, p_lb1_Control.y, p_lb2_Control.x, p_lb2_Control.y, pBottom.x, pBottom.y);
		mCenterPath.cubicTo(p_rb1_Control.x, p_rb1_Control.y, p_rb2_Control.x, p_rb2_Control.y, pRight.x, pRight.y);
		mCenterPath.cubicTo(p_rt1_Control.x, p_rt1_Control.y, p_rt2_Control.x, p_rt2_Control.y, pTop.x, pTop.y);
//		mCenterPath.rQuadTo(dx1, dy1, dx2, dy2);
		mPaint.setColor(mMainColor);
		canvas.drawPath(mCenterPath, mPaint);
//		canvas.drawCircle(mCenterX, mCenterY, mCenterX*mR, mPaint);		
	}

	private boolean	mHasLongClicked;
	private PlayVoiceService	mPlayVoiceService;
	public void setLongClickFlag(boolean fl) {
		mHasLongClicked = fl;
	}

	public boolean isLongClicked() {
		return mHasLongClicked;
	}

	public void resetTaskIndex() {
		mIndex = 0;
	}

	public void setService(PlayVoiceService service) {
		this.mPlayVoiceService = service;
	}



}
