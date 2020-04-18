package com.example.androidscreenshotschedular.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.activity.ImageViewActivity;
import com.example.androidscreenshotschedular.utils.BitMapReading;
import com.example.androidscreenshotschedular.utils.Cache;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.HelperUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImagesAdapter extends BaseAdapter {
    private static final int DESIRED_IMAGE_WIDTH_DP = 90;
    private static final int DESIRED_IMAGE_HEIGHT_DP = 90;
    private static final int CACHE_SIZE = 40;

    private Context context;
    private ExecutorService executorService;
    private Handler handler;
    private List<String> imagesLocation;
    private Cache<Bitmap> imageCache;
    private BitMapReading bitMapReading;

    public ImagesAdapter(Context context, File[] imageFiles) {
        this.context = context;
        executorService = Executors.newFixedThreadPool(5);
        handler = new Handler(Looper.getMainLooper());
        imagesLocation = new ArrayList<>();
        imageCache = new Cache<>(CACHE_SIZE);
        bitMapReading = new BitMapReading(HelperUtil.dpToPx(context, DESIRED_IMAGE_WIDTH_DP), HelperUtil.dpToPx(context, DESIRED_IMAGE_HEIGHT_DP));
        addFileStringToImageLocations(imageFiles);
    }

    public void addFileStringToImageLocations(File[] imageFiles) {
        for (File imageFile : imageFiles) {
            imagesLocation.add(imageFile.getAbsolutePath());
        }
    }

    @Override
    public int getCount() {
        return imagesLocation.size();
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

        setFullViewOnImageViewClick(imageView, position);
        if (!imageCache.isDataFoundAt(position)) {
            setImageGrayColor(imageView);
            loadImage(position, imageView);

        } else {
            imageView.setImageBitmap(imageCache.getDataAt(position));
        }

        return imageView;
    }

    private void setImageGrayColor(ImageView imageView) {
        imageView.setImageResource(R.drawable.gray_color_drawable);
    }

    private void loadImage(final int position, final ImageView imageView) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = bitMapReading.decodeImageFromLocation(imagesLocation.get(position));
                imageCache.setDataAt(position, bitmap);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });


    }

    private void setFullViewOnImageViewClick(ImageView imageView, final int position) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra(Constants.INTENT_IMAGE_PATH_FOR_FULL_VIEW, imagesLocation.get(position));
                context.startActivity(intent);
            }
        });
    }

    private ImageView getImageView() {
        return LayoutInflater.from(context).inflate(R.layout.layout_screen_shot_view, null).findViewById(R.id.screen_shot_image_view);
    }


}
