package com.wz.mobilemedia.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.wz.mobilemedia.bean.MediaInfoBean;

import java.io.File;

/**
 * Created by wz on 17-5-18.
 * 获取音频基本信息工具类
 */

public class MediaInformationUtils {


    public static MediaInfoBean getMediaInfomation(String path){
        MediaInfoBean media = new MediaInfoBean();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE); // api level 10, 即从GB2.3.3开始有此功能
        if (TextUtils.isEmpty(title)){
            title = path.substring(path.lastIndexOf("/")+1);
        }
        media.setTile(title);
        String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        media.setAlbum(album);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        media.setMime(mime);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        media.setArtist(artist);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        media.setDuration(duration);
        String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE); // 从api level 14才有，即从ICS4.0才有此功能
        media.setBitrate(bitrate);
        String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        media.setDate(date);

        Bitmap frameAtTime = mmr.getFrameAtTime(10, MediaMetadataRetriever.OPTION_CLOSEST);
        media.setFrameAtTime(frameAtTime);
        File file = new File(path);
        long length = file.length();
        media.setSize(length);
        media.setPath(path);
        return media;
    }

}
