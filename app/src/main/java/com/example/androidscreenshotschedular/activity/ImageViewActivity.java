package com.example.androidscreenshotschedular.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.bitmap.BitMapReading;
import com.example.androidscreenshotschedular.utils.Constants;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        DisplayMetrics displayMetrics = getDisplayPhoneScreenMetrics();
        setupPhotoView(displayMetrics.heightPixels, displayMetrics.widthPixels);
    }

    private DisplayMetrics getDisplayPhoneScreenMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private void setupPhotoView(int height, int width) {
        PhotoView photoView = findViewById(R.id.photo_view);
        String imagePath = getIntent().getStringExtra(Constants.INTENT_IMAGE_PATH_FOR_FULL_VIEW);
        photoView.setImageBitmap(new BitMapReading(height, width).decodeImageFromLocation(imagePath));
    }


}
