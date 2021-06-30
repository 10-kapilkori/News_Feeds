package com.task.newsapp.ui.technology;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.TechnologyAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.List;

public class TechnologyNewsFragment extends Fragment {
    private static final String TAG = "TechnologyFragment";

    RecyclerView recyclerView;
    ProgressBar techProgressBar;
    SwipeRefreshLayout swipe;
    ImageView offlineIv;
    TextView offlineTv;
    List<ArticlesModel> list;
    TechnologyAdapter adapter;
    TechnologyFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_technology_news, container, false);
        recyclerView = view.findViewById(R.id.tech_recyclerView);
        techProgressBar = view.findViewById(R.id.techProgressBar);
        swipe = view.findViewById(R.id.tech_swipe_refresh);
        offlineIv = view.findViewById(R.id.techOfflineIv);
        offlineTv = view.findViewById(R.id.techOfflineTv);
        list = new ArrayList<>();
        adapter = new TechnologyAdapter(getContext(), list);
        viewModel = new ViewModelProvider(this).get(TechnologyFragmentViewModel.class);

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

        swipe.setOnRefreshListener(() -> {
            viewModel.makeCall();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        viewModel.makeCall();
        return view;
    }

    private void checkErrors(String error) {
        if (error.contains("No address associated with hostname") ||
                error.contains("Software caused connection abort")) {

            new AlertDialog.Builder(getContext())
                    .setView(getActivity().getLayoutInflater().inflate(R.layout.error_dialog, null))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();

        } else if (error.contains("timeout")) {
            new AlertDialog.Builder(getContext())
                    .setView(getActivity().getLayoutInflater().inflate(R.layout.error_timeout, null))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
