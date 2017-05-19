package com.wz.mobilemedia.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

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
    protected void initView() {
        super.initView();
        mRlTemplateBottom.setVisibility(View.GONE);

    }


    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemListener(new MediaInfoAdapter.OnRecyclerViewItemListener() {
            @Override
            public void itemClickListener(int position,List<MediaInfoBean> mediaInfoBeens) {
                Intent intent = new Intent(getActivity(), VitamioPlayActivity.class);
                intent.putExtra("mediaPath",mediaInfoBeens.get(position).getPath());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void init() {
        super.init();
        mMediaPresenter.requestData(mContext, Contract.VIDEO_TYPE);
    }


    @Override
    public void showError() {
        Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
    }

}
