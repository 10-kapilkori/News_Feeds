package com.task.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsModel {
    @SerializedName("status")
    String status;
    @SerializedName("totalResults")
    String totalResults;
    @SerializedName("articles")
    List<ArticlesModel> articles;

    public NewsModel(String status, String totalResults, List<ArticlesModel> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public List<ArticlesModel> getArticles() {
        return articles;
    }
}
