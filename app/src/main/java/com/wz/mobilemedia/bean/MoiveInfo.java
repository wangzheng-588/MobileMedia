package com.wz.mobilemedia.bean;

import java.util.List;

/**
 * Created by wz on 17-5-22.
 */

public class MoiveInfo {

    /**
     * id : 65732
     * movieName : 《表情奇幻冒险》预告
     * coverImg : http://img5.mtime.cn/mg/2017/05/18/155628.79082982.jpg
     * movieId : 233547
     * url : http://vfx.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4
     * hightUrl : http://vfx.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4
     * videoTitle : 表情奇幻冒险 中文预告片
     * videoLength : 119
     * rating : -1
     * type : ["动画","冒险","喜剧","家庭","科幻"]
     * summary : 表情小伙伴穿行手机拯救世界
     */

    private int id;
    private String movieName;
    private String coverImg;
    private int movieId;
    private String url;
    private String hightUrl;
    private String videoTitle;
    private int videoLength;
    private double rating;
    private String summary;
    private List<String> type;

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHightUrl(String hightUrl) {
        this.hightUrl = hightUrl;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getUrl() {
        return url;
    }

    public String getHightUrl() {
        return hightUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public double getRating() {
        return rating;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getType() {
        return type;
    }


}
