package com.wz.mobilemedia.data;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wz on 17-5-18.
 */

public class MediaInfoModel {

    public Observable<List<String>> getLocalVideo(Context context){
        return ApiLocalServer.getLocalVideo(context);
    }

}
