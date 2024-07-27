package com.example.b07demosummer2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class ViewImageAdapter extends RecyclerView.Adapter<ViewImageAdapter.ViewHolder> {

    private ArrayList<String> images;
    private Context context;    //Useful?

    public ViewImageAdapter(ArrayList<String> imageList, Context context) {
        this.images = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewImageAdapter.ViewHolder holder, int position) {
        Picasso.get().load(images.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
