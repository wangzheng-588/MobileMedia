package com.wz.mobilemedia.presenter.contract;

import com.wz.mobilemedia.bean.MoiveInfo;
import com.wz.mobilemedia.ui.BaseView;

import java.util.List;

/**
 * Created by wz on 17-5-18.
 */

public interface NetVideoContract {

    interface View extends BaseView{
        void showResult(List<MoiveInfo> list);
        void showEmpty();
        void showProgress();
        void dismissProgress();
    }



}
