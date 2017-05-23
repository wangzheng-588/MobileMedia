package com.wz.mobilemedia.ui.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wz.mobilemedia.bean.MediaInfoBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wz on 17-5-19.
 */

public class MusicService extends Service{


    private ArrayList playMusicList;
    private MediaPlayer mMediaPlayer;
    boolean isPlaying ;

    public class MusicBind extends Binder{
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        if (mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public void startPlayMusic(MediaInfoBean music){

        try {
            String path = music.getPath();
            File file = new File(path);

            if (file.exists()){
                Log.e("TAG","存在");
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startOrPauseMusic(){
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            boolean isPlaying;
        } else {
            mMediaPlayer.start();
        }
    }


}
