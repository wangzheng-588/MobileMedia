package com.wz.mobilemedia.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.ui.view.VerticalSeekBar;
import com.wz.mobilemedia.ui.view.VideoView;
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



    private boolean isShowControllerMenu;//是否显示控制菜单
    private boolean isPlay;//是否正在播放
    private boolean isFullScreen = false;

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
    private int mMaxVolume;
    private int mLevel;
    private DisplayMetrics mMetric;
    private int mHeight;
    private int mWidth;
    private BatteryReceiver mBatteryReceiver;
    private int mVideoHeight;
    private int mVideoWidth;


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void init() {
        getWindowHeightWidth();

        //设置seekbar最大声音值
        mVsbVolume.setMax(15);

        //注册电池广播事件
        mBatteryReceiver = new BatteryReceiver();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(mBatteryReceiver, ifilter);

        //手势识别器
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });
        mTimeUtils = new TimeUtils();

        //获取音频管理器
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取最大音量和当前音量
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        hideMenu();
        initListener();


        mVideoPlays = (ArrayList<MediaInfoBean>) (getIntent().getSerializableExtra("videoList"));
        mPosition = getIntent().getIntExtra("position", 0);
        //mMediaPath = getIntent().getStringExtra("mediaPath");

        initPlay(mPosition);

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
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
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
        });
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
                    mVideoHeight = mp.getVideoHeight();
                    mVideoWidth = mp.getVideoWidth();
                }
            });

        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent intent = new Intent(this, VitamioPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoList",((ArrayList)mVideoPlays));
        bundle.putInt("position",mPosition);
        intent.putExtras(bundle);
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
    private float endY;
    private long startTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
         float mDisX = 0;
         float mDisY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                mHandler.removeMessages(AUTO_HIDE_MENU);
                long downTime = event.getDownTime();
                long eventTime = event.getEventTime();
                Log.e("TAG",downTime+"down");
                Log.e("TAG",eventTime+"event");

                break;

            case MotionEvent.ACTION_MOVE:
                mHandler.removeMessages(AUTO_HIDE_MENU);

                float endX = event.getX();
                float endY = event.getY();


                float moveX = endX - startX;
                float moveY = endY - startY;

                mDisX += moveX;
                mDisY += moveY;

               // Log.e("TAG","disx:"+ mDisX);
               // Log.e("TAG","disy:"+ mDisY);

                if (Math.abs(mDisX) > Math.abs(mDisY)&& Math.abs(mDisX)>8) {

                    //拖动改变视频快进
                    changePosition(mDisX);

                } else if (Math.abs(mDisX) < Math.abs(mDisY)&&Math.abs(mDisY)>8) {

                    if (startX<mWidth/2){
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
                long endTime = System.currentTimeMillis();

                if (endTime - startTime < 150) {
                    mHandler.removeMessages(AUTO_HIDE_MENU);

                    if (isShowControllerMenu) {
                        hideMenu();
                        isShowControllerMenu = false;
                    } else {
                        showMenu();
                        isShowControllerMenu = true;
                    }
                }

                Log.e("TAG", "up" + (endTime - startTime));

                mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU, 3000);
                break;
        }
        return super.onTouchEvent(event);
    }

    /*
    改变亮度
     */
    private void changeBrightness(float disY) {
        mControllerMenu.setVisibility(View.VISIBLE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mOperationTv.setVisibility(GONE);
        mIvLight.setVisibility(View.VISIBLE);
        mPlayPause.setVisibility(GONE);
        mRlVolume.setVisibility(GONE);

        float currentBrightness = getWindow().getAttributes().screenBrightness;
        float index =  ((disY / mHeight * mMaxVolume * 3) /50);


        int brightness = (int) (currentBrightness+255*index);


        int changeBrightness = (brightness>255?255:brightness)<0?0:(brightness>255?255:brightness);
        Log.e("TAG",changeBrightness+"");
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = changeBrightness;
        getWindow().setAttributes(lpa);

        int level = (int) (currentBrightness / 255 * 100);
        changeLightImage(level);
        Log.e("TAG","lvel:"+level);

    }

    private void changeLightImage(int level) {

        switch (level){
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
        mControllerMenu.setVisibility(View.VISIBLE);
        mIvLight.setVisibility(GONE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mOperationBg.setVisibility(GONE);
        mTvVolume.setVisibility(GONE);
        mPlayPause.setVisibility(GONE);
        mOperationTv.setVisibility(View.VISIBLE);
        int duration = mVideoView.getDuration();
        int currentPosition = mVideoView.getCurrentPosition();

        int index = (int) (disX / mWidth * duration * 3/10);
        Log.e("TAG","INDEX:"+index);
        Log.e("TAG","duration:"+duration);

        int position = currentPosition+index;

        int changePosition = (position>duration?duration:position)<0?0:(position>duration?duration:position);

        mVideoView.seekTo(changePosition);

        mOperationTv.setText(mTimeUtils.stringForTime(changePosition)+"/"+mTimeUtils.stringForTime(duration));

    }

    private void changeVolume(float dis) {
        mControllerMenu.setVisibility(View.VISIBLE);
        mIvLight.setVisibility(GONE);
        mOperationVolumeBrightness.setVisibility(View.VISIBLE);
        mRlVolume.setVisibility(View.VISIBLE);
        mOperationBg.setVisibility(View.VISIBLE);
        mTvVolume.setVisibility(View.VISIBLE);
        mOperationTv.setVisibility(GONE);
        mPlayPause.setVisibility(GONE);

        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float index = (dis / mHeight * mMaxVolume * 3)/mMaxVolume;
        int volume = (int) (mMaxVolume * index) + currentVolume;

        int changeVolume = (volume > mMaxVolume ? mMaxVolume : volume) < 0 ? 0 : (volume > mMaxVolume ? mMaxVolume : volume);
        if (changeVolume == 0) {
            mLevel = 0;
        } else {
            mLevel = currentVolume * 100 / mMaxVolume;
        }

        changeVolumeImage(mLevel);
        mTvVolume.setText(mLevel + "");

        mVsbVolume.setProgress(changeVolume);
        Log.e("TAG", mLevel + "mlevel");
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
                Log.e("TAG","全屏");
                if(isFullScreen){
                    setVideoType(DEFAULT_SCREEN);
                }else{
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
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mBatteryReceiver!=null){
            unregisterReceiver(mBatteryReceiver);
            mBatteryReceiver = null;
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

                //屏幕的宽
                int width = mWidth;
                //屏幕的宽
                int height = mHeight;
                if (mVideoWidth > 0 && mVideoHeight > 0) {
                    if (mVideoWidth * height > width * mVideoHeight) {
                        //Log.i("@@@", "image too tall, correcting");
                        height = width * mVideoHeight / mVideoWidth;
                    } else if (mVideoWidth * height < width * mVideoHeight) {
                        //Log.i("@@@", "image too wide, correcting");
                        width = height * mVideoWidth / mVideoHeight;
                    } else {
                        //Log.i("@@@", "aspect ratio is correct: " +
                        //width+"/"+height+"="+
                        //mVideoWidth+"/"+mVideoHeight);
                    }
                }

                mVideoView.setVideoViewSize(width, height);
                mIbFullscrrent.setBackgroundResource(R.drawable.ic_action_scale);
                isFullScreen = false;

                break;
        }
    }
}
