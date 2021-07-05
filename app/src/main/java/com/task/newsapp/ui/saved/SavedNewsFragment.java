package com.task.newsapp.ui.saved;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.SavedNewsAdapter;
import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.model.ArticlesModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedNewsFragment extends Fragment {
    private static final String TAG = "SavedNewsFragment";

    RecyclerView recyclerView;
    TextView noDataTv;
    ImageView errorIv;

    List<NewsEntity> list;
    SavedNewsAdapter savedNewsAdapter;
    NewsDbViewModel dbViewModel;
    NewsDbViewModel newsDbViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.saved_news_fragment, container, false);
        recyclerView = view.findViewById(R.id.savedRecyclerView);
        noDataTv = view.findViewById(R.id.savedNoneTv);
        errorIv = view.findViewById(R.id.savedErrorIv);

        dbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);

        list = new ArrayList<>();
        savedNewsAdapter = new SavedNewsAdapter(getContext(), list);

        newsDbViewModel.getListLiveData().observe(getViewLifecycleOwner(), newsEntities -> {
            if (newsEntities.size() == 0) {
                noDataTv.setVisibility(View.VISIBLE);
                errorIv.setVisibility(View.VISIBLE);
            } else
                for (int i = 0; i < newsEntities.size(); i++) {
                    list = newsEntities;
                    Collections.reverse(list);
                    savedNewsAdapter.savedNews(list);
                }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(savedNewsAdapter);

        return view;
    }
}