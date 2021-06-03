package com.task.newsapp.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.NewsAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeNewsFragment extends Fragment {
    private static final String TAG = "HomeNewsFragment";

    HomeFragmentViewModel viewModel;
    List<ArticlesModel> list;
    NewsAdapter adapter;
    SwipeRefreshLayout swipe;
    RecyclerView homeRecyclerView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_news, container, false);

        homeRecyclerView = view.findViewById(R.id.home_recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        list = new ArrayList<>();
        swipe = view.findViewById(R.id.home_swipe_refresh);
        adapter = new NewsAdapter(getContext(), list);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            progressBar.setVisibility(View.GONE);
        });


        String from, to;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        Log.i(TAG, "onCreateView: " + to + " " + from);
        viewModel.makeCall(from, to);

        swipe.setOnRefreshListener(() -> {
            viewModel.makeCall(from, to);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        homeRecyclerView.setAdapter(adapter);
        return view;
    }
}
