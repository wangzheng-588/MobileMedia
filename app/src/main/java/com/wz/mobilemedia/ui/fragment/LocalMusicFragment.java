package com.wz.mobilemedia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalMusicFragment extends BaseMediaInfoFragment {


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

}
