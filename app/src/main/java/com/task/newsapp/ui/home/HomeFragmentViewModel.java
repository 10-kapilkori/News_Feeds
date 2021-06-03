package com.task.newsapp.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.newsapp.model.NewsModel;
import com.task.newsapp.retrofit.API;
import com.task.newsapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "HomeFragmentViewModel";

    private final MutableLiveData<NewsModel> mutableLiveData;
    private final MutableLiveData<String> errorMutableLiveData;

    public HomeFragmentViewModel() {
        mutableLiveData = new MutableLiveData<>();
        errorMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<NewsModel> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<String> getErrorMutableLiveData() {
        return errorMutableLiveData;
    }

    public void makeCall(String from, String to) {
        API api = new RetrofitInstance().getInstance().create(API.class);
        Call<NewsModel> call = api.getNews("a69420c4ab5a41359149947df1bf1340", "india and world and sports", from, to, "30", "publishedAt");

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                if (newsModel != null) {
                    mutableLiveData.postValue(newsModel);
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                errorMutableLiveData.postValue(t.getMessage());
            }
        });
    }
}
