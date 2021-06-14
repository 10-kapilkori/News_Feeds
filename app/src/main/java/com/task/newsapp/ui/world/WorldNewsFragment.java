package com.task.newsapp.ui.world;

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
import com.task.newsapp.adapter_viewholder.WorldNewsAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorldNewsFragment extends Fragment {
    private static final String TAG = "WorldNewsFragment";

    WorldFragmentViewModel worldFragmentViewModel;
    RecyclerView worldRecyclerView;
    ImageView offlineIv;
    TextView offlineTv;
    ProgressBar worldNewsProgressBar;
    List<ArticlesModel> list;
    SwipeRefreshLayout swipe;
    WorldNewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_world_news, container, false);

        offlineIv = view.findViewById(R.id.worldOfflineIv);
        offlineTv = view.findViewById(R.id.worldOfflineTv);
        worldNewsProgressBar = view.findViewById(R.id.worldProgressBar);
        worldRecyclerView = view.findViewById(R.id.world_recyclerView);
        swipe = view.findViewById(R.id.world_swipe_refresh);
        worldFragmentViewModel = new ViewModelProvider(this).get(WorldFragmentViewModel.class);
        list = new ArrayList<>();
        adapter = new WorldNewsAdapter(getContext(), list);

        worldFragmentViewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), topHeadlines -> {
            list = topHeadlines.getArticles();
            adapter.updatedList(list);
            worldNewsProgressBar.setVisibility(View.GONE);
        });

        worldFragmentViewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            worldNewsProgressBar.setVisibility(View.GONE);
            checkErrors(s);
        });

        String from, to;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        worldFragmentViewModel.makeCall(from, to);

        swipe.setOnRefreshListener(() -> {
            worldFragmentViewModel.makeCall(from, to);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        worldRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        worldRecyclerView.setAdapter(adapter);

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