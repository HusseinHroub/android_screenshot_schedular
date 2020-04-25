package com.example.androidscreenshotschedular.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class ZoomAnimator {
    private Animator currentAnimator;
    private int shortAnimationDuration;

    private Rect startBounds;
    private Rect endBounds;
    private float startScale;

    private ImageView smallSizeScreenShot;
    private ImageView fullSizeScreenShot;
    private View rootContainer;
    private View blackBackgroundLayerView;

    public ZoomAnimator(ImageView smallSizeScreenShot,
                        ImageView fullSizeScreenShot,
                        View blackBackgroundLayerView,
                        View rootContainer,
                        Context context) {
        this.smallSizeScreenShot = smallSizeScreenShot;
        this.fullSizeScreenShot = fullSizeScreenShot;
        this.blackBackgroundLayerView = blackBackgroundLayerView;
        this.rootContainer = rootContainer;
        constructAttributes(context);
    }

    private void constructAttributes(Context context) {
        shortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        startBounds = new Rect();
        endBounds = new Rect();
    }

    public void zoomIn() {
        cancelCurrentAnimationIfNotNull();
        startScale = initBoundsAndGetStartScale(smallSizeScreenShot);
        startZoomInAnimation(smallSizeScreenShot);
    }


    private float initBoundsAndGetStartScale(ImageView smallerImage) {
        smallerImage.getGlobalVisibleRect(startBounds);
        adjustOffsetsOfRectangles(startBounds, endBounds);

        float startScale;
        if (isHorizontalAspectRatioBigger()) {
            startScale = (float) startBounds.height() / endBounds.height();
            adjustStartBoundHorizontalOffset(startScale);
        } else {
            startScale = (float) startBounds.width() / endBounds.width();
            adjustStartBoundVerticalOffset(startScale);
        }
        return startScale;
    }

    private void startZoomInAnimation(ImageView smallerImage) {
        AnimatorSet zoomInAnimatorSet = prepareZoomInAnimatorSet(smallerImage, startScale);
        setCurrentAnimatorNullOnAnimationEnd(zoomInAnimatorSet);
        zoomInAnimatorSet.start();
        currentAnimator = zoomInAnimatorSet;
    }


    private AnimatorSet prepareZoomInAnimatorSet(ImageView smallerImage, float startScale) {
        smallerImage.setAlpha(0f);
        fullSizeScreenShot.setVisibility(View.VISIBLE);
        setOriginPivots();
        return initZoomInAnimatorSet(startScale);
    }

    private void setOriginPivots() {
        fullSizeScreenShot.setPivotX(0f);
        fullSizeScreenShot.setPivotY(0f);
    }

    private AnimatorSet initZoomInAnimatorSet(float startScale) {
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(fullSizeScreenShot, View.X,
                        startBounds.left, endBounds.left))
                .with(ObjectAnimator.ofFloat(fullSizeScreenShot, View.Y,
                        startBounds.top, endBounds.top))
                .with(ObjectAnimator.ofFloat(fullSizeScreenShot, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(fullSizeScreenShot,
                        View.SCALE_Y, startScale, 1f))
                .with(ObjectAnimator.ofFloat(blackBackgroundLayerView, View.ALPHA, 0, 1));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    private void setCurrentAnimatorNullOnAnimationEnd(AnimatorSet set) {
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
    }

    public void zoomOut() {
        cancelCurrentAnimationIfNotNull();
        AnimatorSet zoomOutAnimatorSet = initZoomOutAnimatorSet(startScale);
        setSmallerImageBackOnAnimationEnd(zoomOutAnimatorSet, smallSizeScreenShot);
        zoomOutAnimatorSet.start();
        currentAnimator = zoomOutAnimatorSet;
    }

    private void cancelCurrentAnimationIfNotNull() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
    }

    private AnimatorSet initZoomOutAnimatorSet(float startScaleFinal) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(fullSizeScreenShot, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(fullSizeScreenShot,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(fullSizeScreenShot,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(fullSizeScreenShot,
                                View.SCALE_Y, startScaleFinal))
                .with(ObjectAnimator.ofFloat(blackBackgroundLayerView, View.ALPHA, 0));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    private void setSmallerImageBackOnAnimationEnd(AnimatorSet set, final ImageView smallerImage) {
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                getSmallerImageVisible(smallerImage);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                getSmallerImageVisible(smallerImage);
            }
        });
    }


    private void getSmallerImageVisible(ImageView smallerImage) {
        smallerImage.setAlpha(1f);
        fullSizeScreenShot.setVisibility(View.GONE);
        currentAnimator = null;
    }


    private void adjustStartBoundVerticalOffset(float startScale) {
        float startHeight = startScale * endBounds.height();
        float deltaHeight = (startHeight - startBounds.height()) / 2;
        startBounds.top -= deltaHeight;
        startBounds.bottom += deltaHeight;
    }

    private void adjustStartBoundHorizontalOffset(float startScale) {
        float startWidth = startScale * endBounds.width();
        float deltaWidth = (startWidth - startBounds.width()) / 2;
        startBounds.left -= deltaWidth;
        startBounds.right += deltaWidth;
    }

    private boolean isHorizontalAspectRatioBigger() {
        return (float) endBounds.width() / endBounds.height()
                > (float) startBounds.width() / startBounds.height();
    }

    private void adjustOffsetsOfRectangles(Rect startBounds, Rect endBounds) {
        Point globalOffset = new Point();
        rootContainer.getGlobalVisibleRect(endBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        endBounds.offset(-globalOffset.x, -globalOffset.y);
    }

}
