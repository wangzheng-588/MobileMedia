package com.wz.mobilemedia.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.MediaPresenter;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wz on 17-5-18.
 */

public class BaseMediaInfoFragment extends ProgressFragment implements MediaInfoContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    protected MediaInfoAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.template_reclerview;
    }

    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        MediaPresenter mediaPresenter = new MediaPresenter(mediaInfoModel, this);
        mediaPresenter.requestData(getActivity());
        initRecyclerView();

        initListener();
    }

    protected void initListener() {

    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MediaInfoAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void showError() {

    }


    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {
        mAdapter.setMediaInfoBeens(mediaInfoBeans);
    }

}
