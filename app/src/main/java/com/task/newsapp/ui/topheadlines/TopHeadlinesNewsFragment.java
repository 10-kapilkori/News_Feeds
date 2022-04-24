package com.task.newsapp.ui.topheadlines;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.task.newsapp.adapter_viewholder.TopHeadlinesAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesNewsFragment extends Fragment {
    private static final String TAG = "TopHeadlinesNews" + "Fragment";

    RecyclerView newsRecyclerView;
    ProgressBar topProgressBar;
    ImageView offlineIv;
    TextView offlineTv;

    List<ArticlesModel> topHeadlinesModelList;
    SharedPreferences spf;
    TopHeadlinesFragmentViewModel viewModel;
    NewsDbViewModel newsDbViewModel;
    TopHeadlinesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_headlines_news, container, false);

        newsRecyclerView = view.findViewById(R.id.top_recyclerView);
        topProgressBar = view.findViewById(R.id.topProgressBar);
        offlineIv = view.findViewById(R.id.topOfflineIv);
        offlineTv = view.findViewById(R.id.topOfflineTv);

        topHeadlinesModelList = new ArrayList<>();
        String email = "";
        if (getActivity() != null) {
            spf = getActivity().getSharedPreferences("News", Context.MODE_PRIVATE);
            email = spf.getString("email", "");
        }
        viewModel = new ViewModelProvider(this).get(TopHeadlinesFragmentViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);

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
        viewModel.makeCall();

        adapter = new TopHeadlinesAdapter(getContext(), topHeadlinesModelList, newsDbViewModel, email);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(adapter);
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