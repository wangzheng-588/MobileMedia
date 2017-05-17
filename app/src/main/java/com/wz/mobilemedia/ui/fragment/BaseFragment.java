package com.wz.mobilemedia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-5-18.
 */

public abstract class BaseFragment extends Fragment {

    protected  View mRootView;
    private Unbinder mBind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutRes(),container,false);
        mBind = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    protected abstract int setLayoutRes();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    protected void init(){


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind!=mBind.EMPTY){
            mBind.unbind();
        }
    }
}
