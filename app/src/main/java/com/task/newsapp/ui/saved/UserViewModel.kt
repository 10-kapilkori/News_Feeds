package com.task.newsapp.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.newsapp.dao.DAO
import com.task.newsapp.entity.UserEntity

class UserViewModel(private val dao: DAO) : ViewModel() {
    fun insertUser(user: UserEntity) {
        dao.insertUser(user)
    }

    fun fetchUser(email: String, password: String): LiveData<UserEntity> {
        return dao.getUser(email, password)
    }

    fun fetchUsers(): LiveData<List<UserEntity>> {
        return dao.allUsers
    }
}

class UserViewModelFactory(private val dao: DAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}