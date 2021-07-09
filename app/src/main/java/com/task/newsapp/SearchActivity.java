package com.task.newsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.newsapp.adapter_viewholder.SearchAdapter;
import com.task.newsapp.model.ArticlesModel;
import com.task.newsapp.ui.saved.NewsDbViewModel;
import com.task.newsapp.viewmodel.QueryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView no_result, query_tv;
    ProgressBar searchPb;

    SearchAdapter adapter;
    QueryViewModel queryViewModel;
    NewsDbViewModel newsDbViewModel;
    List<ArticlesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.search_toolbar);
        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.search_recycler_view);
        no_result = findViewById(R.id.default_result);
        searchPb = findViewById(R.id.searchProgressBar);
        query_tv = findViewById(R.id.queryTv);

        setSupportActionBar(toolbar);
        setTitle("");

        list = new ArrayList<>();
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        newsDbViewModel = new ViewModelProvider(this).get(NewsDbViewModel.class);
        adapter = new SearchAdapter(this, list, newsDbViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String q) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                queryViewModel.query(q);
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

            if (list.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.custom_none, null);
                Button okBtn = view.findViewById(R.id.connectionBtn);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                okBtn.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
            adapter.updatedList(list);
        });

        queryViewModel.getErrorMutableLiveData().observe(this, s -> {
            searchPb.setVisibility(View.GONE);
            checkErrors(s);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_close) {
            finishAfterTransition();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkErrors(String error) {
        if (error.contains("No address associated with hostname") ||
                error.contains("Software caused connection abort")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.error_dialog, null);
            Button okBtn = view.findViewById(R.id.connectionBtn);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            okBtn.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } else if (error.contains("timeout")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.error_timeout, null);
            Button okBtn = view.findViewById(R.id.timeoutBtn);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            okBtn.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }
}
