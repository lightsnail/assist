package com.lightsnail.specturm.effect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.SurfaceHolder;

public abstract class  Effect {

	protected int	mWidth;
	protected int	mHeight;
	
	protected int	mBaseX;
	protected int	mBaseY;
	protected int	mBlockHeight;

	protected SurfaceHolder	mSurfaceHolder;
	protected int	mModelSize;


	protected Path mPath = new Path();
	protected Paint mPaint = new Paint();
	
	private Matrix mMatrix = new Matrix();
	protected Bitmap mFGBitmap  ;
	protected Bitmap mBGBitmap  ;

	public abstract void factDraw(Canvas canvas ,float[] mModel, float mMax);
	
	public  void draw(float[] mModel, float mMax){
		Canvas canvas = mSurfaceHolder.lockCanvas(null);
		canvas.drawColor(Color.argb(110, 0, 255, 255),android.graphics.PorterDuff.Mode.SRC);
//		canvas.drawColor(Color.argb(110, 0, 255, 255));
		
		
		factDraw(canvas ,mModel,mMax);
		
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	};

	public Effect(int width, int height,int modelSize,SurfaceHolder surfaceHolder) {
		 mWidth = width;
		 mHeight = height;
		 mSurfaceHolder = surfaceHolder;
		 
		 
		 mModelSize = modelSize;
		 mBaseX = (int) (mWidth / (modelSize *2f));
		 mBaseY = (int) (mHeight - mBaseX * 2);
//		 mBlockHeight = (int) (0.01f*mHeight);
		 mBlockHeight = 0;

		 mPaint.setColor(Color.WHITE);
		 mPaint.setStyle(Style.STROKE);

//		 mFGBitmap = Bitmap.createBitmap(mWidth/20, height, Config.ARGB_8888);
//		 mBGBitmap = Bitmap.createBitmap(mWidth/20, height, Config.ARGB_8888);
//		 mPaint.setShader(new LinearGradient(0,  0, mWidth,0, Color.WHITE, Color.BLACK, TileMode.CLAMP));
		 //		 mPaint.setAntiAlias(true);
	}

}
