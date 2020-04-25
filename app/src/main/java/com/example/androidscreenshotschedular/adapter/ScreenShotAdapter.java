package com.example.androidscreenshotschedular.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.action.ScreenShotHandler;
import com.example.androidscreenshotschedular.utils.Cache;
import com.example.androidscreenshotschedular.utils.HelperUtil;
import com.example.androidscreenshotschedular.utils.bitmap.BitMapReading;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenShotAdapter extends BaseAdapter {
    private static final int DESIRED_IMAGE_WIDTH_DP = 90;
    private static final int DESIRED_IMAGE_HEIGHT_DP = 90;
    private static final int CACHE_SIZE = 40;

    private Context context;
    private ExecutorService executorService;
    private Handler handler;
    private List<String> screenShotPaths;
    private Cache<Bitmap> screenShotCache;
    private BitMapReading bitMapReading;

    private ScreenShotHandler screenShotHandler;

    public ScreenShotAdapter(Context context, List<String> screenShotPaths, ScreenShotHandler screenShotHandler) {
        this.context = context;
        executorService = Executors.newFixedThreadPool(5);
        handler = new Handler(Looper.getMainLooper());
        this.screenShotPaths = screenShotPaths;
        this.screenShotHandler = screenShotHandler;
        screenShotCache = new Cache<>(CACHE_SIZE);
        bitMapReading = new BitMapReading(HelperUtil.dpToPx(context, DESIRED_IMAGE_WIDTH_DP), HelperUtil.dpToPx(context, DESIRED_IMAGE_HEIGHT_DP));
    }

    public void addImageAbsolutePath(String imageAbsolutePath) {
        screenShotPaths.add(imageAbsolutePath);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return screenShotPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = getImageView();
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setOnClickListener(v -> screenShotHandler.onClick(imageView, screenShotPaths.get(position)));

        if (!screenShotCache.isDataFoundAt(position)) {
            setImageGrayColor(imageView);
            loadImage(position, imageView);
        } else {
            imageView.setImageBitmap(screenShotCache.getDataAt(position));
        }

        return imageView;
    }

    private void setImageGrayColor(ImageView imageView) {
        imageView.setImageResource(R.drawable.gray_color_drawable);
    }

    private void loadImage(final int position, final ImageView imageView) {
        executorService.execute(() -> {
            final Bitmap bitmap = bitMapReading.decodeImageFromLocation(screenShotPaths.get(position));
            screenShotCache.setDataAt(position, bitmap);
            handler.post(() -> imageView.setImageBitmap(bitmap));
        });


    }

    private ImageView getImageView() {
        return LayoutInflater.from(context).inflate(R.layout.layout_screen_shot_view, null).findViewById(R.id.screen_shot_image_view);
    }

}
