package com.warm.tech.float_manager;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by LightSnail on 2019/2/7.
 */

public class GifViewTouch {
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

    private int mHeight;
    private int mWidth;
    private int mCenterX;
    private int mCenterY;
    private int mCompareX;
    private int mCompareY;
    private int mStatusBarHeight;//状态栏高度

    private final static int			ALPHATIME			= 3 * 1000;
    private final static int			LONG_CLICK_DELAY_TIME			= 300;
    private final static int			ALPHA				= 0;
    private final static int			CAN_PLAY_VOICE				= 1;
    private final static int			TRT_LONG_CLICK_MESSAGE				=2;


    GifViewListener mGifViewListener;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALPHA:
                   // setAlpha(0.75f);
                    break;
                case CAN_PLAY_VOICE:
                   // mCanPlayVoice = true;
                    break;
                case TRT_LONG_CLICK_MESSAGE:
                    mLongClickActive = true;
                    mGifViewListener.OnLongClick();
//				ObjectAnimator.ofFloat(VisualizerView.this, "scaleX", 1f,1.2f,1f);
//				ObjectAnimator.ofFloat(VisualizerView.this, "scaleY", 1f,1.2f,1f);
                    break;
                default:
                    break;
            }
        }
    };

    public GifViewTouch(Context context, GifViewListener l){
        mStatusBarHeight = getStatusHeight(context);
        mGifViewListener = l;
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

    public boolean onTouchEvent(MotionEvent event,View view) {
        this.mWidth = view.getWidth();
        this.mHeight = view.getHeight();;
        this.mCompareX = mWidth/5;
        this.mCompareY = mHeight/5;
        this.mCenterX = mWidth/2;
        this.mCenterY = mHeight/2;
       // Log.d("debug", "onTouchEvent = "+event.getAction() + ",,mLongClickActive = "+mLongClickActive+",," + "mCanLongClick = "+mCanLongClick + ",,mTOuchcCount = "+mTouchCount);
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
                    mGifViewListener.OnMove(mGloablePointF,mTouchPointF);
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
                    mHandler.sendEmptyMessageDelayed(TRT_LONG_CLICK_MESSAGE, LONG_CLICK_DELAY_TIME);
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
               // mZidooScrollerX.setCurrentIndex(mOffsetX);
               // mZidooScrollerY.setCurrentIndex(mOffsetY);

                view.invalidate();

                if(mCanLeft && mOffsetX >= mCompareX*.2f){
                    mCanLeft = false;
                   // goPreTask(mContext);
                }else if(mCanRight && mOffsetX <= -mCompareX*.2f){
                    mCanRight = false;
                   // goNextTask(mContext);
                }
            }

            break;

            case MotionEvent.ACTION_UP:
                if(!mLongClickActive && mCanClick /*&& mTouchCount < TOUCH_COUNT*/){
                    mClickActive = true;
                    mGifViewListener.OnClick();
                }
                mHandler.removeMessages(TRT_LONG_CLICK_MESSAGE);
                mTouchPointF.x = event.getX();
                mTouchPointF.y = event.getY();
               // mZidooScrollerX.scrollToTargetIndex(0, 600);
               // mZidooScrollerY.scrollToTargetIndex(0, 600);
                view.postInvalidate();
                if(mOffsetY >= mCompareY){
                    //goTopDown();
                }else if(mOffsetY <= - mCompareY){
                    //goBottomUp();
                }
                mGifViewListener.OnCancel();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                break;
        }
        return true;
    }
}
