package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.ui.activity.PlayMusicActivity;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalMusicFragment extends BaseMediaInfoFragment {


    @Override
    protected void initView() {
        mRlTemplateBottom.setVisibility(View.VISIBLE);
    }

    @Override
    protected int setMediaType() {
        return Contract.MUSIC_TYPE;
    }

    @Override
    protected MediaInfoAdapter initAdapter() {
        return new MediaInfoAdapter(getActivity());
    }

    @Override
    public void showResult(List<MediaInfoBean> mediaInfoBeans) {

        mAdapter.setMediaInfoBeens(mediaInfoBeans);
    }

    @Override
    public void showError() {

    }

    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(int position,List<MediaInfoBean> mediaInfoBeens) {
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("musicList",(ArrayList)mediaInfoBeens);
                startActivity(intent);
            }
        });
    }
}
