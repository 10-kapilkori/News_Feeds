package com.task.newsapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.entity.UserEntity;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertNews(NewsEntity entity);

    @Insert
    void insertUser(UserEntity users);

    @Delete
    void deleteNews(NewsEntity entity);

    @Query("SELECT * FROM news_table_1")
    LiveData<List<NewsEntity>> getNews();

//    @Query("SELECT * FROM news_table_1 WHERE email=:email")
//    LiveData<List<NewsEntity>> getUserSpecificNews(String email);

    @Query("SELECT * FROM users WHERE email=:email AND password=:password")
    LiveData<UserEntity> getUser(String email, String password);

    @Query("SELECT * FROM users")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("DELETE FROM news_table_1")
    void deleteAllNews();
}
