package com.example.wo.androidhttpclientutils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.wo.testhttpclient.VideoItem;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by wo on 2015/3/14.
 */
public class AndroidHttpClientUtils implements  Runnable{

    private  String url = null;
    private Handler httpHandler = new Handler();

    private class HttpHandler extends android.os.Handler{
        public HttpHandler() {
            super();
        }

        public HttpHandler(Looper looper) {
            super(looper);
        }
    };

    public AndroidHttpClientUtils(String url,Handler handler){
        this.url=url;
        this.httpHandler = handler;
    }


    public void run() {
        //String url="http://91sp.vido.ws/v.php?category=rf&viewtype=basic";
        //String url="http://91sp.vido.ws/view_video.php?viewkey=d20b849adb00e395cda0&page=1&viewtype=basic&category=rf";
        HttpGet httpget=new HttpGet(url);
        Header headers[]={};

        httpget.addHeader("Accept-Language","zh-CN,zh;q=0.8");
        httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36");
        httpget.addHeader("Connection","keep-alive");

        HttpClient httpclient = new DefaultHttpClient();


        try{
            HttpResponse response=httpclient.execute(httpget);  //connect
          //  new FeatureParse().getVideolistTest(EntityUtils.toString(response.getEntity(), "utf-8"));
            List<VideoItem> videolist=  new FeatureParse().getVideolist(EntityUtils.toString(response.getEntity(), "utf-8"));
           // LogUtil.logPrint(65535,(EntityUtils.toString(response.getEntity(),"utf-8")),"TAG");
            Log.i("send","Start send msg");
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", (java.io.Serializable) videolist);  //ArryaList 需序列化
            msg.setData(bundle);
            msg.what = 0x123;


            httpHandler.sendMessage(msg);   //用本线程所属的handler ，带自己的loop发送消息


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
