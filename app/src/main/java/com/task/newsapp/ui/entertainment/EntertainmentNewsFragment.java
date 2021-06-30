package com.task.newsapp.ui.entertainment;

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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.EntertainmentAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentNewsFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    ImageView offlineIv;
    TextView offlineTv;
    List<ArticlesModel> list;
    ProgressBar entertainmentProgressBar;
    EntertainmentFragmentViewModel viewModel;
    EntertainmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entertainment_news, container, false);
        recyclerView = view.findViewById(R.id.entertainment_recyclerView);
        entertainmentProgressBar = view.findViewById(R.id.entertainmentProgressBar);
        swipe = view.findViewById(R.id.entertainment_swipe_refresh);
        offlineIv = view.findViewById(R.id.entertainmentOfflineIv);
        offlineTv = view.findViewById(R.id.entertainmentOfflineTv);
        viewModel = new ViewModelProvider(this).get(EntertainmentFragmentViewModel.class);
        list = new ArrayList<>();
        adapter = new EntertainmentAdapter(getContext(), list);

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            checkErrors(s);
            entertainmentProgressBar.setVisibility(View.GONE);
        });

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            entertainmentProgressBar.setVisibility(View.GONE);
        });

        swipe.setOnRefreshListener(() -> {
            viewModel.makeCall();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        viewModel.makeCall();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void checkErrors(String error) {
        if (error.contains("No address associated with hostname") ||
                error.contains("Software caused connection abort")) {

            new AlertDialog.Builder(getContext())
                    .setView(getActivity().getLayoutInflater().inflate(R.layout.error_dialog, null))
                    .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                    .show();

        } else if (error.contains("timeout")) {
            new AlertDialog.Builder(getContext())
                    .setView(getActivity().getLayoutInflater().inflate(R.layout.error_timeout, null))
                    .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}