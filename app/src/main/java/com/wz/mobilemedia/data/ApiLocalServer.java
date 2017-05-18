package com.wz.mobilemedia.data;

import android.content.Context;

import com.wz.mobilemedia.util.StorageListUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wz on 17-5-18.
 */

public class ApiLocalServer {

    public static Observable<List<String>> getLocalVideo(final Context context) {


        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                StorageListUtil storageListUtil = new StorageListUtil();
                List<String> pathString =storageListUtil.scannerMedia(context, StorageListUtil.VIDEO_EXTENSIONS);
                e.onNext(pathString);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());


    }


    public static Observable<List<String>> getLocalMusic(final Context context){


        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                StorageListUtil storageListUtil = new StorageListUtil();
                List<String> pathString = storageListUtil.scannerMedia(context,StorageListUtil.MUSIC_EXTENSIONS);
                e.onNext(pathString);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());


}

}
