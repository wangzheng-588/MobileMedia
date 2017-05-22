package com.wz.mobilemedia.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MoiveInfo;
import com.wz.mobilemedia.ui.activity.PlayVideoActivity;
import com.wz.mobilemedia.util.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wz on 17-5-18.
 */

public class NetVideoAdapter extends RecyclerView.Adapter<NetVideoAdapter.ViewHolder> {


    private Context mContext;
    private List<MoiveInfo>  mMoiveInfoBeens;
    private LayoutInflater mInflater;
    private final TimeUtils mTimeUtils;

    public NetVideoAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mTimeUtils = new TimeUtils();
    }

    public void setMediaInfoBeens(List<MoiveInfo> list) {
        if (list!=null){

            mMoiveInfoBeens = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MoiveInfo moive = mMoiveInfoBeens.get(position);
        holder.mTvName.setText(moive.getMovieName());

        holder.mTvDuration.setText(moive.getVideoTitle());

        holder.mIvIcon.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(mContext).load(moive.getCoverImg()).into(holder.mIvIcon);

        holder.mTvSize.setText(moive.getVideoLength()+"秒");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayVideoActivity.class);
                intent.setDataAndType(Uri.parse(moive.getUrl()),"video/*");
                mContext.startActivity(intent);
            }
        });
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


}
