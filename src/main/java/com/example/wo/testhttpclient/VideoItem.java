package com.example.wo.testhttpclient;

import java.io.Serializable;

/**
 * Created by wo on 2015/3/15.
 */
public class VideoItem implements Serializable {
    private String img;
    private String title;
    private String videolink;

    public VideoItem(String videolink, String img, String title) {
        this.videolink = videolink;
        this.img = img;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getVideolink() {
        return videolink;
    }
}