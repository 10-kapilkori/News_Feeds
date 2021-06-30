package com.task.newsapp.adapter_viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.newsapp.DetailsActivity;
import com.task.newsapp.R;
import com.task.newsapp.model.ArticlesModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private static final String TAG = "NewsAdapter";

    Context context;
    List<ArticlesModel> newsModel;

    public NewsAdapter(Context context, List<ArticlesModel> newsModel) {
        this.context = context;
        this.newsModel = newsModel;
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
        holder.news_title.setText(newsModel.get(position).getTitle()
                .replace("\"", "")
                .replace(" / ", ", "));
        holder.news_author.setText(newsModel.get(position).getModel().getName());
        holder.news_desc.setText(newsModel.get(position).getDescription()
                .replace("<ol><li>", "")
                .replace("<p>", "")
                .replace("</p>", "")
                .replace("<strong>", "")
                .replace("</strong>", "")
                .replace("<br /><br />", "")
                .replace(" / ", ", ")
                .replace("</li><li>", ""));

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
            Date d1 = sdf1.parse(to);
            Date d2 = sdf1.parse(from);
            long diff2 = 0;

            if (d1 != null && d2 != null)
                diff2 = d1.getTime() - d2.getTime();


            diff2 = (int) (diff2 / (1000 * 60 * 60));

            Log.i(TAG, "onBindViewHolder: " + diff2);

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
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.news_img);

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                putExtra("url", newsModel.get(position).getUrl().replace("\"", ""))));
    }

    @Override
    public int getItemCount() {
        return newsModel.size();
    }
}
