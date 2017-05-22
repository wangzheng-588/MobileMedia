package com.wz.mobilemedia.util;

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

}
