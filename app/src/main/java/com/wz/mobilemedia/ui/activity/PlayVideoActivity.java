package com.wz.mobilemedia.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.util.TimeUtils;

import butterknife.BindView;

import static android.view.View.GONE;

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener, View.OnClickListener {

    @BindView(R.id.top_back)
    ImageButton mTopBack;
    @BindView(R.id.tv_filename)
    TextView mTvFilename;
    @BindView(R.id.ib_share)
    ImageButton mIbShare;
    @BindView(R.id.ib_favorite)
    ImageButton mIbFavorite;
    @BindView(R.id.play_pause)
    ImageButton mPlayPause;
    @BindView(R.id.operation_bg)
    ImageView mOperationBg;
    @BindView(R.id.operation_tv)
    TextView mOperationTv;
    @BindView(R.id.operation_volume_brightness)
    RelativeLayout mOperationVolumeBrightness;
    @BindView(R.id.tv_time_current)
    TextView mTvTimeCurrent;
    @BindView(R.id.tv_time_total)
    TextView mTvTimeTotal;
    @BindView(R.id.mediacontroller_seekbar)
    SeekBar mMediacontrollerSeekbar;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.menu_controller)
    View mControllerMenu;

    private String mMediaPath;


    public static final int AUTO_HIDE_MENU = 1;
    public static final int CURRENT_POSITION = 2;

    private Context mContext;
    private boolean isShowControllerMenu;//是否显示控制菜单
    private boolean isPlay;//是否正在播放
    private long duration;//当前播放视频总长度
    private long currentTime;//当前进度

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case AUTO_HIDE_MENU:
                    hideMenu();
                    break;

                case CURRENT_POSITION:
                    mHandler.removeMessages(CURRENT_POSITION);
                    mTvTimeCurrent.setText(mTimeUtils.stringForTime( mVideoView.getCurrentPosition()));
                    mMediacontrollerSeekbar.setProgress(mVideoView.getCurrentPosition());
                    mHandler.sendEmptyMessageDelayed(CURRENT_POSITION,1000);
                    break;
            }
        }
    };
    private TimeUtils mTimeUtils;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {
        mTimeUtils = new TimeUtils();
        hideMenu();
        initListener();
        mMediaPath = getIntent().getStringExtra("mediaPath");

        initPlay(mMediaPath);

    }

    private void initListener() {
        mPlayPause.setOnClickListener(this);
    }


    private void initPlay(final String mediaPath) {
        if (mediaPath != null) {
            MediaController controller = new MediaController(this);
            mVideoView.setVideoPath(mediaPath);
            mVideoView.setMediaController(controller);


            mVideoView.start();

            mVideoView.setOnErrorListener(this);

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int duration = mp.getDuration();
                    mTvTimeTotal.setText(mTimeUtils.stringForTime(duration));
                    mMediacontrollerSeekbar.setMax(mp.getDuration());

                    mHandler.sendEmptyMessage(CURRENT_POSITION);
                }
            });


        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent intent = new Intent(this, VitamioPlayActivity.class);
        intent.putExtra("mediaPath", mMediaPath);
        startActivity(intent);
        finish();
        return true;
    }



    /**
     * 隐藏控制菜单
     */
    public void hideMenu() {
        mControllerMenu.setVisibility(GONE);
        isShowControllerMenu = false;
        mHandler.removeMessages(AUTO_HIDE_MENU);
    }

    /**
     * 显示控制菜单
     */
    public void showMenu() {
        mControllerMenu.setVisibility(View.VISIBLE);

        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
        isShowControllerMenu = true;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(AUTO_HIDE_MENU);
                Log.e("TAG", "onTouchEvent: "+"down" );
                break;

            case MotionEvent.ACTION_UP:
                Log.e("TAG", "onTouchEvent: "+"up" );
                if (isShowControllerMenu){
                    hideMenu();
                    isShowControllerMenu = false;
                } else {
                    showMenu();
                    isShowControllerMenu = true;
                }

                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause:
                if (!isPlay){
                    mHandler.removeMessages(AUTO_HIDE_MENU);
                    mVideoView.pause();
                    mPlayPause.setImageResource(R.drawable.ic_player_play);
                    isPlay = true;
                } else {
                    mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
                    mVideoView.start();
                    mPlayPause.setImageResource(R.drawable.ic_player_pause);
                    isPlay = false;
                }
                break;
        }
    }
}
