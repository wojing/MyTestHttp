package com.example.wo.testhttpclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.wo.androidhttpclientutils.AndroidHttpClientUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private ListView listview;
    private List<VideoItem> videoItemList;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("msg", "msg.what is "+String.valueOf(msg.what));
            if (msg.what == 0x123) {
                Bundle bundle = msg.getData();
                videoItemList = (List<VideoItem>) bundle.getSerializable("videolist");
            //    handleMessageTest(videoItemList);
                CreateVideoWall(videoItemList);
            }
            super.handleMessage(msg);
        }

        void handleMessageTest(List<VideoItem> videoItemList){  //测试方法
            for(int i = 0;i < videoItemList.size() ; i++ ){
                VideoItem item = videoItemList.get(i);
                String testString = new String("Title: "+item.getTitle()+"  img: "+item.getImg()+"  link: "+item.getVideolink());
                Log.i("handler", testString);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=(Button)findViewById(R.id.button1);
        listview = (ListView)findViewById(R.id.listview);

        btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = "http://91sp.vido.ws/v.php?category=rf&viewtype=basic";
                AndroidHttpClientUtils hcu=new AndroidHttpClientUtils(url,handler);  //传递主进程的handler才能在子进程将msg发送到主线程的MessageQueue中
                new Thread(hcu).start();//实现runnable的线程必须
                v.invalidate();
            }
        });
    }

    public void CreateVideoWall(List<VideoItem> videoItemList){
        List<Map<String,String>> videoMapList = new ArrayList<>();
        for (int i = 0; i < videoItemList.size(); i++){
                VideoItem item = videoItemList.get(i);
                Map<String,String> map=new HashMap<String, String>();
                map.put("img",item.getImg());
                map.put("title",item.getTitle());
                map.put("link",item.getVideolink());
                videoMapList.add(map);
         }


        ImageListAdpter simpleAdapter= new ImageListAdpter(this,videoMapList,R.layout.videoitem,
                new String[]{"img","title","link"},new int[]{R.id.img,R.id.title,R.id.link});

        listview.setAdapter(simpleAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
