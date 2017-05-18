package com.wz.mobilemedia.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by wz on 17-5-18.
 */

public class MediaInfoBean implements Serializable {
    private int mType;
    private String mTitle;
    private String mAlbum;
    private String mMime;
    private String mArtist;
    private String mDuration;
    private String mBitrate;
    private String mDate;
    private String mDisplayName;
    private String mPath;
    private ImageView mIcon;
    private long mSize;
    private String mTile;
    private Bitmap mFrameAtTime;

    public MediaInfoBean() {
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public Bitmap getFrameAtTime() {
        return mFrameAtTime;
    }

    public void setFrameAtTime(Bitmap frameAtTime) {
        mFrameAtTime = frameAtTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    public String getMime() {
        return mMime;
    }

    public void setMime(String mime) {
        mMime = mime;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getBitrate() {
        return mBitrate;
    }

    public void setBitrate(String bitrate) {
        mBitrate = bitrate;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public void setIcon(ImageView icon) {
        mIcon = icon;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }

    public String getTile() {
        return mTile;
    }

    public void setTile(String tile) {
        mTile = tile;
    }
}
