package com.task.newsapp.model;

import com.google.gson.annotations.SerializedName;

public class ArticlesModel {
    @SerializedName("title")
    String title;
    @SerializedName("publishedAt")
    String publishedAt;
    @SerializedName("urlToImage")
    String urlToImage;
    @SerializedName("url")
    String url;
    @SerializedName("author")
    String author;
    @SerializedName("description")
    String description;
    @SerializedName("source")
    SourceModel model;

    public ArticlesModel(String title, String publishedAt, String urlToImage, String url, String author, String description, SourceModel model) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.url = url;
        this.author = author;
        this.description = description;
        this.model = model;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public SourceModel getModel() {
        return model;
    }

    public String getUrl() {
        return url;
    }
}
