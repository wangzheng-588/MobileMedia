package com.wz.mobilemedia.ui.fragment;

import com.wz.mobilemedia.bean.MoiveInfo;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.NetVideoPresenter;
import com.wz.mobilemedia.presenter.contract.NetVideoContract;
import com.wz.mobilemedia.ui.adapter.NetVideoAdapter;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class NetworkVideoFragment extends  BaseMediaInfoFragment<NetVideoAdapter> implements NetVideoContract.View{


    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        NetVideoPresenter netVideoPresenter = new NetVideoPresenter(mediaInfoModel,this);
        netVideoPresenter.requestData();
    }


    @Override
    public void showResult(List<MoiveInfo> list) {
        if (list!=null){
            mAdapter.setMediaInfoBeens(list);
        }
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    protected NetVideoAdapter setAdapter() {
        return new NetVideoAdapter(mContext);
    }
}
