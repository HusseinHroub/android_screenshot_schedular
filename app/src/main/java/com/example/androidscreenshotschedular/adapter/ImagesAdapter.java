package com.example.androidscreenshotschedular.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.activity.ImageViewActivity;
import com.example.androidscreenshotschedular.utils.BitMapReading;
import com.example.androidscreenshotschedular.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private static final int DESIRED_IMAGE_WIDTH = 220;
    private static final int DESIRED_IMAGE_HEIGHT = 220;

    private List<String> imagesLocation;
    private BitMapReading bitMapReading;
    private Context context;
    private Handler handler;
    private ExecutorService executorService;
    private LayoutInflater mInflater;

    public ImagesAdapter(Context context, File[] imageFiles) {
        this.context = context;
        imagesLocation = new ArrayList<>();
        bitMapReading = new BitMapReading(DESIRED_IMAGE_HEIGHT, DESIRED_IMAGE_WIDTH);
        add(imageFiles);
        executorService = Executors.newFixedThreadPool(1);
        handler = new Handler(Looper.getMainLooper());
        this.mInflater = LayoutInflater.from(context);
    }

    public void add(File[] imageFiles) {
        for (File imageFile : imageFiles) {
            imagesLocation.add(imageFile.getAbsolutePath());//TODO maybe just use file cuz u need the name of the image? or any extra info
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap imageBitMapFromLocation = bitMapReading.decodeImageFromLocation(imagesLocation.get(position));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.imageView.setImageBitmap(imageBitMapFromLocation);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.screen_shot_image_view);
            setFullViewOnImageViewClick(imageView, getAdapterPosition());
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

    }


}
