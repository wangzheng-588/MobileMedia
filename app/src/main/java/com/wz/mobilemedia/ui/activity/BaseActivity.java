package com.wz.mobilemedia.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wz.mobilemedia.ui.services.MusicService;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-5-18.
 */

public abstract class BaseActivity extends AppCompatActivity {



    private Unbinder mBind;
    protected MusicService mMusicService;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBind)service).getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayoutResID());
        initVitamio();
        mBind = ButterKnife.bind(this);

        init();

    }

    protected void initVitamio(){};

    protected abstract void init();

    protected abstract int setLayoutResID();

    public void startActivity(Class clazz){
        startActivity(new Intent(this,clazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind!=mBind.EMPTY){
            mBind.unbind();
        }
    }

    public void allBindService(){
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    public void allUnBindService(){
        if (mConnection!=null){
            unbindService(mConnection);
            mConnection= null;
        }
    }
}
