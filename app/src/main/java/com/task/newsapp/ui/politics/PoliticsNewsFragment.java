package com.task.newsapp.ui.politics;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.PoliticsAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PoliticsNewsFragment extends Fragment {
    private static final String TAG = "PoliticsNewsFragment";

    RecyclerView recyclerView;
    ProgressBar politicsProgressBar;
    ImageView offlineIv;
    TextView offlineTv;

    List<ArticlesModel> list;
    PoliticsAdapter adapter;
    PoliticsFragmentViewModel politicsViewModel;
    NewsDbViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_politics_news, container, false);
        recyclerView = view.findViewById(R.id.politics_recyclerView);
        politicsProgressBar = view.findViewById(R.id.politicsProgressBar);
        offlineIv = view.findViewById(R.id.politicsOfflineIv);
        offlineTv = view.findViewById(R.id.politicsOfflineTv);

        list = new ArrayList<>();
        politicsViewModel = new ViewModelProvider(this).get(PoliticsFragmentViewModel.class);
        viewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        adapter = new PoliticsAdapter(getContext(), list, viewModel);

        politicsViewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            politicsProgressBar.setVisibility(View.GONE);
        });

        politicsViewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            checkErrors(s);
            politicsProgressBar.setVisibility(View.GONE);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        String from, to;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        politicsViewModel.makeCall(from, to);

        return view;
    }

    private void checkErrors(String error) {
        if (error.contains("No address associated with hostname") ||
                error.contains("Software caused connection abort")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = getLayoutInflater().inflate(R.layout.error_dialog, null);
            Button okBtn = view.findViewById(R.id.connectionBtn);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            okBtn.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } else if (error.contains("timeout")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = getLayoutInflater().inflate(R.layout.error_timeout, null);
            Button okBtn = view.findViewById(R.id.timeoutBtn);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            okBtn.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }

}