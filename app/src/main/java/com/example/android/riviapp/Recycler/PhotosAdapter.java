package com.example.android.riviapp.Recycler;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.riviapp.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public PhotosAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        View view = myViewHolder.view;

        RoundedImageView myImage = (RoundedImageView) view
                .findViewById(R.id.slide_photos);
        Picasso.get().load(images.get(i)).into(myImage);

    }



    @Override
    public int getItemCount() {
        return images.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

}

