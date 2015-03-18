package com.example.wo.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wo on 2015/3/15.
 *
 *
 */
public class ImageLoader {
    public static Bitmap getBitmap(String urlStr) throws IOException {
        Bitmap bitmap = null;
        String urlTest = "http:////img2.w3p.la/thumb/3_107435.jpg";
        URL url= new URL(urlTest);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5*1000);
        connection.setDoInput(true);
        connection.connect();
        InputStream is = connection.getInputStream();
        Log.i("Bitmap is ",urlStr);
        bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
    }
}
