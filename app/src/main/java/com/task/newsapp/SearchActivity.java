package com.task.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.viewmodel.QueryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    SearchView searchView;
    Toolbar toolbar;
    TextView no_result, query_tv;
    ProgressBar searchPb;
    QueryViewModel queryViewModel;
    List<ArticlesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.search_toolbar);
        searchView = findViewById(R.id.search_bar);
        no_result = findViewById(R.id.default_result);
        searchPb = findViewById(R.id.searchProgressBar);
        query_tv = findViewById(R.id.queryTv);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        list = new ArrayList<>();

        setSupportActionBar(toolbar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String q) {
                Toast.makeText(SearchActivity.this, q, Toast.LENGTH_SHORT).show();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

//                queryViewModel.query(q);
                searchPb.setVisibility(View.VISIBLE);
                query_tv.setVisibility(View.VISIBLE);

                query_tv.setText("Searched for: " + q);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        queryViewModel.getModelMutableLiveData().observe(this, newsModel -> {
            list = newsModel.getArticles();
            searchPb.setVisibility(View.GONE);
        });

        setTitle("");
    }
}
