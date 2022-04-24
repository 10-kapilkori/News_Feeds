package com.task.newsapp.ui.world;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.WorldNewsAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorldNewsFragment extends Fragment {
    private static final String TAG = "WorldNewsFragment";

    RecyclerView worldRecyclerView;
    ProgressBar worldNewsProgressBar;
    View view;

    List<ArticlesModel> list;
    SharedPreferences spf;
    WorldNewsAdapter adapter;
    String from, to;
    WorldFragmentViewModel worldFragmentViewModel;
    NewsDbViewModel newsDbViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_world_news, container, false);
        worldNewsProgressBar = view.findViewById(R.id.worldProgressBar);
        worldRecyclerView = view.findViewById(R.id.world_recyclerView);

        list = new ArrayList<>();
        String email = "";
        if (getActivity() != null) {
            spf = getActivity().getSharedPreferences("News", Context.MODE_PRIVATE);
            email = spf.getString("email", "");
        }
        worldFragmentViewModel = new ViewModelProvider(this).get(WorldFragmentViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        adapter = new WorldNewsAdapter(getContext(), list, newsDbViewModel, email);

        worldFragmentViewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), topHeadlines -> {
            list = topHeadlines.getArticles();
            adapter.updatedList(list);
            worldNewsProgressBar.setVisibility(View.GONE);
        });

        worldFragmentViewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            worldNewsProgressBar.setVisibility(View.GONE);
            checkErrors(s);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        worldFragmentViewModel.makeCall(from, to);

        worldRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        worldRecyclerView.setAdapter(adapter);

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
