package com.task.newsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.task.newsapp.dao.DAO;
import com.task.newsapp.entity.NewsEntity;
import com.task.newsapp.entity.UserEntity;

@Database(entities = {NewsEntity.class, UserEntity.class}, exportSchema = false, version = 4)
public abstract class NewsDb extends RoomDatabase {
    public static final String db_name = "News_Database";
    public static NewsDb db;

    public static synchronized NewsDb getDb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, NewsDb.class, db_name)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return db;
    }

    public abstract DAO getDao();
}
