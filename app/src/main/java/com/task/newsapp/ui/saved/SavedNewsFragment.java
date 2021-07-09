package com.task.newsapp.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.R;
import com.task.newsapp.adapter_viewholder.SavedNewsAdapter;
import com.task.newsapp.entity.NewsEntity;

import java.util.ArrayList;
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
        savedNewsAdapter = new SavedNewsAdapter(getContext(), list, newsDbViewModel);

        newsDbViewModel.getListLiveData().observe(getViewLifecycleOwner(), newsEntities -> {
            if (newsEntities.size() == 0) {
                noDataTv.setVisibility(View.VISIBLE);
                errorIv.setVisibility(View.VISIBLE);
            } else
                for (int i = 0; i < newsEntities.size(); i++) {
                    list = newsEntities;
                    savedNewsAdapter.savedNews(list);
                }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                newsDbViewModel.deleteNews(savedNewsAdapter.getPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(savedNewsAdapter);

        return view;
    }
}