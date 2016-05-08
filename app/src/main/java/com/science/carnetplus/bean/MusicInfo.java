package com.science.carnetplus.bean;

import java.io.Serializable;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/5/5
 */
public class MusicInfo implements Serializable {

    private long id; // 歌曲ID
    private String title; // 歌曲名称
    private String artist; // 歌手名称
    private long duration; // 歌曲时长
    private long size; // 歌曲大小
    private String url; // 歌曲路径

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
