package com.wz.mobilemedia.presenter;

import android.content.Context;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.util.MediaInformationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.wz.mobilemedia.common.Contract.MUSIC_TYPE;
import static com.wz.mobilemedia.common.Contract.VIDEO_TYPE;

/**
 * Created by wz on 17-5-18.
 */

public class MediaPresenter extends BasePresenter<MediaInfoModel, MediaInfoContract.View> {



    public MediaPresenter(MediaInfoModel mediaInfoModel, MediaInfoContract.View view) {
        super(mediaInfoModel, view);
    }

    public void requestData(Context context,int type) {

        Observable<List<MediaInfoBean>> map=null;
        if (type==VIDEO_TYPE){
            map = mModel.getLocalVideo(context).map(new Function<List<String>, List<MediaInfoBean>>() {
                @Override
                public List<MediaInfoBean> apply(List<String> strings) throws Exception {
                    List<MediaInfoBean> list = new ArrayList<>();
                    MediaInformationUtils mediaInformationUtils = new MediaInformationUtils();
                    for (int i = 0; i < strings.size(); i++) {
                        MediaInfoBean videoInfoBean = mediaInformationUtils.getMediaInfomation(strings.get(i));
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
                        list.add(musicInfoBean);
                    }
                    return list;
                }
            });
        }

    map.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<MediaInfoBean>>() {
                @Override
                public void accept(List<MediaInfoBean> mediaInfoBeen) throws Exception {
                    mView.showResult(mediaInfoBeen);
                }
            });

    }
}
