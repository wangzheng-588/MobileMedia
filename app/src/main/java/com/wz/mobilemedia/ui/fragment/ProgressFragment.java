package com.wz.mobilemedia.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.dao.MediaInfoBeanDao;
import com.wz.mobilemedia.dao.manager.DaoManager;
import com.wz.mobilemedia.ui.BaseView;
import com.wz.mobilemedia.ui.services.MusicService;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-5-18.
 */

public abstract class ProgressFragment extends Fragment implements BaseView {

    private View mRootView;
    protected View mViewProgress;
    protected View mViewEmpty;
    protected TextView mTextError;
    private FrameLayout mViewContent;
    private boolean isFragmentVisible;
    private boolean isReuseView;
    protected boolean isFirstVisible;
    private Unbinder mBind;
    protected Context mContext;
    protected MusicService mMusicService;
    protected MediaInfoBeanDao mMediaInfoBeanDao;

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBind) service).getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }

        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    protected void onFragmentFirstVisible() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        initVariable();

        mMediaInfoBeanDao = DaoManager.getInstance().getSession().getMediaInfoBeanDao();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_progress, container, false);
        mViewContent = (FrameLayout) mRootView.findViewById(R.id.view_content);
        mViewProgress = mRootView.findViewById(R.id.view_progress);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        mTextError = (TextView) mRootView.findViewById(R.id.tv_empty);

        mContext = getActivity();

        return mRootView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRealContentView();
        initView();
        init();
    }

    protected abstract void initView();


    private void setRealContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(setLayout(), mViewContent, true);
        mBind = ButterKnife.bind(this, view);
    }

    protected abstract int setLayout();

    protected abstract void init();


    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }


    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        isReuseView = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBind != mBind.EMPTY) {
            mBind.unbind();
        }
        initVariable();
    }

    public void allBindService(){
        Intent intent = new Intent(mContext,MusicService.class);
        mContext.startService(intent);
        mContext.bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    }

    public void allUnBindService(){
        if (mConnection!=null){
            mContext.unbindService(mConnection);
            mConnection = null;
        }
    }




    @Override
    public void showError() {

    }

}
