package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.dao.MediaInfoBeanDao;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.MediaPresenter;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.ui.activity.PlayVideoActivity;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalVideoFragment extends BaseMediaInfoFragment<MediaInfoAdapter> implements MediaInfoContract.View {


    @Override
    protected void initView() {
        super.initView();
        mRlTemplateBottom.setVisibility(View.GONE);

    }

    @Override
    protected MediaInfoAdapter setAdapter() {
        return new MediaInfoAdapter(mContext);
    }


    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(int position,List<MediaInfoBean> mediaInfoBeens) {
                //跳转到系统播放器
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                //跳转到vitamio播放器
                //Intent intent = new Intent(getActivity(), VitamioPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList",(ArrayList<MediaInfoBean>)mediaInfoBeens);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                //intent.putExtra("mediaPath",mediaInfoBeens.get(position).getPath());
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMediaPresenter.requestData(mContext, Contract.VIDEO_TYPE);

            }
        });
    }


    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        mMediaPresenter = new MediaPresenter(mediaInfoModel, this);

        List<MediaInfoBean> mediaInfoBeen = mMediaInfoBeanDao.queryBuilder().where(MediaInfoBeanDao.Properties.MType.eq(Contract.VIDEO_TYPE)).list();
        if (mediaInfoBeen.size()==0){
            mViewEmpty.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setMediaInfoBeens(mediaInfoBeen);
        }

    }

    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {
        if (mediaInfoBeans!=null&&mediaInfoBeans.size()>0){

            List<MediaInfoBean> mediaInfoBeen = mMediaInfoBeanDao.queryBuilder().where(MediaInfoBeanDao.Properties.MType.eq(Contract.VIDEO_TYPE)).list();
            mMediaInfoBeanDao.deleteInTx(mediaInfoBeen);
            mMediaInfoBeanDao.insertInTx(mediaInfoBeans);

            mAdapter.setMediaInfoBeens(mediaInfoBeans);
            mViewEmpty.setVisibility(View.GONE);
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
    public void onRefreshFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
