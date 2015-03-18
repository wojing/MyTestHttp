package com.example.wo.androidhttpclientutils;

import android.util.Log;

import com.example.wo.testhttpclient.VideoItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wo on 2015/3/15.
 */
public class FeatureParse {


    public  List<VideoItem> getVideolist(String html){
        List<VideoItem> videoItemList=new ArrayList<VideoItem>();

        Document doc = Jsoup.parse(html);
        Elements nodes = doc.getElementsByClass("imagechannelhd");
        for (int i = 0; i < nodes.size(); i++) {
            Element node = nodes.get(i).select("a").first();
            String link = node.attr("href");
            String title = node.select("img").attr("title");
            String img = node.select("img").attr("src");
            VideoItem item = new VideoItem(link,img,title);
            videoItemList.add(item);
        }

        return videoItemList;
    }

    public void getVideolistTest(String html){
        List<VideoItem> videoItemList =getVideolist(html);
        for (int i = 0; i < videoItemList.size(); i++) {
            VideoItem item = videoItemList.get(i);
            String testString = new String("Title: "+item.getTitle()+"  img: "+item.getImg()+"  link: "+item.getVideolink());
            Log.i("video",testString);
        }
    }
}


