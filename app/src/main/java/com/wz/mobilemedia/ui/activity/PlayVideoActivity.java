package com.wz.mobilemedia.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
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

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

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
                    if (mVideoView!=null){

                        mTvTimeCurrent.setText(mTimeUtils.stringForTime( mVideoView.getCurrentPosition()));
                        mMediacontrollerSeekbar.setProgress(mVideoView.getCurrentPosition());
                    }
                    mHandler.sendEmptyMessageDelayed(CURRENT_POSITION,1000);
                    break;
            }
        }
    };
    private TimeUtils mTimeUtils;
    private AudioManager mAudioManager;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {
        mTimeUtils = new TimeUtils();
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        hideMenu();
        initListener();
        mMediaPath = getIntent().getStringExtra("mediaPath");

        initPlay(mMediaPath);

    }

    private void initListener() {
        mPlayPause.setOnClickListener(this);
        mMediacontrollerSeekbar.setOnSeekBarChangeListener(this);
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



    private float startX;
    private float startY;
    private float endY;
    private long startTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                mHandler.removeMessages(AUTO_HIDE_MENU);
                Log.e("TAG", "onTouchEvent: "+"down" );

                //showMenu();

                break;

            case MotionEvent.ACTION_MOVE:
                mHandler.removeMessages(AUTO_HIDE_MENU);
                showMenu();
                float moveX = event.getX();
                float moveY = event.getY();

                float disX = moveX - startX;
                float disY = moveY - startY;

                if (Math.abs(disX)>Math.abs(disY)){

                        changePosition(-disX);

                } else if (Math.abs(disX)<Math.abs(disY)){

                    changeVolume(-disY);

                }

                break;

            case MotionEvent.ACTION_UP:
                long endTime = System.currentTimeMillis();

                if (endTime - startTime<150){
                    mHandler.removeMessages(AUTO_HIDE_MENU);
                    if (isShowControllerMenu){
                        hideMenu();
                    } else {
                        showMenu();
                    }
                }

                Log.e("TAG","up"+(endTime - startTime));

                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
                break;
        }
        return true;
    }

    private void changePosition(float disX) {
        int duration = mVideoView.getDuration();
        int currentPosition = mVideoView.getCurrentPosition();
        if (!isScreenOriatationPortrait(this)) {


            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;

            float index = disX / height * duration * 3;

            float max = Math.max(currentPosition + index, 0);

            mVideoView.seekTo((int) max);

        }

    }

    private void changeVolume(float dis) {

        mOperationBg.setVisibility(View.VISIBLE);
        mPlayPause.setVisibility(GONE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (!isScreenOriatationPortrait(this)) {

            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;


            float index = dis / height * maxVolume * 3;
            float volume = Math.max(currentVolume+index,0);
            Log.e("TAG", "changeVolume: "+volume );

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volume,0);
        }


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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){

            seekBar.setProgress(progress);
            mVideoView.seekTo(progress);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.sendEmptyMessageDelayed(CURRENT_POSITION,3000);
        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 返回当前屏幕是否为竖屏。
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


}
