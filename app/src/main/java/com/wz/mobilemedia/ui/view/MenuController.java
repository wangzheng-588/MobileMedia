package com.wz.mobilemedia.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.util.TimeUtils;

import static com.wz.mobilemedia.ui.activity.PlayVideoActivity.AUTO_HIDE_MENU;

/**
 * Created by wz on 17-5-19.
 */

public class MenuController extends FrameLayout implements View.OnClickListener {


    private  Context mContext;
    private boolean isShowControllerMenu;//是否显示控制菜单
    private boolean isPlay;//是否正在播放
    private long duration;//当前播放视频总长度
    private long currentTime;//当前进度


    private ImageButton mPlayPause;
    private ImageButton mTopBack;
    private TextView mTvName;
    private ImageButton mIbShare;
    private ImageButton mIbFavorite;
    private RelativeLayout mVolume;
    private ImageView mVolumeBg;
    private TextView mOperation_tv;
    private TextView mTvTimeCurrent;
    private TextView mTvTimeTotal;
    private SeekBar mSeekbar;



    private final TimeUtils mTimeUtils;

    public MenuController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        hideMenu();
        isShowControllerMenu = false;
        mTimeUtils = new TimeUtils();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case AUTO_HIDE_MENU:
                    hideMenu();
                    break;
            }
        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = View.inflate(mContext, R.layout.control_menu, null);
        mPlayPause = (ImageButton) view.findViewById(R.id.play_pause);
        mTopBack = (ImageButton) view.findViewById(R.id.top_back);
        mTvName = (TextView) view.findViewById(R.id.tv_filename);
        mIbShare = (ImageButton) view.findViewById(R.id.ib_share);
        mIbFavorite = (ImageButton) view.findViewById(R.id.ib_favorite);
        mVolume = (RelativeLayout) view.findViewById(R.id.operation_volume_brightness);
        mVolumeBg = (ImageView) view.findViewById(R.id.operation_bg);
        mOperation_tv = (TextView) view.findViewById(R.id.operation_tv);
        mTvTimeCurrent = (TextView) view.findViewById(R.id.tv_time_current);
        mTvTimeTotal = (TextView) view.findViewById(R.id.tv_time_total);
        mSeekbar = (SeekBar) view.findViewById(R.id.mediacontroller_seekbar);
        addView(view);


        initListener();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private void initListener() {
        mPlayPause.setOnClickListener(this);
        mTopBack.setOnClickListener(this);
        mTvName.setOnClickListener(this);
        mIbShare.setOnClickListener(this);
        mIbFavorite.setOnClickListener(this);
        mVolume.setOnClickListener(this);
        mVolumeBg.setOnClickListener(this);
        mOperation_tv.setOnClickListener(this);
        mTvTimeCurrent.setOnClickListener(this);
        mTvTimeTotal.setOnClickListener(this);
        mSeekbar.setOnClickListener(this);
    }


    public boolean isShowControllerMenu(){
        return isShowControllerMenu;
    }

    public void setShowControllerMenu(boolean showControllerMenu) {
        isShowControllerMenu = showControllerMenu;
    }

    /**
     * 隐藏控制菜单
     */
    public void hideMenu(){
        setVisibility(GONE);
        isShowControllerMenu=false;
        mHandler.removeMessages(AUTO_HIDE_MENU);
    }

    /**
     * 显示控制菜单
     */
    public void showMenu(){
        setVisibility(VISIBLE);

        mHandler.sendEmptyMessageDelayed(AUTO_HIDE_MENU,3000);
        isShowControllerMenu=true;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause://暂停
                if (isPlay){
                    mPlayPause.setImageResource(R.drawable.ic_player_pause);
                    if (mPlayStateListener !=null){
                        mPlayStateListener.playState(isPlay);
                    }
                    isPlay = false;


                } else {
                    mPlayPause.setImageResource(R.drawable.ic_player_play);
                    if (mPlayStateListener !=null){
                        mPlayStateListener.playState(isPlay);
                    }
                    isPlay = true;

                }
                break;
            case R.id.top_back://返回

                break;
            case R.id.tv_filename://文件名字
                break;
            case R.id.ib_share://共享
                break;
            case R.id.ib_favorite://喜欢
                break;
            case R.id.operation_volume_brightness://音乐图标view布局
                break;
            case R.id.operation_bg://音乐图标布局
                break;
            case R.id.operation_tv://音乐声量
                break;
            case R.id.tv_time_current://当前播放进度
                break;
            case R.id.tv_time_total://当前视频总大小
                break;
            case R.id.mediacontroller_seekbar://seekbar
                if (mOnSeekChangeListener!=null){
                    mOnSeekChangeListener.seekChange(mSeekbar);
                }
                break;
        }
    }

    OnPlayStateListener mPlayStateListener;

    public void setPlayStateListener(OnPlayStateListener listener) {
        mPlayStateListener = listener;
    }

    public interface OnPlayStateListener{
        void playState(boolean isPlay);
    }

    OnSeekChangeListener mOnSeekChangeListener;

    public void setOnSeekChangeListener(OnSeekChangeListener onSeekChangeListener) {
        mOnSeekChangeListener = onSeekChangeListener;
    }

    public interface OnSeekChangeListener{
        void seekChange(SeekBar mSeekbar);
    }


    setCurrentPositionListener mCurrentPositionListener;

    public void setCurrentPositionListener(setCurrentPositionListener currentPositionListener) {
        mCurrentPositionListener = currentPositionListener;
    }


    public interface setCurrentPositionListener{
        void setCurrentPosition(Handler handler);
    }

}
