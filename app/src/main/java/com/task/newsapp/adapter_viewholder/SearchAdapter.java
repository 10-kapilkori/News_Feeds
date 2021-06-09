package com.task.newsapp.adapter_viewholder;

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
import com.task.newsapp.model.ArticlesModel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    Context context;
    List<ArticlesModel> searchList;

    public SearchAdapter(Context context, List<ArticlesModel> list) {
        this.context = context;
        this.searchList = list;
    }

    public void updatedList(List<ArticlesModel> list) {
        this.searchList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.search_adapter, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.news_title.setText(searchList.get(position).getTitle()
                .replace("\"", "")
                .replace(" / ", ", "));
        holder.news_author.setText(searchList.get(position).getModel().getName());
        holder.news_desc.setText(searchList.get(position).getDescription());

        if (searchList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        if (searchList.get(position).getPublishedAt() != null)
            holder.news_date.setText(searchList.get(position).getPublishedAt().substring(0, 10));

        Glide.with(context)
                .load(searchList.get(position).getUrlToImage())
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.news_img);

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                        putExtra("url", searchList.get(position).getUrl().replace("\"", ""))));
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
