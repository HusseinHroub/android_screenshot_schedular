package com.example.androidscreenshotschedular.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.BitMapReading;
import com.example.androidscreenshotschedular.utils.Constants;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        initializePhotoView();
    }

    private void initializePhotoView() {
        PhotoView photoView = findViewById(R.id.photo_view);
        String imagePath = getIntent().getStringExtra(Constants.INTENT_IMAGE_PATH_FOR_FULL_VIEW);
        photoView.setImageBitmap(new BitMapReading(photoView.getWidth(), photoView.getHeight()).decodeImageFromLocation(imagePath));
    }
}
