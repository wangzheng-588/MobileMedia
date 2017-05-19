package com.wz.mobilemedia.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.text.TextUtils;

import com.wz.mobilemedia.bean.MediaInfoBean;

import java.io.File;

import static android.R.attr.type;

/**
 * Created by wz on 17-5-18.
 * 获取音频基本信息工具类
 */

public class MediaInformationUtils {


    public  MediaInfoBean getMediaInfomation(String path){



        MediaInfoBean media = new MediaInfoBean();
        media.setType(type);
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

        //获取视频缩略图
        long seconds = (Long.parseLong(duration) / 1000);
        seconds = ((seconds / 2) + 2) * 1000 * 1000;
        Bitmap frameAtTime = mmr.getFrameAtTime(seconds,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if (frameAtTime!=null){
            frameAtTime = ThumbnailUtils.extractThumbnail(frameAtTime,
                    DisplayUtil.dp2px(100),
                    DisplayUtil.dp2px(60),
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            media.setFrameAtTime(frameAtTime);
        }




        File file = new File(path);
        long length = file.length();
        media.setSize(length);
        media.setPath(path);
        return media;
    }

}
