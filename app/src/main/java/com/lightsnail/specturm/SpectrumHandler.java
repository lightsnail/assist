package com.lightsnail.specturm;

import android.annotation.SuppressLint;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.lightsnail.specturm.effect.DefaultEffect;
import com.lightsnail.specturm.effect.Effect;
import com.lightsnail.specturm.effect.LineEffect;

@SuppressLint("NewApi") public class SpectrumHandler extends Handler{

	
	public static final int	START		= 	0;
	public static final int	END			= 	1;
	public static final int	CONTINUE	= 	2;
	private Visualizer mVisualizer ;
	private SurfaceHolder	mSurfaceHolder; 
	private boolean	mVisible;
	private int mCaptureSize = Visualizer.getCaptureSizeRange()[0] ;
	private int mModelSize = mCaptureSize/2 - 1;
	private float[] mModel = new float[mModelSize] ;
	
	private Effect mIEffect = null;
	private int	mWidth;
	private int	mHeight;
	private int	mEffectIndex = -1;
	private int mEffectCount = 2;
	private float mMax = (float) ((float) /*Math.log*/(127) );
//	private Equalizer mEqualizer ; 
//	mEqualizer = new Equalizer(0, 0);  //equalizer����ȫ��output mix֧��
//	mEqualizer.setEnabled(true);  
	public SpectrumHandler() {
		
		
		mVisualizer = new Visualizer(0);
		mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {

			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
				// TODO Auto-generated method stub
	            Log.d("debug", "onWaveFormDataCapture ");
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

				String string = "";
	            for (int i = 2; i < fft.length;i+=2) {  
	            	mModel[i/2-1] = (float) Math.hypot(fft[i], fft[i+1]) ; 
//	            	mModel[i/2-1] = (float) /*Math.log*/(mModel[i/2-1]);
	                string += mModel[i/2-1]+"__";
	            }  	
	            Log.d("debug", "onFftDataCapture "+string);
//				Log.d("debug", "-------" );
				mIEffect.draw(mModel,mMax);
			}
		}, Visualizer.getMaxCaptureRate(), false, true);
		mVisualizer.setCaptureSize(mCaptureSize);
//		mVisualizer.setScalingMode(Visualizer.SCALING_MODE_AS_PLAYED);
	}

	public void onSurfaceChanged(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
		changeEffect();
	}
	public void changeEffect() {

		mEffectIndex ++;
		mEffectIndex %= mEffectCount;
		switch (mEffectIndex) {
		case 0:
			this.mIEffect = new DefaultEffect(mWidth,mHeight,mModelSize,mSurfaceHolder);
			break;
		case 1:
			this.mIEffect = new LineEffect(mWidth,mHeight,mModelSize,mSurfaceHolder);
		break;

		default:
			break;
		}
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
//		if(msg.what == START){
//			mHandler.sendEmptyMessage(CONTINUE);
//		}else if(msg.what == END){
//			mHandler.removeMessages(CONTINUE);
//			mHandler.removeMessages(START);
//		}else if(msg.what == CONTINUE){
//			mHandler.removeMessages(CONTINUE);
//			mHandler.sendEmptyMessageDelayed(CONTINUE, 1000);
////			draw();
//		}
	}

	public void onVisibilityChanged(boolean visible) {
		this.mVisible = visible;
		if(mVisible){
			mVisualizer.setEnabled(true);
		}else{
			mVisualizer.setEnabled(false);
		}
	}

}
