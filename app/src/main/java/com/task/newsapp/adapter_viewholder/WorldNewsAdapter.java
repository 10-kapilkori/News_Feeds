package com.task.newsapp.adapter_viewholder;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.newsapp.DetailsActivity;
import com.task.newsapp.R;
import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorldNewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    Context context;
    List<ArticlesModel> newsModel;
    NewsDbViewModel newsViewModel;
    NewsEntity entity;
    String email;

    public WorldNewsAdapter(Context context, List<ArticlesModel> newsModel, NewsDbViewModel newsViewModel, String email) {
        this.context = context;
        this.newsModel = newsModel;
        this.newsViewModel = newsViewModel;
        this.email = email;
    }

    public void updatedList(List<ArticlesModel> newsModel) {
        this.newsModel = newsModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_adapter, parent, false);
        return new NewsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.news_author.setText(newsModel.get(position).getModel().getName());

        if (newsModel.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String from = newsModel.get(position).getPublishedAt().substring(11, 19);
        String to = Calendar.getInstance().getTime().toString().substring(11, 19);

        String year = newsModel.get(position).getPublishedAt().substring(0, 4);
        String month = newsModel.get(position).getPublishedAt().substring(5, 7);
        String date = newsModel.get(position).getPublishedAt().substring(8, 10);

        LocalDate first = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        LocalDate second = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));

        Period difference = Period.between(second, first);

        try {
            holder.news_title.setText(newsModel.get(position).getTitle()
                    .replace("\"", "")
                    .replace(" / ", ", "));

            holder.news_desc.setText(newsModel.get(position).getDescription()
                    .replace("<ol><li>", "")
                    .replace("<p>", "")
                    .replace("</p>", "")
                    .replace("<strong>", "")
                    .replace("</strong>", "")
                    .replace("<br /><br />", "")
                    .replace(" / ", ", ")
                    .replace("</li><li>", ""));

            Date d1 = sdf1.parse(to);
            Date d2 = sdf1.parse(from);
            long diff2 = 0;

            if (d1 != null && d2 != null)
                diff2 = d1.getTime() - d2.getTime();

            boolean hours = String.valueOf(diff2).contains("-");

            if (hours) {
                diff2 *= -diff2;
            }

            diff2 = (int) (diff2 / (1000 * 60 * 60));

            if (difference.getDays() > 0)
                holder.news_date.setText(difference.getDays() + " day");
            else
                holder.news_date.setText(diff2 + " hours");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newsModel.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        Glide.with(context)
                .load(newsModel.get(position).getUrlToImage())
                .error(R.drawable.ic_empty)
                .into(holder.news_img);

        holder.newsDownloadBtn.setOnClickListener(v -> {
            entity = new NewsEntity(newsModel.get(position).getTitle(),
                    newsModel.get(position).getDescription(),
                    newsModel.get(position).getUrlToImage(),
                    newsModel.get(position).getUrl(),
                    newsModel.get(position).getPublishedAt(),
                    newsModel.get(position).getModel().getName(),
                    email);

            newsViewModel.insertNews(entity);
            Toast.makeText(context, "News Saved", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                                putExtra("url", newsModel.get(position).getUrl().replace("\"", "")),
                        ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()));
    }

    @Override
    public int getItemCount() {
        return newsModel.size();
    }
}
