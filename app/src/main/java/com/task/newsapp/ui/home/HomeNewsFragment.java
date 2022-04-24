package com.task.newsapp.ui.home;

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
import com.task.newsapp.adapter_viewholder.NewsAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;
import com.task.newsapp.ui.saved.NewsViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeNewsFragment extends Fragment {
    private static final String TAG = "HomeNewsFragment";

    RecyclerView homeRecyclerView;
    ProgressBar progressBar;
    ImageView offlineIv;
    TextView offlineTv;

    NewsDbViewModel newsViewModel;
    HomeFragmentViewModel viewModel;
    SharedPreferences spf;
    List<ArticlesModel> list;
    NewsAdapter adapter;
    SimpleDateFormat sdf;
    Calendar cal;
    String from, to;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_news, container, false);

        homeRecyclerView = view.findViewById(R.id.home_recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        offlineIv = view.findViewById(R.id.homeOfflineIv);
        offlineTv = view.findViewById(R.id.homeOfflineTv);

        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        newsViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);

        list = new ArrayList<>();
        String email = "";
        if (getActivity() != null) {
            spf = getActivity().getSharedPreferences("News", Context.MODE_PRIVATE);
            email = spf.getString("email", "");
        }

        adapter = new NewsAdapter(getContext(), list, newsViewModel, email);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            progressBar.setVisibility(View.GONE);
            Log.i(TAG, "onCreateView: hello");
        });

        viewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            checkErrors(s);
            Log.e(TAG, "onCreateView: " + s);
            progressBar.setVisibility(View.GONE);
        });

        sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        viewModel.makeCall(from, to);

        homeRecyclerView.setAdapter(adapter);
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
