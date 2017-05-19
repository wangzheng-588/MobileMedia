package com.wz.mobilemedia;

import android.app.Application;
import android.content.Context;

/**
 * Created by wz on 17-5-19.
 */

public class MediaApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext (){
        return mContext;
    }
}
