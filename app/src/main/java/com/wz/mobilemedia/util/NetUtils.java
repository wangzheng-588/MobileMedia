package com.wz.mobilemedia.util;

import android.content.Context;
import android.net.TrafficStats;

/**
 * Created by wz on 17-5-22.
 */

public class NetUtils {

    public static boolean isNetUri(String str){
        boolean isNetUri = false;
        if (str.toLowerCase().startsWith("http")||str.toLowerCase().startsWith("mms")||str.toLowerCase().startsWith("rtps")){
            isNetUri = true;
        }

        return isNetUri;

    }

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public String showNetSpeed(Context context) {

        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;

        return  String.valueOf(speed) + " kb/s";


    }

}
