package com.wz.mobilemedia.ui.fragment;

import android.view.View;

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


    private NetVideoPresenter mNetVideoPresenter;

    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        mNetVideoPresenter = new NetVideoPresenter(mediaInfoModel,this);

    }


    @Override
    public void showResult(List<MoiveInfo> list) {
        if (list!=null){
            mAdapter.setMediaInfoBeens(list);
        }
    }

    @Override
    public void showProgress() {
        mViewProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgress() {
        mViewProgress.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        mViewEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    protected NetVideoAdapter setAdapter() {
        return new NetVideoAdapter(mContext);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        mNetVideoPresenter.requestData();
    }


    @Override
    public void onRefreshFinish() {

    }
}
