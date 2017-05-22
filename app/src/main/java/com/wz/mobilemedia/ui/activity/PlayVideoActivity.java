package com.wz.mobilemedia.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.ui.view.VerticalSeekBar;
import com.wz.mobilemedia.ui.view.VideoView;
import com.wz.mobilemedia.util.NetUtils;
import com.wz.mobilemedia.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;
import static com.wz.mobilemedia.R.id.operation_volume_brightness;

public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    public static final int AUTO_HIDE_MENU = 1;
    public static final int CURRENT_POSITION = 2;
    private static final int CHANGE_VOLUME = 3;
    private static final int FULL_SCREEN = 4;
    private static final int DEFAULT_SCREEN = 5;

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
    @BindView(operation_volume_brightness)
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
    @BindView(R.id.tv_volume)
    TextView mTvVolume;
    @BindView(R.id.rl_volume)
    RelativeLayout mRlVolume;
    @BindView(R.id.iv_light)
    ImageView mIvLight;
    @BindView(R.id.vsb_volume)
    VerticalSeekBar mVsbVolume;
    @BindView(R.id.tv_light)
    TextView mTvLight;
    @BindView(R.id.tv_net_speed)
    TextView mTvSpeed1;
    @BindView(R.id.ll_net_speed)
    LinearLayout mLlNetSpeed;
    @BindView(R.id.tv_speed)
    TextView mTvSpeed2;


    private boolean isShowControllerMenu;//是否显示控制菜单
    private boolean isPlay;//是否正在播放
    private boolean isFullScreen = false;
    private TimeUtils mTimeUtils;
    private AudioManager mAudioManager;
    private List<MediaInfoBean> mVideoPlays;
    private int mPosition;
    private GestureDetector mGestureDetector;
    private int mMaxVolume;
    private int mLevel;
    private DisplayMetrics mMetric;
    private int mHeight;
    private int mWidth;
    private BatteryReceiver mBatteryReceiver;
    private int mVideoHeight;
    private int mVideoWidth;
    private Uri mUri;
    private boolean mIsNetUri;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_HIDE_MENU:
                    hideMenu();
                    break;

                case CURRENT_POSITION:
                    mHandler.removeMessages(CURRENT_POSITION);
                    int currentPosition = mVideoView.getCurrentPosition();
                    if (mVideoView != null) {

                        mTvTimeCurrent.setText(mTimeUtils.stringForTime(currentPosition));
                        mMediacontrollerSeekbar.setProgress(mVideoView.getCurrentPosition());
                    }

                    setNetBuffer();


                    setNetSpeed(currentPosition);

                    mHandler.sendEmptyMessageDelayed(CURRENT_POSITION, 1000);
                    break;
            }
        }
    };

    private int prePosition;

    private void setNetSpeed(int currentPosition) {

        if (mIsNetUri){
            int duration = currentPosition - prePosition;

            if (duration<500){
                mLlNetSpeed.setVisibility(View.VISIBLE);
            } else {
                mLlNetSpeed.setVisibility(GONE);
            }

            prePosition = currentPosition;
//            DeviceBandwidthSampler.getInstance().startSampling();
//            ConnectionQuality currentBandwidthQuality = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
//            double downloadKBitsPerSecond = ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();
//
//            Log.e("TAG","connectionQuality:"+currentBandwidthQuality+" downloadKBitsPerSecond:"+downloadKBitsPerSecond+" kb/s");

//            int uid = TrafficBean.getInstance(this, mHandler).getUid();
//
//            TrafficBean trafficBean = new TrafficBean(this, mHandler, uid);
//            trafficBean.startCalculateNetSpeed();
//            double netSpeed = trafficBean.getNetSpeed();
//            long trafficInfo = trafficBean.getTrafficInfo();
//            Log.e("TAG",trafficInfo+"");

        } else {
            mLlNetSpeed.setVisibility(GONE);
        }

    }

    private void setNetBuffer() {
        if (mIsNetUri) {
            int bufferPercentage = mVideoView.getBufferPercentage();
            int buffer = bufferPercentage * mMediacontrollerSeekbar.getMax();
            buffer = buffer / 100;
            mMediacontrollerSeekbar.setSecondaryProgress(buffer);

        }
    }



    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {

        mTimeUtils = new TimeUtils();
        getWindowHeightWidth();
        initVolume();
        initBatterBroadcast();
        initGestureDetector();
        hideMenu();
        initListener();
        getData();
        initPlay(mPosition);


    }

    private void initBatterBroadcast() {
        //注册电池广播事件
        mBatteryReceiver = new BatteryReceiver();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(mBatteryReceiver, ifilter);
    }

    private void initVolume() {
        //获取音频管理器
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取最大音量和当前音量
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //设置seekbar最大声音值
        mVsbVolume.setMax(mMaxVolume);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        mVsbVolume.setProgress(currentVolume);
    }

    private void initGestureDetector() {
        //手势识别器
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                mHandler.removeMessages(AUTO_HIDE_MENU);

                if (isShowControllerMenu) {
                    hideMenu();
                    isShowControllerMenu = false;
                } else {
                    showMenu();
                    isShowControllerMenu = true;
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isFullScreen) {
                    setVideoType(DEFAULT_SCREEN);
                } else {
                    setVideoType(FULL_SCREEN);
                }
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                setPlayButtonState();
                super.onLongPress(e);
            }
        });
    }

    private void getData() {
        mUri = getIntent().getData();
        mVideoPlays = (ArrayList<MediaInfoBean>) (getIntent().getSerializableExtra("videoList"));
        mPosition = getIntent().getIntExtra("position", 0);
    }

    private void getWindowHeightWidth() {
        mMetric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetric);
        //获取屏幕的宽和高
        mHeight = mMetric.heightPixels;
        mWidth = mMetric.widthPixels;
    }

    private void initListener() {
        mPlayPause.setOnClickListener(this);
        mIbVideoNext.setOnClickListener(this);
        mIbVideoPre.setOnClickListener(this);
        mIbFullscrrent.setOnClickListener(this);
        mVsbVolume.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                int volumeIndex = progress * 100 / mMaxVolume;

                mOperationVolumeBrightness.setVisibility(View.VISIBLE);
                mPlayPause.setVisibility(GONE);
                mIvLight.setVisibility(GONE);
                mTvLight.setVisibility(GONE);
                mTvVolume.setVisibility(View.VISIBLE);
                mOperationBg.setVisibility(View.VISIBLE);
                mRlVolume.setVisibility(View.VISIBLE);
                mTvVolume.setText(volumeIndex + "");

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                mHandler.removeCallbacksAndMessages(null);

                mHandler.sendEmptyMessage(CURRENT_POSITION);
                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "start");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "stop");

            }
        });
        mMediacontrollerSeekbar.setOnSeekBarChangeListener(this);

    }


    private void initPlay(final int position) {

        if (mVideoPlays != null && mVideoPlays.size() > 0) {
            mVideoView.setVideoPath(mVideoPlays.get(position).getPath());
            mTvFilename.setText(mVideoPlays.get(position).getTile());
            mIsNetUri = NetUtils.isNetUri(mVideoPlays.get(position).getPath());

        } else if (mUri != null) {
            mVideoView.setVideoURI(mUri);
            mIsNetUri = NetUtils.isNetUri(String.valueOf(mUri));
        }


        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = mp.getDuration();
                mTvTimeTotal.setText(mTimeUtils.stringForTime(duration));
                mMediacontrollerSeekbar.setMax(mp.getDuration());
                mHandler.sendEmptyMessage(CURRENT_POSITION);
                mVideoHeight = mp.getVideoHeight();
                mVideoWidth = mp.getVideoWidth();
                mp.start();
            }
        });

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent intent = new Intent(this, VitamioPlayActivity.class);
        if (mVideoPlays!=null&&mVideoPlays.size()>0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoList", ((ArrayList) mVideoPlays));
            bundle.putInt("position", mPosition);
            intent.putExtras(bundle);
        } else if (mUri!=null){
            intent.setDataAndType(mUri,"video/*");
        }

        startActivity(intent);
        finish();

        return true;
    }


    /**
     * 隐藏控制菜单
     */
    public void hideMenu() {
        mControllerMenu.setVisibility(GONE);
        mHandler.removeMessages(AUTO_HIDE_MENU);
        isShowControllerMenu = false;
    }

    /**
     * 显示控制菜单
     */
    public void showMenu() {
        mControllerMenu.setVisibility(View.VISIBLE);
        mOperationTv.setVisibility(View.GONE);
        mPlayPause.setVisibility(View.VISIBLE);
        mOperationVolumeBrightness.setVisibility(GONE);
        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
    }


    private float startX;
    private float startY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        float mDisX = 0;
        float mDisY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                mHandler.removeMessages(AUTO_HIDE_MENU);

                break;

            case MotionEvent.ACTION_MOVE:
                mHandler.removeMessages(AUTO_HIDE_MENU);

                float endX = event.getX();
                float endY = event.getY();


                float moveX = endX - startX;
                float moveY = endY - startY;

                mDisX += moveX;
                mDisY += moveY;


                if (Math.abs(mDisX) > Math.abs(mDisY) && Math.abs(mDisX) > 8) {

                    //拖动改变视频快进
                    changePosition(mDisX);

                } else if (Math.abs(mDisX) < Math.abs(mDisY) && Math.abs(mDisY) > 8) {

                    if (startX < mWidth / 2) {
                        //改变亮度
                        changeBrightness(-mDisY);

                    } else {
                        //改变音量
                        changeVolume(-mDisY);

                    }
                }
                startY = event.getY();
                startX = event.getX();
                break;

            case MotionEvent.ACTION_UP:

                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
                break;
        }
        return super.onTouchEvent(event);
    }

    /*
    改变亮度
     */
    private void changeBrightness(float disY) {
        mLlNetSpeed.setVisibility(GONE);
        mControllerMenu.setVisibility(View.VISIBLE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mOperationTv.setVisibility(GONE);
        mIvLight.setVisibility(View.VISIBLE);
        mTvLight.setVisibility(View.VISIBLE);
        mPlayPause.setVisibility(GONE);
        mRlVolume.setVisibility(GONE);
        isShowControllerMenu = true;

        float currentBrightness = getWindow().getAttributes().screenBrightness;
        float index = ((disY / mHeight * mMaxVolume * 3) / 50);


        int brightness = (int) (currentBrightness + 255 * index);


        int changeBrightness = (brightness > 255 ? 255 : brightness) < 0 ? 0 : (brightness > 255 ? 255 : brightness);
        Log.e("TAG", changeBrightness + "");
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = changeBrightness;
        getWindow().setAttributes(lpa);

        int level = (int) (currentBrightness / 255 * 100);
        changeLightImage(level);
        mTvLight.setText(level + "%");
        Log.e("TAG", "lvel:" + level);

    }

    private void changeLightImage(int level) {

        switch (level) {
            case 20:
                mIvLight.getBackground().setLevel(20);
                break;
            case 30:
                mIvLight.getBackground().setLevel(30);
                break;
            case 40:
                mIvLight.getBackground().setLevel(40);
                break;
            case 50:
                mIvLight.getBackground().setLevel(50);
                break;
            case 60:
                mIvLight.getBackground().setLevel(60);
                break;
            case 70:
                mIvLight.getBackground().setLevel(70);
                break;
            case 80:
                mIvLight.getBackground().setLevel(80);
                break;
            case 90:
                mIvLight.getBackground().setLevel(90);
                break;
            case 100:
                mIvLight.getBackground().setLevel(100);
                break;


        }
    }


    private void changePosition(float disX) {
        mLlNetSpeed.setVisibility(GONE);
        isShowControllerMenu = true;
        mControllerMenu.setVisibility(View.VISIBLE);
        mIvLight.setVisibility(GONE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mOperationBg.setVisibility(GONE);
        mTvVolume.setVisibility(GONE);
        mPlayPause.setVisibility(GONE);
        mOperationTv.setVisibility(View.VISIBLE);
        mTvLight.setVisibility(GONE);


        int duration = mVideoView.getDuration();
        int currentPosition = mVideoView.getCurrentPosition();

        //int index = (int) (disX / mWidth * duration * 3/10);
        int index = (int) (disX / mWidth * duration);

        int position = currentPosition + index;

        int changePosition = (position > duration ? duration : position) < 0 ? 0 : (position > duration ? duration : position);

        mVideoView.seekTo(changePosition);

        mOperationTv.setText(mTimeUtils.stringForTime(changePosition) + "/" + mTimeUtils.stringForTime(duration));

    }

    private void changeVolume(float dis) {
        isShowControllerMenu = true;
        mControllerMenu.setVisibility(View.VISIBLE);
        mIvLight.setVisibility(GONE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mRlVolume.setVisibility(View.VISIBLE);
        mOperationBg.setVisibility(View.VISIBLE);
        mTvVolume.setVisibility(View.VISIBLE);
        mOperationTv.setVisibility(GONE);
        mPlayPause.setVisibility(GONE);
        mTvLight.setVisibility(GONE);
        mLlNetSpeed.setVisibility(GONE);

        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // float index = (dis / mHeight * mMaxVolume * 3)/mMaxVolume;
        float index = dis / mHeight * mMaxVolume;
        // int volume = (int) (mMaxVolume * index) + currentVolume;
        int volume = (int) index + currentVolume;

        int changeVolume = (volume > mMaxVolume ? mMaxVolume : volume) < 0 ? 0 : (volume > mMaxVolume ? mMaxVolume : volume);
        if (changeVolume == 0) {
            mLevel = 0;
        } else {
            mLevel = currentVolume * 100 / mMaxVolume;
        }

        changeVolumeImage(mLevel);
        mTvVolume.setText(mLevel + "");

        mVsbVolume.setProgress(changeVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, changeVolume, 0);
    }


    private void changeVolumeImage(int level) {
        switch (level) {
            case 0:
                mOperationBg.getBackground().setLevel(0);
                break;

            case 30:
                mOperationBg.getBackground().setLevel(30);
                break;

            case 60:
                mOperationBg.getBackground().setLevel(60);
                break;

            case 100:
                mOperationBg.getBackground().setLevel(100);
                break;
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

            case R.id.ib_fullscrrent:

                if (isFullScreen) {
                    setVideoType(DEFAULT_SCREEN);
                } else {
                    setVideoType(FULL_SCREEN);
                }

                break;

        }
        mHandler.removeMessages(AUTO_HIDE_MENU);
        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
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

        if (mPosition == 0) {
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
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
            mBatteryReceiver = null;
        }

        if (mConnectionChangedListener!=null){
            ConnectionClassManager.getInstance().remove(mConnectionChangedListener);
        }
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

    /**
     * 改变电池图标状态方法
     *
     * @param level
     */
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

    public void setVideoType(int type) {
        switch (type) {
            case FULL_SCREEN:
                mVideoView.setVideoViewSize(mWidth, mHeight);
                isFullScreen = true;
                mIbFullscrrent.setBackgroundResource(R.drawable.ic_action_scale);
                break;

            case DEFAULT_SCREEN:
                int width = mWidth;

                int height = mHeight;
                if (mVideoWidth > 0 && mVideoHeight > 0) {
                    if (mVideoWidth * height > width * mVideoHeight) {

                        height = width * mVideoHeight / mVideoWidth;
                    } else if (mVideoWidth * height < width * mVideoHeight) {

                        width = height * mVideoWidth / mVideoHeight;
                    }
                }

                mVideoView.setVideoViewSize(width, height);
                mIbFullscrrent.setBackgroundResource(R.drawable.ic_action_scale);
                isFullScreen = false;

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            currentVolume--;
            currentVolume = (currentVolume > mMaxVolume ? mMaxVolume : currentVolume) < 0 ? 0 : (currentVolume > mMaxVolume ? mMaxVolume : currentVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            currentVolume++;
            currentVolume = (currentVolume > mMaxVolume ? mMaxVolume : currentVolume) < 0 ? 0 : (currentVolume > mMaxVolume ? mMaxVolume : currentVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    ConnectionChangedListener mConnectionChangedListener;

    private class ConnectionChangedListener implements
            ConnectionClassManager.ConnectionClassStateChangeListener {
        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            Log.e("onBandwidthStateChange", bandwidthState.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mConnectionChangedListener!=null){

            ConnectionClassManager.getInstance().register(mConnectionChangedListener);
        }
    }
}
