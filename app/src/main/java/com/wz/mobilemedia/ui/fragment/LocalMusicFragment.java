package com.wz.mobilemedia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.dao.MediaInfoBeanDao;
import com.wz.mobilemedia.data.MediaInfoModel;
import com.wz.mobilemedia.presenter.MediaPresenter;
import com.wz.mobilemedia.presenter.contract.MediaInfoContract;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalMusicFragment extends BaseMediaInfoFragment<MediaInfoAdapter> implements MediaInfoContract.View{


    @Override
    protected void init() {
        MediaInfoModel mediaInfoModel = new MediaInfoModel();
        mMediaPresenter = new MediaPresenter(mediaInfoModel, this);

        List<MediaInfoBean> mediaInfoBeen = mMediaInfoBeanDao.queryBuilder().where(MediaInfoBeanDao.Properties.MType.eq(Contract.MUSIC_TYPE)).list();
        if (mediaInfoBeen.size()==0){
            mViewEmpty.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setMediaInfoBeens(mediaInfoBeen);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allBindService();
    }

    @Override
    protected void initView() {
        super.initView();
        mRlTemplateBottom.setVisibility(View.VISIBLE);
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
              mMusicService.startPlayMusic(mediaInfoBeens.get(position));

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMediaPresenter.requestData(mContext,Contract.MUSIC_TYPE);
            }
        });
    }


    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {
        if (mediaInfoBeans!=null&&mediaInfoBeans.size()>0){

            List<MediaInfoBean> mediaInfoBeen = mMediaInfoBeanDao.queryBuilder().where(MediaInfoBeanDao.Properties.MType.eq(Contract.MUSIC_TYPE)).list();
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
