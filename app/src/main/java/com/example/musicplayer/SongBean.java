package com.example.musicplayer;

import java.io.Serializable;

/**
 * Created by XYG
 * 类SongBean：YGMusic的音乐bean
 */

public class SongBean implements Serializable{
    private String song; //歌曲名称
    private String singer; //歌手名称
    private String album; //专辑名称
    private String duration; //歌曲时长
    private String path; //歌曲路径
    private int album_id;//专辑的ID

    /**
     * 空参构造函数
     */
    public SongBean() {
    }

    /**
     * 带参数构造函数
     * @param song
     * @param singer
     * @param album
     * @param duration
     * @param path
     * @param album_id
     */
    public SongBean( String song, String singer, String album, String duration,String path,int album_id) {
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.album_id =album_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
