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

public class ZoomInAnimator {
    private Animator currentAnimator;
    private int shortAnimationDuration;

    private Rect startBounds;
    private Rect endBounds;

    private ImageView smallerImage;
    private ImageView biggerImage;
    private View biggerImageContainer;

    public ZoomInAnimator(ImageView smallerImage, ImageView biggerImage, View biggerImageContainer,
                          Context context) {
        this.smallerImage = smallerImage;
        this.biggerImage = biggerImage;
        shortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        startBounds = new Rect();
        endBounds = new Rect();
        this.biggerImageContainer = biggerImageContainer;
    }

    public void zoomInToBiggerImage() {
        cancelCurrentAnimationIfNotNull();
        float startScale = initBoundsAndGetStartScale(smallerImage);
        startZoomInAnimation(smallerImage, startScale);
        initZoomOutOnExpandedClick(smallerImage, startScale);
    }

    private void startZoomInAnimation(ImageView smallerImage, float startScale) {
        AnimatorSet zoomInAnimatorSet = prepareZoomInAnimatorSet(smallerImage, startScale);
        setCurrentAnimatorNullOnAnimationEnd(zoomInAnimatorSet);
        zoomInAnimatorSet.start();
        currentAnimator = zoomInAnimatorSet;
    }

    private void initZoomOutOnExpandedClick(final ImageView smallerImage, final float startScaleFinal) {
        biggerImage.setOnClickListener(view -> {
            cancelCurrentAnimationIfNotNull();
            AnimatorSet zoomOutAnimatorSet = initZoomOutAnimatorSet(startScaleFinal);
            setSmallerImageBackOnAnimationEnd(zoomOutAnimatorSet, smallerImage);
            zoomOutAnimatorSet.start();
            currentAnimator = zoomOutAnimatorSet;
        });
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

    private AnimatorSet prepareZoomInAnimatorSet(ImageView smallerImage, float startScale) {
        smallerImage.setAlpha(0f);
        biggerImage.setVisibility(View.VISIBLE);
        setOriginPivots();
        return initZoomInAnimatorSet(startScale);
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

    private void getSmallerImageVisible(ImageView smallerImage) {
        smallerImage.setAlpha(1f);
        biggerImage.setVisibility(View.GONE);
        currentAnimator = null;
    }

    private AnimatorSet initZoomOutAnimatorSet(float startScaleFinal) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(biggerImage, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(biggerImage,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(biggerImage,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(biggerImage,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    private AnimatorSet initZoomInAnimatorSet(float startScale) {
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(biggerImage, View.X,
                        startBounds.left, endBounds.left))
                .with(ObjectAnimator.ofFloat(biggerImage, View.Y,
                        startBounds.top, endBounds.top))
                .with(ObjectAnimator.ofFloat(biggerImage, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(biggerImage,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    private void setOriginPivots() {
        biggerImage.setPivotX(0f);
        biggerImage.setPivotY(0f);
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
        biggerImageContainer.getGlobalVisibleRect(endBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        endBounds.offset(-globalOffset.x, -globalOffset.y);
    }

    private void cancelCurrentAnimationIfNotNull() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
    }
}
