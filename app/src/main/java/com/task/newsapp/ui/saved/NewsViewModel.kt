package com.task.newsapp.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.newsapp.dao.DAO
import com.task.newsapp.entity.NewsEntity

public class NewsViewModel(private val dao: DAO) : ViewModel() {
    fun insertNews(entity: NewsEntity) {
        dao.insertNews(entity)
    }

    fun fetchAllNews(): LiveData<List<NewsEntity>> {
        return dao.news
    }

    fun fetchNewsWithUserSpecific(email: String): LiveData<List<NewsEntity>> {
        return dao.getUserSpecificNews(email)
    }

    fun deleteNews(newsEntity: NewsEntity) {
        dao.deleteNews(newsEntity)
    }
}

class NewsViewModelFactory(private val dao: DAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}