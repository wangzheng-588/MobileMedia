package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.ui.activity.VitamioPlayActivity;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalVideoFragment extends BaseMediaInfoFragment  {


    @Override
    protected int setMediaType() {
        return Contract.VIDEO_TYPE;
    }

    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(MediaInfoBean mediaInfoBean) {
                Intent intent = new Intent(getActivity(), VitamioPlayActivity.class);
                intent.putExtra("mediaPath",mediaInfoBean.getPath());
                startActivity(intent);
            }
        });
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
}
