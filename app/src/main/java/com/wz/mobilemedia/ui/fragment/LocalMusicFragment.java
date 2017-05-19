package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

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
        super.initView();
        mRlTemplateBottom.setVisibility(View.VISIBLE);
    }


    @Override
    public void showError() {
        Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        mMediaPresenter.requestData(mContext,Contract.MUSIC_TYPE);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

}
