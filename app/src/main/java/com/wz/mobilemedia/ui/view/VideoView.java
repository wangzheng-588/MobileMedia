package com.wz.mobilemedia.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by wz on 17-5-21.
 */

public class VideoView extends android.widget.VideoView {
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    public void setVideoViewSize(int videoWidth, int videoHeight){
        ViewGroup.LayoutParams layoutParams  =getLayoutParams();
        layoutParams.width = videoWidth;
        layoutParams.height = videoHeight;
        setLayoutParams(layoutParams);

    }
}
