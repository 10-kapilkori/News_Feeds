package com.task.newsapp.adapter_viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView news_title, news_date, news_author, news_desc;
    ImageView news_img;
    ProgressBar newsProgressBar;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        news_title = itemView.findViewById(R.id.news_title);
        news_date = itemView.findViewById(R.id.news_date);
        news_author = itemView.findViewById(R.id.news_author);
        news_desc = itemView.findViewById(R.id.news_desc);
        news_img = itemView.findViewById(R.id.news_image);
        newsProgressBar = itemView.findViewById(R.id.imageProgressBar);
    }
}
