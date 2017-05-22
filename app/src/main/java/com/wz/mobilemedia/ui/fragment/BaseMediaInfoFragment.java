package com.wz.mobilemedia.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.presenter.MediaPresenter;

import butterknife.BindView;

/**
 * Created by wz on 17-5-18.
 */

public abstract class BaseMediaInfoFragment<T extends RecyclerView.Adapter> extends ProgressFragment  {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_template_bottom)
    FrameLayout mRlTemplateBottom;

    protected T mAdapter;
    protected MediaPresenter mMediaPresenter;


    @Override
    protected int setLayout() {
        return R.layout.template_reclerview;
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
//        mAdapter = new MediaInfoAdapter(mContext);
        mAdapter = setAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    protected abstract T setAdapter();

    @Override
    public void showError() {
        super.showError();
        Toast.makeText(mContext, "数据加载错误，请重新加载", Toast.LENGTH_SHORT).show();
    }



}
