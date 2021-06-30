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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PoliticsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private static final String TAG = "PoliticsAdapter";

    Context context;
    List<ArticlesModel> politicsModelList;

    public PoliticsAdapter(Context context, List<ArticlesModel> list) {
        this.context = context;
        this.politicsModelList = list;
    }

    public void updatedList(List<ArticlesModel> list) {
        this.politicsModelList = list;
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
        holder.news_title.setText(politicsModelList.get(position).getTitle()
                .replace("\"", "")
                .replace(" / ", ", "));
        holder.news_author.setText(politicsModelList.get(position).getModel().getName());
        holder.news_desc.setText(politicsModelList.get(position).getDescription());

        if (politicsModelList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String from = politicsModelList.get(position).getPublishedAt().substring(11, 19);
        String to = Calendar.getInstance().getTime().toString().substring(11, 19);

        String year = politicsModelList.get(position).getPublishedAt().substring(0, 4);
        String month = politicsModelList.get(position).getPublishedAt().substring(5, 7);
        String date = politicsModelList.get(position).getPublishedAt().substring(8, 10);

        LocalDate first = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        LocalDate second = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));

        Period difference = Period.between(second, first);

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

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (politicsModelList.get(position).getUrlToImage() == null)
            holder.newsProgressBar.setVisibility(View.GONE);

        Glide.with(context)
                .load(politicsModelList.get(position).getUrlToImage())
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.news_img);

        holder.itemView.setOnClickListener(v ->
                context.startActivity(new Intent(context, DetailsActivity.class).
                        putExtra("url", politicsModelList.get(position).getUrl().replace("\"", ""))));
    }

    @Override
    public int getItemCount() {
        return politicsModelList.size();
    }
}
