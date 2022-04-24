package com.task.newsapp.adapter_viewholder;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.newsapp.DetailsActivity;
import com.task.newsapp.R;
import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private static final String TAG = "SavedNewsAdapter";

    Context context;
    List<NewsEntity> list;
    NewsEntity entity;

    public SavedNewsAdapter(Context context, List<NewsEntity> list) {
        this.context = context;
        this.list = list;
    }

    public void savedNews(List<NewsEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_adapter, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.news_title.setText(list.get(position).getTitle());
        holder.news_desc.setText(list.get(position).getDescription());
        holder.news_date.setText(list.get(position).getTime().substring(0, 10));
        holder.news_author.setText(list.get(position).getAuthor());

        Glide.with(context)
                .load(list.get(position).getImgUrl())
                .error(R.drawable.ic_empty)
                .into(holder.news_img);

        holder.newsDownloadBtn.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                                putExtra("url", list.get(position).getUrl().replace("\"", "")),
                        ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()));

    }

    public NewsEntity getPosition(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
