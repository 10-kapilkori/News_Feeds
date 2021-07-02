package com.task.newsapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.repo.NewsRepo;

import java.util.List;

public class NewsDbViewModel extends AndroidViewModel {

    NewsRepo newsRepo;
    LiveData<List<NewsEntity>> listLiveData;

    public NewsDbViewModel(@NonNull Application application) {
        super(application);
        newsRepo = new NewsRepo(application);
        listLiveData = newsRepo.getListLiveData();
    }

    public void insertNews(NewsEntity entity) {
        newsRepo.insertNews(entity);
    }

    public void deleteNews(NewsEntity entity) {
        newsRepo.deleteNews(entity);
    }

    public void deleteAll() {
        newsRepo.deleteAll();
    }

    public LiveData<List<NewsEntity>> getListLiveData() {
        return listLiveData;
    }
}
