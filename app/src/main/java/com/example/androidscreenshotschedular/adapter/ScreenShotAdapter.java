package com.example.androidscreenshotschedular.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

import java.util.ArrayList;
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

    private Cache<Bitmap> screenShotCache;
    private BitMapReading bitMapReading;
    private ScreenShotHandler screenShotHandler;
    private ScreenShotsWrapper screenShotsWrapper;

    public ScreenShotAdapter(Context context, List<String> screenShotPaths, ScreenShotHandler screenShotHandler) {
        this.context = context;
        this.screenShotHandler = screenShotHandler;
        constructAttributes(screenShotPaths);

    }

    private void constructAttributes(List<String> screenShotPaths) {
        executorService = Executors.newFixedThreadPool(5);
        handler = new Handler(Looper.getMainLooper());
        screenShotsWrapper = new ScreenShotsWrapper(screenShotPaths);
        screenShotCache = new Cache<>(CACHE_SIZE);
        bitMapReading = new BitMapReading(HelperUtil.dpToPx(context, DESIRED_IMAGE_WIDTH_DP), HelperUtil.dpToPx(context, DESIRED_IMAGE_HEIGHT_DP));
    }

    public void addImageAbsolutePath(String imageAbsolutePath) {
        screenShotsWrapper.addPath(imageAbsolutePath);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return screenShotsWrapper.size();
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

        imageView.setOnClickListener(v -> screenShotHandler.onClick(imageView, screenShotsWrapper.getScreenShotPath(position)));
        manageImageLoading(position, imageView);
        animateIfNewlyAddedImage(position, imageView);

        return imageView;
    }

    private void manageImageLoading(int position, ImageView imageView) {
        if (!screenShotCache.isDataFoundAt(position)) {
            setImageGrayColor(imageView);
            loadImage(position, imageView);
        } else {
            imageView.setImageBitmap(screenShotCache.getDataAt(position));
        }
    }

    private void animateIfNewlyAddedImage(int position, ImageView imageView) {
        if (!screenShotsWrapper.isAnimated(position)) {
            screenShotsWrapper.setAnimated(position);
            AnimatorSet animatorSet = getAnimatorSet(imageView);
            animatorSet.setDuration(240);
            animatorSet.start();
        }
    }

    private AnimatorSet getAnimatorSet(ImageView imageView) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(imageView, "translationY", 300f, 0))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1.5f, 1))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1.5f, 1))
                .with(ObjectAnimator.ofFloat(imageView, View.ALPHA, 0, 1));
        return animatorSet;
    }

    private void setImageGrayColor(ImageView imageView) {
        imageView.setImageResource(R.drawable.gray_color_drawable);
    }

    private void loadImage(final int position, final ImageView imageView) {
        executorService.execute(() -> {
            final Bitmap bitmap = bitMapReading.decodeImageFromLocation(screenShotsWrapper.getScreenShotPath(position));
            screenShotCache.setDataAt(position, bitmap);
            handler.post(() -> imageView.setImageBitmap(bitmap));
        });


    }

    private ImageView getImageView() {
        return LayoutInflater.from(context).inflate(R.layout.layout_screen_shot_view, null).findViewById(R.id.screen_shot_image_view);
    }

    private class ScreenShotsWrapper {
        private List<String> screenShotPaths;
        private List<Boolean> animationStatus;

        private ScreenShotsWrapper(List<String> screenShotPaths) {
            this.screenShotPaths = screenShotPaths;
            animationStatus = new ArrayList<>();
            for (int i = 0; i < screenShotPaths.size(); i++) {
                animationStatus.add(false);
            }
        }

        private void addPath(String imageAbsolutePath) {
            screenShotPaths.add(imageAbsolutePath);
            animationStatus.add(false);
        }

        private int size() {
            return screenShotPaths.size();
        }

        private String getScreenShotPath(int position) {
            return screenShotPaths.get(position);
        }

        private boolean isAnimated(int position) {
            return animationStatus.get(position);
        }

        private void setAnimated(int position) {
            animationStatus.set(position, true);
        }
    }


}
