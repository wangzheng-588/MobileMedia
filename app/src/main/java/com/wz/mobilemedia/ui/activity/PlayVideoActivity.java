package com.wz.mobilemedia.ui.activity;

import android.widget.MediaController;
import android.widget.VideoView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;

import butterknife.BindView;

public class PlayVideoActivity extends BaseActivity {


    @BindView(R.id.video_view)
    VideoView mVideoView;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {
        MediaInfoBean media = (MediaInfoBean) getIntent().getSerializableExtra("media");

        initPlay(media);
    }

    private void initPlay(MediaInfoBean media) {
        if (media!=null){
            MediaController controller = new MediaController(this);
            mVideoView.setVideoPath(media.getPath());
            mVideoView.setMediaController(controller);
            mVideoView.start();
        }
    }

}
