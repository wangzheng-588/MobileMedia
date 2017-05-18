package com.wz.mobilemedia.data;

import android.content.Context;

import com.wz.mobilemedia.util.StorageListUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by wz on 17-5-18.
 */

public class ApiLocalServer {

    public static Observable<List<String>> getLocalVideo(Context context){

        final List<String> pathString = StorageListUtil.scannerMedia(context);

        Observable<List<String>> listObservable = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(pathString);
            }
        });
        return listObservable;
    }

}
