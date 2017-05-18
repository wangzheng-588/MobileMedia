package com.wz.mobilemedia.ui.fragment;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.common.Contract;
import com.wz.mobilemedia.ui.adapter.MediaInfoAdapter;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public class LocalMusicFragment extends BaseMediaInfoFragment {


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
}
