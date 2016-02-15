package com.lightsnail.specturm;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("NewApi") public class VisualizerView extends View{

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
	private int mColorWhite;
	private int mColorBlack;
	
	public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VisualizerView(Context context) {
		super(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(mInit == false){

			mInit = true;
			this.mWidth = getWidth();
			this.mHeight = getHeight();;
			this.mCenterX = mWidth/2;
			this.mCenterY = mHeight/2;
			this.mPaint= new Paint();
			this.mPaint.setColor(Color.WHITE);
			this.mPaint.setAntiAlias(true);
			this.mColorWhite =Color.parseColor("#eeffff00");
			this.mColorBlack = Color.parseColor("#66000000");
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
		            	}
		            	if(mStaticCount > 1000){
		            		mStaticCount = 10;
		            	}
						
		            }else{
		            	mStaticCount = 0;
		            	invalidate();
		            }
//					mIEffect.draw(mModel,mMax);
				}
			}, Visualizer.getMaxCaptureRate(), false, true);
			mVisualizer.setCaptureSize(mCaptureSize);
			mVisualizer.setEnabled(true);
		}
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
		super.onDraw(canvas);

		mPaint.setColor(mColorBlack);
		canvas.drawCircle(mCenterX, mCenterY, mCenterX*mBackR, mPaint);
		mPaint.setColor(mColorWhite);
		canvas.drawCircle(mCenterX, mCenterY, mCenterX*mR, mPaint);

		ArrayList<Float> list = new ArrayList<Float>();
		for (int i = 0; i < mModel.length; i++) {
			list.add(mModel[i]*1f/(mMax));
		}

//		for (int i = 0; i < mArrayList.size()/2; i++) {
//			list.add((float) Math.random());
//		}
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
//		removeCallbacks(mRunable);
//		postDelayed(mRunable, 50);
	}

	Runnable mRunable = new Runnable() {
		
		@Override
		public void run() {
			invalidate();
		}
	};

}
