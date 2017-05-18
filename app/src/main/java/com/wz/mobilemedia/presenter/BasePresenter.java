package com.wz.mobilemedia.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wz.mobilemedia.ui.BaseView;

/**
 * Created by wz on 17-5-18.
 */

public class BasePresenter<M,V extends BaseView> {


    protected M mModel;
    protected V mView;
    protected Context mContext;

    public BasePresenter(M m,V v){
        this.mModel = m;
        this.mView = v;

        initContext();
    }

    private void initContext() {
        if (mView instanceof Fragment){
            mContext = ((Fragment) mView).getActivity();
        } else {
            mContext = (Context) mView;
        }
    }

    public  void requestData(Context context,int type){

    }

}
