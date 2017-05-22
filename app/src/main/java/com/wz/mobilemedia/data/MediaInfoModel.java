package com.wz.mobilemedia.data;

import android.content.Context;

import com.wz.mobilemedia.bean.BaseMoive;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wz on 17-5-18.
 */

public class MediaInfoModel {

    private ApiNetService mApiNetService;

    public Observable<List<String>> getLocalVideo(Context context){
        return ApiLocalServer.getLocalVideo(context);
    }

    public Observable<List<String>> getLocalMusic(Context context){
        return ApiLocalServer.getLocalMusic(context);
    }

    public Observable<BaseMoive> getNetMoive(){
        return HttpModule.getsInstance().getApiNetService().getNetMoive();
    }

}
