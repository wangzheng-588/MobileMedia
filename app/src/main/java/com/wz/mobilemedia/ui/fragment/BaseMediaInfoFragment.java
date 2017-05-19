package com.wz.mobilemedia.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

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

public abstract class BaseMediaInfoFragment extends ProgressFragment implements MediaInfoContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_template_bottom)
    FrameLayout mRlTemplateBottom;

    protected MediaInfoAdapter mAdapter;
    protected MediaPresenter mMediaPresenter;


    @Override
    protected int setLayout() {
        return R.layout.template_reclerview;
    }

    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        mMediaPresenter = new MediaPresenter(mediaInfoModel, this);
    }


    protected void initListener() {

    }





    @Override
    protected void initView() {
        initRecyclerView();
        initListener();
    }

    protected  void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MediaInfoAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {
        if (mediaInfoBeans!=null){
            mAdapter.setMediaInfoBeens(mediaInfoBeans);
        }
    }
}
