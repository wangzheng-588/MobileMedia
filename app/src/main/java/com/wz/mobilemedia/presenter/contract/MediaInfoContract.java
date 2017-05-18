package com.wz.mobilemedia.presenter.contract;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.ui.BaseView;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public interface MediaInfoContract {

    interface View extends BaseView{
        void showResult(List<MediaInfoBean> mediaInfoBeans);
    }



}
