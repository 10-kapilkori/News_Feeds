package com.task.newsapp.ui.sports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.SportsAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.List;

public class SportsNewsFragment extends Fragment {
    private static final String TAG = "SportsNewsFragment";

    RecyclerView recyclerView;
    ProgressBar sportsProgressBar;
    SwipeRefreshLayout swipe;
    List<ArticlesModel> list;
    SportsAdapter adapter;
    SportsFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports_news, container, false);
        recyclerView = view.findViewById(R.id.sports_recyclerView);
        sportsProgressBar = view.findViewById(R.id.sportsProgressBar);
        swipe = view.findViewById(R.id.sports_swipe_refresh);
        list = new ArrayList<>();
        adapter = new SportsAdapter(getContext(), list);
        viewModel = new ViewModelProvider(this).get(SportsFragmentViewModel.class);

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            sportsProgressBar.setVisibility(View.GONE);
        });

        swipe.setOnRefreshListener(() -> {
            viewModel.makeCall();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.makeCall();
        return view;
    }
}