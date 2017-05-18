package com.wz.mobilemedia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.ui.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-5-18.
 */

public abstract class ProgressFragment extends Fragment implements BaseView{

    private View mRootView;
    private View mViewProgress;
    private View mViewEmpty;
    private TextView mTextError;
    private FrameLayout mViewContent;
    private Unbinder mBind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_progress, container, false);
        mViewContent = (FrameLayout) mRootView.findViewById(R.id.view_content);
        mViewProgress = mRootView.findViewById(R.id.view_progress);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        mTextError = (TextView) mRootView.findViewById(R.id.text_tip);
        return mRootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRealContentView();
        init();
    }




    private void setRealContentView() {
        View view = LayoutInflater.from(getActivity()).inflate(setLayout(), mViewContent, true);
        mBind = ButterKnife.bind(this, view);
    }

    protected abstract int setLayout();

    protected abstract void init();

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBind!=mBind.EMPTY){
            mBind.unbind();
        }

    }
}
