package com.task.newsapp.ui.topheadlines;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.newsapp.MainActivity;
import com.task.newsapp.model.NewsModel;
import com.task.newsapp.retrofit.API;
import com.task.newsapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopHeadlinesFragmentViewModel extends ViewModel {
    private static final String TAG = "TopHeadlinesFragmentVie" + "w";

    private final MutableLiveData<NewsModel> modelMutableLiveData;
    private final MutableLiveData<String> errorMutableLiveData;

    public TopHeadlinesFragmentViewModel() {
        modelMutableLiveData = new MutableLiveData<>();
        errorMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<NewsModel> getModelMutableLiveData() {
        return modelMutableLiveData;
    }

    public MutableLiveData<String> getErrorMutableLiveData() {
        return errorMutableLiveData;
    }

    public void makeCall() {
        API api = new RetrofitInstance().getInstance().create(API.class);
        Call<NewsModel> call = api.getTopHeadlines(MainActivity.API_KEY,
                "in",
                "general",
                "30");

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.body() != null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    modelMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorMutableLiveData.postValue(t.getMessage());
            }
        });
    }
}
