package com.task.newsapp.ui.politics;

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
import com.task.newsapp.adapter_viewholder.PoliticsAdapter;
import com.task.newsapp.model.ArticlesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PoliticsNewsFragment extends Fragment {
    private static final String TAG = "PoliticsNewsFragment";

    RecyclerView recyclerView;
    ProgressBar politicsProgressBar;
    List<ArticlesModel> list;
    PoliticsAdapter adapter;
    SwipeRefreshLayout swipe;
    PoliticsFragmentViewModel politicsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_politics_news, container, false);
        recyclerView = view.findViewById(R.id.politics_recyclerView);
        politicsProgressBar = view.findViewById(R.id.politicsProgressBar);
        swipe = view.findViewById(R.id.politics_swipe_refresh);
        list = new ArrayList<>();
        politicsViewModel = new ViewModelProvider(this).get(PoliticsFragmentViewModel.class);
        adapter = new PoliticsAdapter(getContext(), list);

        politicsViewModel.getModelMutableLiveData().observe(getViewLifecycleOwner(), newsModel -> {
            list = newsModel.getArticles();
            adapter.updatedList(list);
            politicsProgressBar.setVisibility(View.GONE);
        });

        politicsViewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            Log.i(TAG, "onCreateView: " + s);
            politicsProgressBar.setVisibility(View.GONE);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        String from, to;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        to = sdf.format(Calendar.getInstance().getTime());
        from = String.valueOf(cal.getTime());

        swipe.setOnRefreshListener(() -> {
            politicsViewModel.makeCall(from, to);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Handler().postDelayed(() -> {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                swipe.setRefreshing(false);
            }, 2000);
        });

        politicsViewModel.makeCall(from, to);

        return view;
    }
}