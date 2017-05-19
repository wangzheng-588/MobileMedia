package com.wz.mobilemedia.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.util.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wz on 17-5-18.
 */

public class MediaInfoAdapter extends RecyclerView.Adapter<MediaInfoAdapter.ViewHolder> {


    private Context mContext;
    private List<MediaInfoBean> mMediaInfoBeens;
    private LayoutInflater mInflater;
    private final TimeUtils mTimeUtils;

    public MediaInfoAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTimeUtils = new TimeUtils();
    }

    public void setMediaInfoBeens(List<MediaInfoBean> mediaInfoBeens) {
        mMediaInfoBeens = mediaInfoBeens;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MediaInfoBean media = mMediaInfoBeens.get(position);
        holder.mTvName.setText(media.getTile());
        String duration = media.getDuration();
         duration = mTimeUtils.stringForTime(Integer.parseInt(duration));
        holder.mTvDuration.setText(duration);

        Bitmap bitmap = media.getFrameAtTime();
        holder.mIvIcon.setImageBitmap(bitmap);

        //格式化视频大小
        String fileSize = Formatter.formatFileSize(mContext, Long.valueOf(media.getSize()));

        holder.mTvSize.setText(fileSize);
        if (mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemClickListener(position,mMediaInfoBeens);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMediaInfoBeens==null?0:mMediaInfoBeens.size();
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
        void itemClickListener(int position,List<MediaInfoBean> mediaInfoBeens);
    }
}
