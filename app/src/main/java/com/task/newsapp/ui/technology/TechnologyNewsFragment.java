package com.task.newsapp.ui.technology;

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
import com.task.newsapp.adapter_viewholder.TechnologyAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.util.ArrayList;
import java.util.List;

public class TechnologyNewsFragment extends Fragment {
    private static final String TAG = "TechnologyFragment";

    RecyclerView recyclerView;
    ProgressBar techProgressBar;
    ImageView offlineIv;
    TextView offlineTv;

    List<ArticlesModel> list;
    TechnologyAdapter adapter;
    TechnologyFragmentViewModel viewModel;
    NewsDbViewModel newsDbViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_technology_news, container, false);
        recyclerView = view.findViewById(R.id.tech_recyclerView);
        techProgressBar = view.findViewById(R.id.techProgressBar);
        offlineIv = view.findViewById(R.id.techOfflineIv);
        offlineTv = view.findViewById(R.id.techOfflineTv);

        list = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(TechnologyFragmentViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        adapter = new TechnologyAdapter(getContext(), list, newsDbViewModel);

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            techProgressBar.setVisibility(View.GONE);
            checkErrors(s);
        });

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            techProgressBar.setVisibility(View.GONE);
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
