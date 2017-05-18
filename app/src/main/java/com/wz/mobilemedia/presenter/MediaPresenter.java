package com.wz.mobilemedia.presenter;

import android.content.Context;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.util.MediaInformationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wz on 17-5-18.
 */

public class MediaPresenter extends BasePresenter<MediaInfoModel, MediaInfoContract.View> {


    public MediaPresenter(MediaInfoModel mediaInfoModel, MediaInfoContract.View view) {
        super(mediaInfoModel, view);
    }

    public void requestData(Context context) {
        mModel.getLocalVideo(context)
                .map(new Function<List<String>, List<MediaInfoBean>>() {
                    @Override
                    public List<MediaInfoBean> apply(List<String> strings) throws Exception {
                        List<MediaInfoBean> list = new ArrayList<MediaInfoBean>();
                        for (int i = 0; i < strings.size(); i++) {
                            MediaInfoBean mediaInfomation = MediaInformationUtils.getMediaInfomation(strings.get(i));
                            list.add(mediaInfomation);
                        }
                        return list;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<MediaInfoBean>>() {
            @Override
            public void accept(List<MediaInfoBean> mediaInfoBeen) throws Exception {
                mView.showResult(mediaInfoBeen);
            }
        });
    }
}
