package com.task.newsapp.ui.topheadlines;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.task.newsapp.adapter_viewholder.TopHeadlinesAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesNewsFragment extends Fragment {
    private static final String TAG = "TopHeadlinesNews" + "Fragment";

    TopHeadlinesFragmentViewModel viewModel;
    RecyclerView newsRecyclerView;
    ProgressBar topProgressBar;
    ImageView offlineIv;
    TextView offlineTv;
    SwipeRefreshLayout swipe;
    List<ArticlesModel> topHeadlinesModelList;
    TopHeadlinesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_headlines_news, container, false);

        newsRecyclerView = view.findViewById(R.id.top_recyclerView);
        swipe = view.findViewById(R.id.top_swipe_refresh);
        topProgressBar = view.findViewById(R.id.topProgressBar);
        offlineIv = view.findViewById(R.id.topOfflineIv);
        offlineTv = view.findViewById(R.id.topOfflineTv);
        topHeadlinesModelList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(TopHeadlinesFragmentViewModel.class);

        viewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), topHeadlinesResponseModel -> {
            topHeadlinesModelList = topHeadlinesResponseModel.getArticles();
            adapter.setUpdatedList(topHeadlinesModelList);
            topProgressBar.setVisibility(View.GONE);
        });

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            topProgressBar.setVisibility(View.GONE);
            checkErrors(s);
            Log.i(TAG, "onCreateView: " + s);
        });
/*
        String from, to;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());
*/
        swipe.setOnRefreshListener(() -> {
            viewModel.makeCall();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        viewModel.makeCall();

        adapter = new TopHeadlinesAdapter(getContext(), topHeadlinesModelList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(adapter);
        return view;
    }

    private void checkErrors(String error) {
        if (error.contains("No address associated with hostname")) {
            offlineIv.setVisibility(View.VISIBLE);
            offlineTv.setVisibility(View.VISIBLE);
        } else if (error.contains("Software caused connection abort")) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            offlineIv.setVisibility(View.VISIBLE);
            offlineTv.setVisibility(View.VISIBLE);
        } else if (error.contains("timeout"))
            Toast.makeText(getContext(), "Request timeout", Toast.LENGTH_SHORT).show();
    }
}