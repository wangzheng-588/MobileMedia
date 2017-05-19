package com.wz.mobilemedia.ui.activity;

import android.content.Intent;

import com.wz.mobilemedia.R;
import com.wz.mobilemedia.ui.services.MusicService;

import java.util.ArrayList;

public class PlayMusicActivity extends BaseActivity {



    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_music;
    }

    @Override
    protected void init() {
        int position = getIntent().getIntExtra("position",0);
        ArrayList musicList = (ArrayList) getIntent().getSerializableExtra("musicList");
        playMusic(position, musicList);
    }

    private void playMusic(int  position,ArrayList musicList) {
        Intent intent = new Intent(this,MusicService.class);
        intent.putExtra("position",position);
        intent.putExtra("musicList",musicList);
        startService(intent);
    }
}
