package com.example.wo.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by wo on 2015/3/15.
 */
public class AsyncImageLoader extends AsyncTask<String,Void,Bitmap>{
    private ImageView image;
    private LruCache<String,Bitmap> lruCache ;

    public AsyncImageLoader(ImageView image, LruCache<String, Bitmap> lruCache) {
        this.image = image;
        this.lruCache = lruCache;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try{
            bitmap = ImageLoader.getBitmap(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addBitmapToCache(params[0],bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
        super.onPostExecute(bitmap);
    }

    /**
     *
     * @param key   索引
     * @param bitmap    图片
     * @deprecated 加入到缓存
     */
    private  void addBitmapToCache(String key,Bitmap bitmap){
        if(getBitmapFromCache(key) != null) {
            lruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key){
        return lruCache.get(key);
    }
}
