package com.wz.mobilemedia.common;

import com.wz.mobilemedia.ui.BaseView;

import io.reactivex.observers.DefaultObserver;

/**
 * Created by wz on 17-5-18.
 */

public abstract class ProgressObserver extends DefaultObserver{

    protected BaseView mView;

    public ProgressObserver(BaseView view) {
        mView = view;
    }


    @Override
    public void onError(Throwable e) {
        mView.showError();
    }

    @Override
    public void onComplete() {

    }
}
