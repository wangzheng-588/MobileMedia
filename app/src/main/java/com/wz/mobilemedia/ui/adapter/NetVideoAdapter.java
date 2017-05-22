package com.wz.mobilemedia.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.bean.MoiveInfoBeens;
import com.wz.mobilemedia.util.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wz on 17-5-18.
 */

public class NetVideoAdapter extends RecyclerView.Adapter<NetVideoAdapter.ViewHolder> {


    private Context mContext;
    private List<MoiveInfoBeens> mMoiveInfoBeens;
    private LayoutInflater mInflater;
    private final TimeUtils mTimeUtils;

    public NetVideoAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTimeUtils = new TimeUtils();
    }

    public void setMediaInfoBeens(List<MoiveInfoBeens> moiveInfoBeens) {
        mMoiveInfoBeens = moiveInfoBeens;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mMoiveInfoBeens ==null?0: mMoiveInfoBeens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_duration)
        TextView mTvDuration;
        @BindView(R.id.tv_size)
        TextView mTvSize;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private OnRecyclerViewItemListener mListener;

    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerViewItemListener{
        void itemClickListener(int position, List<MediaInfoBean> mediaInfoBeens);
    }
}
