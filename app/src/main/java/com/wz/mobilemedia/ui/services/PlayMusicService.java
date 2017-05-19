package com.wz.mobilemedia.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by wz on 17-5-19.
 */

public class PlayMusicService extends Service{


    private ArrayList playMusicList;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        playMusicList = (ArrayList) intent.getSerializableExtra("musicList");

        Log.e("TAG",playMusicList.size()+"");
        return super.onStartCommand(intent, flags, startId);
    }
}
