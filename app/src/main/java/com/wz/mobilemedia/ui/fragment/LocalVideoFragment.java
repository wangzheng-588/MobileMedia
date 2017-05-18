package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.ui.activity.PlayVideoActivity;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

/**
 * Created by wz on 17-5-18.
 */

public class LocalVideoFragment extends BaseMediaInfoFragment  {

    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(MediaInfoBean mediaInfoBean) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                intent.putExtra("media",mediaInfoBean);
                startActivity(intent);
            }
        });
    }
}
