package com.wz.mobilemedia.presenter;

import android.content.Context;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.util.MediaInformationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.wz.mobilemedia.common.Contract.MUSIC_TYPE;
import static com.wz.mobilemedia.common.Contract.VIDEO_TYPE;

/**
 * Created by wz on 17-5-18.
 */

public class MediaPresenter extends BasePresenter<MediaInfoModel, MediaInfoContract.View> {


    private Disposable mDisposable;

    public MediaPresenter(MediaInfoModel mediaInfoModel, MediaInfoContract.View view) {
        super(mediaInfoModel, view);
    }

    public void requestData(Context context,int type) {
        mView.showProgress();
        Observable<List<MediaInfoBean>> map=null;
        if (type==VIDEO_TYPE){
            map = mModel.getLocalVideo(context).map(new Function<List<String>, List<MediaInfoBean>>() {
                @Override
                public List<MediaInfoBean> apply(List<String> strings) throws Exception {
                    List<MediaInfoBean> list = new ArrayList<>();
                    MediaInformationUtils mediaInformationUtils = new MediaInformationUtils();
                    for (int i = 0; i < strings.size(); i++) {
                        MediaInfoBean videoInfoBean = mediaInformationUtils.getMediaInfomation(strings.get(i));
                        videoInfoBean.setType(Contract.VIDEO_TYPE);
                        list.add(videoInfoBean);
                    }
                    return list;
                }
            });
        } else if (type==MUSIC_TYPE){
          map =  mModel.getLocalMusic(context).map(new Function<List<String>, List<MediaInfoBean>>() {
                @Override
                public List<MediaInfoBean> apply(List<String> strings) throws Exception {
                    List<MediaInfoBean> list = new ArrayList<>();
                    MediaInformationUtils mediaInformationUtils = new MediaInformationUtils();
                    for (int i = 0; i < strings.size(); i++) {
                        MediaInfoBean musicInfoBean =  mediaInformationUtils.getMediaInfomation(strings.get(i));
                        musicInfoBean.setType(Contract.MUSIC_TYPE);
                        list.add(musicInfoBean);
                    }
                    return list;
                }
            });
        }

    map.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<MediaInfoBean>>() {
                @Override
                public void onSubscribe(Disposable d) {

                    mView.dismissProgress();
                    mDisposable = d;
                }

                @Override
                public void onNext(List<MediaInfoBean> mediaInfoBeen) {

                    if (mediaInfoBeen!=null){
                        mView.showResult(mediaInfoBeen);
                    } else {
                        mView.showEmpty();
                    }

                    mDisposable.dispose();
                    mView.dismissProgress();
                }

                @Override
                public void onError(Throwable e) {
                    mView.showError();
                }

                @Override
                public void onComplete() {
                    mView.dismissProgress();
                }
            });

    }
}
