package com.wz.mobilemedia.presenter;

import com.wz.mobilemedia.bean.BaseMoive;
import com.wz.mobilemedia.bean.MoiveInfo;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.contract.NetVideoContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wz on 17-5-22.
 */

public class NetVideoPresenter extends BasePresenter<MediaInfoModel, NetVideoContract.View> {


    public NetVideoPresenter(MediaInfoModel mediaInfoModel, NetVideoContract.View view) {
        super(mediaInfoModel, view);
    }

    public void requestData(){
       mModel.getNetMoive().map(new Function<BaseMoive, List<MoiveInfo>>() {
           @Override
           public List<MoiveInfo> apply(BaseMoive baseMoive) throws Exception {
               if (baseMoive!=null){

                   return baseMoive.getTrailers();
               }

               return null;
           }
       }).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<List<MoiveInfo>>() {
                   @Override
                   public void onSubscribe(Disposable d) {
                       d.dispose();
                   }

                   @Override
                   public void onNext(List<MoiveInfo> value) {
                       if (value!=null){

                           mView.showResult(value);
                       }
                   }

                   @Override
                   public void onError(Throwable e) {
                    mView.showError();
                   }

                   @Override
                   public void onComplete() {

                   }
               });
    }


}
