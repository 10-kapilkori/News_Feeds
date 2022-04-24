package com.task.newsapp.ui.sports;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.task.newsapp.adapter_viewholder.SportsAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.util.ArrayList;
import java.util.List;

public class SportsNewsFragment extends Fragment {
    private static final String TAG = "SportsNewsFragment";

    RecyclerView recyclerView;
    ProgressBar sportsProgressBar;
    ImageView offlineIv;
    TextView offlineTv;

    List<ArticlesModel> list;
    SharedPreferences spf;
    SportsAdapter adapter;
    SportsFragmentViewModel viewModel;
    NewsDbViewModel newsDbViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports_news, container, false);
        recyclerView = view.findViewById(R.id.sports_recyclerView);
        sportsProgressBar = view.findViewById(R.id.sportsProgressBar);
        offlineIv = view.findViewById(R.id.sportsOfflineIv);
        offlineTv = view.findViewById(R.id.sportsOfflineTv);

        list = new ArrayList<>();
        String email = "";
        if (getActivity() != null) {
            spf = getActivity().getSharedPreferences("News", Context.MODE_PRIVATE);
            email = spf.getString("email", "");
        }
        viewModel = new ViewModelProvider(this).get(SportsFragmentViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        adapter = new SportsAdapter(getContext(), list, newsDbViewModel, email);

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            sportsProgressBar.setVisibility(View.GONE);
            checkErrors(s);
        });

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            sportsProgressBar.setVisibility(View.GONE);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.makeCall();
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