package com.task.newsapp.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.task.newsapp.dao.DAO;
import com.task.newsapp.database.NewsDb;
import com.task.newsapp.entity.NewsEntity;

import java.util.List;

public class NewsRepo {
    DAO dao;
    LiveData<List<NewsEntity>> listLiveData;

    public NewsRepo(Application application) {
        dao = NewsDb.getDb(application.getApplicationContext()).getDao();
        listLiveData = dao.getNews();
    }

    public void insertNews(NewsEntity entity) {
        new InsertNewsAsyncTask(dao).execute(entity);
    }

    public void deleteNews(NewsEntity entity) {
        new DeleteNewsAsyncTask(dao).execute(entity);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    public LiveData<List<NewsEntity>> getListLiveData() {
        return listLiveData;
    }

    private static class InsertNewsAsyncTask extends AsyncTask<NewsEntity, Void, Void> {
        DAO dao;

        public InsertNewsAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NewsEntity... newsEntities) {
            dao.insertNews(newsEntities[0]);
            return null;
        }
    }

    private static class DeleteNewsAsyncTask extends AsyncTask<NewsEntity, Void, Void> {
        DAO dao;

        public DeleteNewsAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NewsEntity... newsEntities) {
            dao.deleteNews(newsEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        DAO dao;

        public DeleteAllAsyncTask(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllNews();
            return null;
        }
    }
}
