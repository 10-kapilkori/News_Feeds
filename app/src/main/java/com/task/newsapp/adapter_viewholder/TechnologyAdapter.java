package com.task.newsapp.adapter_viewholder;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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

public class TechnologyAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private static final String TAG = "TechnologyAdapter";

    Context context;
    List<ArticlesModel> techList;
    NewsEntity entity;
    NewsDbViewModel newsViewModel;

    public TechnologyAdapter(Context context, List<ArticlesModel> list, NewsDbViewModel newsViewModel) {
        this.context = context;
        this.techList = list;
        this.newsViewModel = newsViewModel;
    }

    public void updatedList(List<ArticlesModel> list) {
        this.techList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_adapter, parent, false);

        return new NewsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.news_title.setText(techList.get(position).getTitle()
                .replace("\"", "")
                .replace(" / ", ", "));
        holder.news_author.setText(techList.get(position).getModel().getName());
        holder.news_desc.setText(techList.get(position).getDescription());

        if (techList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String from = techList.get(position).getPublishedAt().substring(11, 19);
        String to = Calendar.getInstance().getTime().toString().substring(11, 19);

        String year = techList.get(position).getPublishedAt().substring(0, 4);
        String month = techList.get(position).getPublishedAt().substring(5, 7);
        String date = techList.get(position).getPublishedAt().substring(8, 10);

        LocalDate first = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        LocalDate second = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));

        Period difference = Period.between(second, first);

        if (techList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        try {
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

            Log.i(TAG, "onBindViewHolder: " + to);
            Log.i(TAG, "onBindViewHolder: " + from);

            if (difference.getDays() > 0)
                holder.news_date.setText(difference.getDays() + " day");
            else
                holder.news_date.setText(diff2 + " hours");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (techList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        Glide.with(context)
                .load(techList.get(position).getUrlToImage())
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.news_img);

        holder.newsDownloadBtn.setOnClickListener(v -> {
            entity = new NewsEntity(techList.get(position).getTitle(),
                    techList.get(position).getDescription(),
                    techList.get(position).getUrlToImage(),
                    techList.get(position).getUrl(),
                    techList.get(position).getPublishedAt(),
                    techList.get(position).getModel().getName());

            newsViewModel.insertNews(entity);
            Toast.makeText(context, "News Saved", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                                putExtra("url", techList.get(position).getUrl().replace("\"", "")),
                        ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()));
    }

    @Override
    public int getItemCount() {
        return techList.size();
    }
}
