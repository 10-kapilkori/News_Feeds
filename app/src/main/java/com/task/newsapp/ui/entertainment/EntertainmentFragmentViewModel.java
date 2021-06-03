package com.task.newsapp.ui.entertainment;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.newsapp.model.NewsModel;
import com.task.newsapp.retrofit.API;
import com.task.newsapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntertainmentFragmentViewModel extends ViewModel {
    private static final String TAG = "EntertainmentViewModel";

    private final MutableLiveData<NewsModel> modelMutableLiveData;
    private final MutableLiveData<String> errorMutableLiveData;

    public EntertainmentFragmentViewModel() {
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
        Call<NewsModel> call = api.getEntertainmentNews("Entertainment", "a69420c4ab5a41359149947df1bf1340", "en", "30");

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.body() != null) {
                    modelMutableLiveData.postValue(response.body());
                    Log.i(TAG, "onResponse: " + response.body());
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
