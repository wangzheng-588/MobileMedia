package com.wz.mobilemedia.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;

import butterknife.BindView;
import io.vov.vitamio.Vitamio;

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener {


    @BindView(R.id.video_view)
    VideoView mVideoView;
    private MediaInfoBean mMedia;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {


        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //必须写这个，初始化加载库文件
        Vitamio.isInitialized(this);


        mMedia = (MediaInfoBean) getIntent().getSerializableExtra("media");

        initPlay(mMedia);
    }

    private void initPlay(MediaInfoBean media) {
        if (media!=null){
            MediaController controller = new MediaController(this);
            mVideoView.setVideoPath(media.getPath());
            mVideoView.setMediaController(controller);
            mVideoView.start();

            mVideoView.setOnErrorListener(this);
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent intent = new Intent(this, VitamioPlayActivity.class);
        intent.putExtra("media",mMedia);
        startActivity(intent);
        Log.e("TAG", "onError: "+"kwkw");
        finish();
        return true;
    }
}
