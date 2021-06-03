package com.task.newsapp.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.List;

public class NewsModel {
    JsonElement status;
    JsonElement totalResults;
    List<ArticlesModel> articles;

    public NewsModel(JsonElement status, JsonElement totalResults, List<ArticlesModel> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public JsonElement getStatus() {
        return status;
    }

    public JsonElement getTotalResults() {
        return totalResults;
    }

    public List<ArticlesModel> getArticles() {
        return articles;
    }
}
