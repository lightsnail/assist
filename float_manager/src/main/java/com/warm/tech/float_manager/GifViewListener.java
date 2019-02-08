package com.warm.tech.float_manager;

import android.graphics.PointF;

/**
 * Created by LightSnail on 2019/2/7.
 */

public interface GifViewListener {
    void OnClick() ;
    void OnLongClick();
    void OnMove(PointF mGloablePointF, PointF mTouchPointF);
    void OnCancel();
}
