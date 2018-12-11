package com.lightsnail.specturm.effect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.view.SurfaceHolder;

public class LineEffect extends Effect{

	Path[] mLinePath  ;
	public LineEffect(int width, int height, int modelSize, SurfaceHolder surfaceHolder) {
		super(width, height, modelSize, surfaceHolder);
		mLinePath = new Path[modelSize];
		for (int i = 0; i < modelSize; i++) {
			mLinePath[i] = new Path();
		}
	}

	@Override
	public void factDraw(Canvas canvas ,float[] model,float max) {
		
		
		mPath.reset();
		
		for (int i = 0; i < model.length; i++) {
			float xStart = (float) (mBaseX + i *1f/ model.length * mWidth);
			float yStart = (float) (mBaseY +  - mBlockHeight  ) ;
			
			float xEnd = (float) (mBaseX + i *1f/ model.length * mWidth);
			float yEnd = (float) (mBaseY +  - mBlockHeight -model[i]*1f/(max)*mHeight) ;

			mLinePath[i] .reset();
			
			mLinePath[i].moveTo(xStart, yStart);
			mLinePath[i].lineTo(xEnd, yEnd);
			
			mPath.addPath(mLinePath[i]);
		}

			
		canvas.drawPath(mPath, mPaint);
		
	}

}
