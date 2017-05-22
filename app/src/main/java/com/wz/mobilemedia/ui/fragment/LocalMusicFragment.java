package com.wz.mobilemedia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
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
                //Intent intent = new Intent(getActivity(), PlayMusicActivity.class);

               // allBindService();

                Log.e("TAG", "itemClickListener: "+mMusicService.toString() );
              mMusicService.startPlayMusic(mediaInfoBeens.get(position));
                //intent.putExtra("position",position);
                //intent.putExtra("musicList",(ArrayList)mediaInfoBeens);

               // startActivity(intent);
            }
        });
    }


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        mMediaPresenter.requestData(mContext,Contract.MUSIC_TYPE);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }



    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {
        if (mediaInfoBeans!=null){
            mAdapter.setMediaInfoBeens(mediaInfoBeans);
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

}
