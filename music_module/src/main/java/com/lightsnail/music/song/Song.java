package com.lightsnail.music.song;

public class Song {
    public  String mName    ;
    public String mPicLink;
    public String mId;
    public String mSinger;
    public String mLyric;
    public String mUrl;

    public void setName(String name) {
        this.mName = name;
    }

    public void setPicLink(String picLink) {
        this.mPicLink = picLink;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setSinger(String singer) {
        this.mSinger = singer;
    }

    public void setLyric(String lyric) {
        this.mLyric = lyric;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
}
