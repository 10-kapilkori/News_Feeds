package com.task.newsapp.ui.entertainment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.EntertainmentAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentNewsFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView offlineIv;
    TextView offlineTv;
    ProgressBar entertainmentProgressBar;

    List<ArticlesModel> list;
    EntertainmentFragmentViewModel viewModel;
    NewsDbViewModel newsViewModel;
    EntertainmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entertainment_news, container, false);
        recyclerView = view.findViewById(R.id.entertainment_recyclerView);
        entertainmentProgressBar = view.findViewById(R.id.entertainmentProgressBar);
        offlineIv = view.findViewById(R.id.entertainmentOfflineIv);
        offlineTv = view.findViewById(R.id.entertainmentOfflineTv);

        viewModel = new ViewModelProvider(this).get(EntertainmentFragmentViewModel.class);
        newsViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);

        list = new ArrayList<>();
        adapter = new EntertainmentAdapter(getContext(), list, newsViewModel);

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            checkErrors(s);
            entertainmentProgressBar.setVisibility(View.GONE);
        });

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            entertainmentProgressBar.setVisibility(View.GONE);
        });

        viewModel.makeCall();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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