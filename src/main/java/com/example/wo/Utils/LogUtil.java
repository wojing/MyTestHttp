package com.example.wo.Utils;

import android.util.Log;

/**
 * Created by wo on 2015/3/15.
 */
public class LogUtil {
    public static void logPrint(int maxLogSize,String veryLongString,String tag){
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v(tag, veryLongString.substring(start, end));
        }
    }
}
