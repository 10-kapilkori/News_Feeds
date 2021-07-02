package com.task.newsapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.task.newsapp.entity.NewsEntity;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertNews(NewsEntity entity);

    @Delete
    void deleteNews(NewsEntity entity);

    @Query("SELECT * FROM news_table_1")
    LiveData<List<NewsEntity>> getNews();

    @Query("DELETE FROM news_table_1")
    void deleteAllNews();
}
