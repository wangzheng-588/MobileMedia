package com.wz.mobilemedia.presenter;

import android.content.Context;
import android.util.Log;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.util.MediaInformationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        //mView.showProgress();
        Observable<List<MediaInfoBean>> map=null;
        if (type==VIDEO_TYPE){
             mModel.getLocalVideo(context).map(new Function<List<String>, List<MediaInfoBean>>() {
                @Override
                public List<MediaInfoBean> apply(List<String> strings) throws Exception {
                    List<MediaInfoBean> list = new ArrayList<>();
                    MediaInformationUtils mediaInformationUtils = new MediaInformationUtils();
                    for (int i = 0; i < strings.size(); i++) {
                        try {
                            MediaInfoBean videoInfoBean = mediaInformationUtils.getMediaInfomation(strings.get(i));
                            if (videoInfoBean!=null){

                                videoInfoBean.setType(Contract.VIDEO_TYPE);
                                list.add(videoInfoBean);
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    Log.e("TAG",list.size()+":");
                    return list;
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Consumer<List<MediaInfoBean>>() {
                 @Override
                 public void accept(List<MediaInfoBean> mediaInfoBeen) throws Exception {
                     if (mediaInfoBeen!=null&&mediaInfoBeen.size()>0){
                         mView.showResult(mediaInfoBeen);
                     } else {
                         mView.showEmpty();
                     }

                     mView.dismissProgress();
                     mView.onRefreshFinish();
                 }
             });
        } else if (type==MUSIC_TYPE){
           mModel.getLocalMusic(context).map(new Function<List<String>, List<MediaInfoBean>>() {
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
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<List<MediaInfoBean>>() {
              @Override
              public void accept(List<MediaInfoBean> mediaInfoBeen) throws Exception {
                  if (mediaInfoBeen!=null&&mediaInfoBeen.size()>0){
                      mView.showResult(mediaInfoBeen);
                  } else {
                      mView.showEmpty();
                  }

                  //mView.dismissProgress();
                  mView.onRefreshFinish();
              }
          });
        }


    }
}
