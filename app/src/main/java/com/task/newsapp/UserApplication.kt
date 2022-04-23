package com.task.newsapp

import android.app.Application
import com.task.newsapp.database.NewsDb

class UserApplication : Application() {
    val db: NewsDb by lazy {
        NewsDb.getDb(this)
    }
}