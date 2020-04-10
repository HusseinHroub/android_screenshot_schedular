package com.example.androidscreenshotschedular.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.androidscreenshotschedular.activity.ImageViewActivity;
import com.example.androidscreenshotschedular.utils.BitMapReading;
import com.example.androidscreenshotschedular.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends BaseAdapter {
    private static final int DESIRED_IMAGE_WIDTH = 220;
    private static final int DESIRED_IMAGE_HEIGHT = 220;

    private List<String> imagesLocation;
    private BitMapReading bitMapReading;
    private Context context;

    public ImagesAdapter(Context context, File[] imageFiles) {
        this.context = context;
        imagesLocation = new ArrayList<>();
        bitMapReading = new BitMapReading(DESIRED_IMAGE_WIDTH, DESIRED_IMAGE_WIDTH);
        add(imageFiles);
    }

    public void add(File[] imageFiles) {
        for (File imageFile : imageFiles) {
            imagesLocation.add(imageFile.getAbsolutePath());//TODO maybe just use file cuz u need the name of the image? or any extra info
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = initializeImageView();
            setFullViewOnImageViewClick(imageView, position);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(bitMapReading.decodeImageFromLocation(imagesLocation.get(position)));
        return imageView;
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

    private ImageView initializeImageView() {
        ImageView imageView = new ImageView(context);
        setImageLayout(imageView);
        return imageView;
    }

    private void setImageLayout(ImageView imageView) {
        imageView.setLayoutParams(new GridView.LayoutParams(DESIRED_IMAGE_WIDTH, DESIRED_IMAGE_HEIGHT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
    }


}
