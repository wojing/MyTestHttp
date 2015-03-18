package com.example.wo.testhttpclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.wo.Utils.AsyncImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wo on 2015/3/15.
 */
public class ImageListAdpter extends SimpleAdapter{
    private int[] mTo;
    private String[] mFrom;
    private ViewBinder mViewBinder;

    private List<? extends Map<String, ?>> mData;

    private int mResource;
    private int mDropDownResource;
    private LayoutInflater mInflater;


    private ArrayList<Map<String, ?>> mUnfilteredData;

    private int cachesize = (int) Runtime.getRuntime().maxMemory()/5;   //使用1/5可用内存
    private LruCache<String,Bitmap> lruCache = new LruCache<String, Bitmap>(cachesize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight() / 1024;  //以KB为单位
        }
    };

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public ImageListAdpter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
        mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Map dataSet = mData.get(position);
        if (dataSet == null) {
            return null;
        }
        View view;
        if (convertView== null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }


        final ViewBinder binder = mViewBinder;
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else if (v instanceof TextView) {
                            // Note: keep the instanceof TextView check at the bottom of these
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                            setViewText((TextView) v, text);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " +
                                    (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else {
                            loadBitmap(text,(ImageView)v);  //调用异步任务加载图片
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
        return super.getView(position, view, parent);
    }

    public void loadBitmap(String strUrl,ImageView imageView){
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(imageView,lruCache);
        Bitmap bitmap = asyncImageLoader.getBitmapFromCache(strUrl);
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.ic_launcher);
            asyncImageLoader.execute();
        }
        else {
            imageView.setImageBitmap(bitmap);
        }

    }
}
