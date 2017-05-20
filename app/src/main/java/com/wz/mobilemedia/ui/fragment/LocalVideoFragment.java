package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.ui.activity.PlayVideoActivity;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalVideoFragment extends BaseMediaInfoFragment  {


    @Override
    protected void initView() {
        super.initView();
        mRlTemplateBottom.setVisibility(View.GONE);

    }


    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(int position,List<MediaInfoBean> mediaInfoBeens) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList",(ArrayList<MediaInfoBean>)mediaInfoBeens);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                //intent.putExtra("mediaPath",mediaInfoBeens.get(position).getPath());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void init() {
        super.init();
        mMediaPresenter.requestData(mContext, Contract.VIDEO_TYPE);
    }

}
