package com.wz.mobilemedia.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by wz on 17-5-18.
 */

@Entity
public class MediaInfoBean implements Serializable {

    private static final long serialVersionUID = 18789823872834L;

    @Id(autoincrement = true)
    private Long mId;
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
    @Transient
    private ImageView mIcon;
    private Long mSize;
    private String mTile;
    @Transient
    private transient Bitmap mFrameAtTime;

    public MediaInfoBean() {
    }

    @Generated(hash = 1944517882)
    public MediaInfoBean(Long mId, int mType, String mTitle, String mAlbum, String mMime, String mArtist,
            String mDuration, String mBitrate, String mDate, String mDisplayName, String mPath, Long mSize,
            String mTile) {
        this.mId = mId;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mAlbum = mAlbum;
        this.mMime = mMime;
        this.mArtist = mArtist;
        this.mDuration = mDuration;
        this.mBitrate = mBitrate;
        this.mDate = mDate;
        this.mDisplayName = mDisplayName;
        this.mPath = mPath;
        this.mSize = mSize;
        this.mTile = mTile;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaInfoBean that = (MediaInfoBean) o;

        if (mType != that.mType) return false;
        if (mSize != that.mSize) return false;
        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        if (mAlbum != null ? !mAlbum.equals(that.mAlbum) : that.mAlbum != null) return false;
        if (mMime != null ? !mMime.equals(that.mMime) : that.mMime != null) return false;
        if (mArtist != null ? !mArtist.equals(that.mArtist) : that.mArtist != null) return false;
        if (mDuration != null ? !mDuration.equals(that.mDuration) : that.mDuration != null)
            return false;
        if (mBitrate != null ? !mBitrate.equals(that.mBitrate) : that.mBitrate != null)
            return false;
        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        if (mDisplayName != null ? !mDisplayName.equals(that.mDisplayName) : that.mDisplayName != null)
            return false;
        if (mPath != null ? !mPath.equals(that.mPath) : that.mPath != null) return false;
        if (mIcon != null ? !mIcon.equals(that.mIcon) : that.mIcon != null) return false;
        if (mTile != null ? !mTile.equals(that.mTile) : that.mTile != null) return false;
        return mFrameAtTime != null ? mFrameAtTime.equals(that.mFrameAtTime) : that.mFrameAtTime == null;

    }

    @Override
    public int hashCode() {
        int result = mType;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mAlbum != null ? mAlbum.hashCode() : 0);
        result = 31 * result + (mMime != null ? mMime.hashCode() : 0);
        result = 31 * result + (mArtist != null ? mArtist.hashCode() : 0);
        result = 31 * result + (mDuration != null ? mDuration.hashCode() : 0);
        result = 31 * result + (mBitrate != null ? mBitrate.hashCode() : 0);
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        result = 31 * result + (mDisplayName != null ? mDisplayName.hashCode() : 0);
        result = 31 * result + (mPath != null ? mPath.hashCode() : 0);
        result = 31 * result + (mIcon != null ? mIcon.hashCode() : 0);
        result = 31 * result + (int) (mSize ^ (mSize >>> 32));
        result = 31 * result + (mTile != null ? mTile.hashCode() : 0);
        result = 31 * result + (mFrameAtTime != null ? mFrameAtTime.hashCode() : 0);
        return result;
    }

    public Long getMId() {
        return this.mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    public int getMType() {
        return this.mType;
    }

    public void setMType(int mType) {
        this.mType = mType;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMAlbum() {
        return this.mAlbum;
    }

    public void setMAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getMMime() {
        return this.mMime;
    }

    public void setMMime(String mMime) {
        this.mMime = mMime;
    }

    public String getMArtist() {
        return this.mArtist;
    }

    public void setMArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getMDuration() {
        return this.mDuration;
    }

    public void setMDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getMBitrate() {
        return this.mBitrate;
    }

    public void setMBitrate(String mBitrate) {
        this.mBitrate = mBitrate;
    }

    public String getMDate() {
        return this.mDate;
    }

    public void setMDate(String mDate) {
        this.mDate = mDate;
    }

    public String getMDisplayName() {
        return this.mDisplayName;
    }

    public void setMDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getMPath() {
        return this.mPath;
    }

    public void setMPath(String mPath) {
        this.mPath = mPath;
    }

    public Long getMSize() {
        return this.mSize;
    }

    public void setMSize(Long mSize) {
        this.mSize = mSize;
    }

    public String getMTile() {
        return this.mTile;
    }

    public void setMTile(String mTile) {
        this.mTile = mTile;
    }
}
