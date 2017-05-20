package com.wz.mobilemedia.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    public static final int AUTO_HIDE_MENU = 1;
    public static final int CURRENT_POSITION = 2;
    private static final int CHANGE_VOLUME = 3;

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
    @BindView(R.id.ib_battery)
    ImageButton mIbBattery;
    @BindView(R.id.ib_fullscrrent)
    ImageButton mIbFullscrrent;
    @BindView(R.id.ib_video_next)
    ImageButton mIbVideoNext;
    @BindView(R.id.ib_video_pre)
    ImageButton mIbVideoPre;

    private String mMediaPath;


    private Context mContext;
    private boolean isShowControllerMenu;//是否显示控制菜单
    private boolean isPlay;//是否正在播放
    private long duration;//当前播放视频总长度
    private long currentTime;//当前进度

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_HIDE_MENU:
                    hideMenu();
                    break;

                case CURRENT_POSITION:
                    mHandler.removeMessages(CURRENT_POSITION);
                    if (mVideoView != null) {

                        mTvTimeCurrent.setText(mTimeUtils.stringForTime(mVideoView.getCurrentPosition()));
                        mMediacontrollerSeekbar.setProgress(mVideoView.getCurrentPosition());
                    }
                    mHandler.sendEmptyMessageDelayed(CURRENT_POSITION, 1000);
                    break;
            }
        }
    };
    private TimeUtils mTimeUtils;
    private AudioManager mAudioManager;
    private List<MediaInfoBean> mVideoPlays;
    private int mPosition;
    private GestureDetector mGestureDetector;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {
        //注册电池广播事件
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(batteryReceiver, ifilter);

        //手势识别器
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });
        mTimeUtils = new TimeUtils();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        hideMenu();
        initListener();


        mVideoPlays = (ArrayList<MediaInfoBean>) (getIntent().getSerializableExtra("videoList"));
        mPosition = getIntent().getIntExtra("position", 0);
        //mMediaPath = getIntent().getStringExtra("mediaPath");

        initPlay(mPosition);

    }

    private void initListener() {
        mPlayPause.setOnClickListener(this);
        mIbVideoNext.setOnClickListener(this);
        mIbVideoPre.setOnClickListener(this);
        mMediacontrollerSeekbar.setOnSeekBarChangeListener(this);

    }


    private void initPlay(final int position) {

        if (mVideoPlays != null && mVideoPlays.size() > 0) {
            mVideoView.setVideoPath(mVideoPlays.get(position).getPath());

            mVideoView.start();
            mVideoView.setOnErrorListener(this);
            mVideoView.setOnCompletionListener(this);

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int duration = mp.getDuration();
                    mTvFilename.setText(mVideoPlays.get(position).getTile());
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
        mPlayPause.setVisibility(View.VISIBLE);

        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
        isShowControllerMenu = true;
    }


    private float startX;
    private float startY;
    private float endY;
    private long startTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                mHandler.removeMessages(AUTO_HIDE_MENU);
                Log.e("TAG", "onTouchEvent: " + "down");


                break;

            case MotionEvent.ACTION_MOVE:
                mHandler.removeMessages(AUTO_HIDE_MENU);
                showMenu();
                float moveX = event.getX();
                float moveY = event.getY();

                float disX = moveX - startX;
                float disY = moveY - startY;

                if (Math.abs(disX) > Math.abs(disY)) {

                    changePosition(-disX);

                } else if (Math.abs(disX) < Math.abs(disY)) {

                    changeVolume(-disY);

                }

                break;

            case MotionEvent.ACTION_UP:
                long endTime = System.currentTimeMillis();

                if (endTime - startTime < 150) {
                    mHandler.removeMessages(AUTO_HIDE_MENU);

                    if (isShowControllerMenu) {
                        hideMenu();
                    } else {
                        showMenu();
                    }
                }

                Log.e("TAG", "up" + (endTime - startTime));

                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
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

            mHandler.sendEmptyMessage(CHANGE_VOLUME);
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
            float volume = Math.max(currentVolume + index, 0);
            Log.e("TAG", "changeVolume: " + volume);

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volume, 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //播放暂停点击事件
            case R.id.play_pause:
                setPlayButtonState();
                break;

            case R.id.ib_video_next:
                Toast.makeText(this, "下一个视频", Toast.LENGTH_SHORT).show();
                setNextVideo();
                break;

            case R.id.ib_video_pre:
                Toast.makeText(this, "上一个视频", Toast.LENGTH_SHORT).show();
                setPreVideo();
                break;

        }
        mHandler.removeMessages(AUTO_HIDE_MENU);
        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
    }

    private void setNextVideo() {
        mPosition++;
        if (mPosition < mVideoPlays.size()) {
            mVideoView.setVideoPath(mVideoPlays.get(mPosition).getPath());
            mVideoView.start();

            setButtonState();
        }

    }

    private void setPreVideo() {
        mPosition--;
        if (mPosition >= 0) {
            mVideoView.setVideoPath(mVideoPlays.get(mPosition).getPath());
            mVideoView.start();
            setButtonState();
        }

    }

    private void setButtonState() {

        mIbVideoPre.setEnabled(true);
        mIbVideoNext.setEnabled(true);

        if (mPosition == mVideoPlays.size() - 1) {
            mIbVideoNext.setEnabled(false);
            //mIbVideoNext.setBackgroundResource(R.drawable.vector_drawable_video_next_gray);
        }

        if (mPosition==0){
            mIbVideoPre.setEnabled(false);
           // mIbVideoPre.setBackgroundResource(R.drawable.vector_drawable_video_pre_gray);
        }
    }

    private void setPlayButtonState() {
        if (!isPlay) {
            mVideoView.pause();
            mPlayPause.setImageResource(R.drawable.ic_player_play);
            isPlay = true;
        } else {
            mVideoView.start();
            mPlayPause.setImageResource(R.drawable.ic_player_pause);
            isPlay = false;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
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
        mHandler.sendEmptyMessage(CURRENT_POSITION);
        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
        finish();
    }


    public class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            changeBattery(level);

        }
    }

    private void changeBattery(int level) {
        if (level <= 10) {
            mIbBattery.getBackground().setLevel(10);
        } else if (level <= 20) {
            mIbBattery.getBackground().setLevel(20);
        } else if (level <= 50) {
            mIbBattery.getBackground().setLevel(50);
        } else if (level <= 80) {
            mIbBattery.getBackground().setLevel(80);
        } else if (level <= 100) {
            mIbBattery.getBackground().setLevel(level);
        }

    }

}
