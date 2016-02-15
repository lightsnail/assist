package com.lightsnail.specturm.effect;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DefaultEffect extends Effect{

	public DefaultEffect(int width, int height, int modelSize, SurfaceHolder surfaceHolder) {
		super(width, height, modelSize, surfaceHolder);
	}

	@Override
	public void factDraw(Canvas canvas ,float[] model,float max) {
		
		mPath.reset();
		float xStart  = (float) (mBaseX );
		float yStart = (float) (  mBaseY  );
		
		mPath.moveTo(xStart, yStart);
		
		float x0 = (float) (mBaseX );
		float y0 = (float) (  mBaseY - mBlockHeight);
		
		mPath.lineTo(x0, y0);
		
		for (int i = 0; i < model.length; i++) {
			float x = (float) (mBaseX + i *1f/ model.length * mWidth);
			float y = (float) (mBaseY +  - mBlockHeight -model[i]*1f/(max)*mHeight) ;
			mPath.lineTo(x, y);
		}
		float x = (float) (mBaseX + (model.length - 1) *1f/ model.length * mWidth);
		float y = (float) (mBaseY - mBlockHeight) ;
		mPath.lineTo(x, y);

		float xEnd = (float) (mBaseX + (model.length - 1) *1f/ model.length * mWidth);
		float yEnd = (float) (mBaseY  ) ;
		mPath.lineTo(xEnd, yEnd);
		mPath.lineTo(xStart, yStart);
		
		canvas.drawPath(mPath, mPaint);
	}

	
}
