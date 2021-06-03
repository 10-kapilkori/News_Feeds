package com.task.newsapp.ui.entertainment;

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
import com.task.newsapp.adapter_viewholder.EntertainmentAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentNewsFragment extends Fragment {
    private static final String TAG = "EntertainmentFragment";

    RecyclerView recyclerView;
    List<ArticlesModel> list;
    ProgressBar entertainmentProgressBar;
    EntertainmentFragmentViewModel viewModel;
    EntertainmentAdapter adapter;
    SwipeRefreshLayout swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entertainment_news, container, false);
        recyclerView = view.findViewById(R.id.entertainment_recyclerView);
        entertainmentProgressBar = view.findViewById(R.id.entertainmentProgressBar);
        swipe = view.findViewById(R.id.entertainment_swipe_refresh);
        viewModel = new ViewModelProvider(this).get(EntertainmentFragmentViewModel.class);
        list = new ArrayList<>();
        adapter = new EntertainmentAdapter(getContext(), list);

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
}