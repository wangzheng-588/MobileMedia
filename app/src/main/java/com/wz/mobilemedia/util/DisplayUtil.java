package com.wz.mobilemedia.util;

import android.util.TypedValue;

import com.wz.mobilemedia.MediaApplication;

/**
 * Created by wz on 17-5-19.
 */

public class DisplayUtil {


    public static int dp2px( float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, MediaApplication.getContext().getResources().getDisplayMetrics());
    }

    public static int sp2px( float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, MediaApplication.getContext().getResources().getDisplayMetrics());
    }


    public static float px2dp( float pxVal)
    {
        final float scale = MediaApplication.getContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }


    public static float px2sp( float pxVal)
    {
        return (pxVal / MediaApplication.getContext().getResources().getDisplayMetrics().scaledDensity);
    }
}
