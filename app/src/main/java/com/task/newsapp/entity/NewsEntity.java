package com.task.newsapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_table_1")
public class NewsEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    String title;
    @ColumnInfo(name = "desc")
    String description;
    @ColumnInfo(name = "imgUrl")
    String imgUrl;
    @ColumnInfo(name = "url")
    String url;
    @ColumnInfo(name = "time")
    String time;
    @ColumnInfo(name = "author")
    String author;
    @ColumnInfo(name = "email")
    public String email;

    public NewsEntity(String title, String description, String imgUrl, String url, String time, String author, String email) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.url = url;
        this.time = time;
        this.author = author;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getTime() {
        return time;
    }

    public String getAuthor() {
        return author;
    }
}
