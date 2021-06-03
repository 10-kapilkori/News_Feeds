package com.task.newsapp.retrofit;

import com.task.newsapp.model.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    @GET("v2/everything")
    Call<NewsModel> getNews(
            @Query("apiKey") String apiKey,
            @Query("q") String q,
            @Query("from") String from,
            @Query("to") String to,
            @Query("pageSize") String pageSize,
            @Query("sortBy") String sortBy
    );

    @GET("v2/top-headlines")
    Call<NewsModel> getTopHeadlines(
            @Query("apiKey") String apiKey,
            @Query("country") String country,
            @Query("category") String category,
            @Query("pageSize") String pageSize
    );

    @GET("v2/everything")
    Call<NewsModel> getWorldNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("from") String from,
            @Query("to") String to,
            @Query("sortBy") String sortBy,
            @Query("pageSize") String limit
    );

    @GET("v2/everything")
    Call<NewsModel> getPoliticsNews(
            @Query("q") String q,
            @Query("apiKey") String apiKey,
            @Query("from") String from,
            @Query("to") String to,
            @Query("sortBy") String sortBy,
            @Query("pageSize") String pageSize,
            @Query("language") String language
    );

    @GET("v2/top-headlines")
    Call<NewsModel> getEntertainmentNews(
            @Query("category") String category,
            @Query("apiKey") String apiKey,
            @Query("language") String language,
            @Query("pageSize") String pageSize
    );

    @GET("v2/top-headlines")
    Call<NewsModel> getTechnologyNews(
            @Query("category") String category,
            @Query("apiKey") String apiKey,
            @Query("language") String language,
            @Query("pageSize") String pageSize
    );
}
