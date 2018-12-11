package com.lightsnail.specturm;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ZidooScroller {
	private Scroller mScroller;
	private float 	mCurrentIndex;
	private float 	mSpace;
	private float	mCurrentPara;
	private float	mTargetIndex;
	private float	mTargetPara;
	private Interpolator mInterpolator = new Interpolator() {

//		float a = 1f/((float) Math.sin(Math.PI * 2/2.8f ));
		@Override
		public float getInterpolation(float input) {
			
//			float t = a*(float) Math.sin(Math.PI * 2/2.8f * input);
//			Log.d("debug", "input = "+input+",,"+ "t = "+t);
//			return t  ;
			return input;
		}
	}; 
	public  ZidooScroller(Context context){
		mScroller = new Scroller(context,mInterpolator);
	}
	public  ZidooScroller(Context context,Interpolator i){
		this.mInterpolator = i;
		mScroller = new Scroller(context,mInterpolator);
	}
	public  ZidooScroller(Context context,float index,float space){
		mScroller = new Scroller(context,mInterpolator);
		init(index,space);
	}
	public  ZidooScroller(Context context,float index,float space,Interpolator i){
		
		this.mInterpolator = i;
		mScroller = new Scroller(context,mInterpolator);
		init(index,space);
	}
	public void init(float index, float space){
		this.mCurrentIndex = index;
		this.mSpace = space;
		this.mCurrentPara = this.mCurrentIndex * this.mSpace;
		mScroller.setFinalX((int)mCurrentPara);
		mScroller.abortAnimation(); 
	}
	public void setCurrentIndex(float index){
		this.mCurrentIndex = index;
		this.mCurrentPara = this.mCurrentIndex * this.mSpace;
		mScroller.setFinalX((int)mCurrentPara);
		mScroller.abortAnimation(); 
	}
	public void setCurrentPara(float para){
		this.mCurrentPara = para;
		mScroller.setFinalX((int)mCurrentPara);
		mScroller.abortAnimation(); 
	}
	public float getCurrentPara(){
		this.mCurrentPara = mScroller.getCurrX();
//		Log.d("debug", "mCurrentPara  = "+mCurrentPara + ",,mSpace = "+mSpace);
		return this.mCurrentPara;
	}
	public float getCurrentIndex(){
		this.mCurrentPara = mScroller.getCurrX();
		this.mCurrentIndex = mCurrentPara/mSpace;
		return mCurrentIndex;
	}
	public void scrollToTargetIndex(float index,int duration){
		this.mTargetIndex = index;
		this.mTargetPara = this.mTargetIndex * this.mSpace;
		
		int dx = (int) (mTargetPara - mCurrentPara) ;
		mScroller.forceFinished(true);
//		mScroller.extendDuration(duration);
		mScroller.startScroll((int)mCurrentPara, 0,  dx, 0,duration);
	}
	public boolean computeOffset() {
		return mScroller.computeScrollOffset();
	}
	public float getTargetIndex() {
		return mTargetIndex;
	}
	public float getCurrVelocity(){
		return mScroller.getCurrVelocity();
	}
	public void dragBy( int dd) {

		mCurrentPara = mScroller.getCurrX() + dd;
		mScroller.setFinalX((int)mCurrentPara);
		mScroller.abortAnimation();
	}
	public void flingTo(int velocity, int minX,int maxX,int minY,int maxY) {
		mScroller.setFinalX((int)mCurrentPara);
		mScroller.abortAnimation();
		mScroller.fling(mScroller.getCurrX(), 0, velocity, 0, minX, maxX, minY, maxY);

	} 
}
