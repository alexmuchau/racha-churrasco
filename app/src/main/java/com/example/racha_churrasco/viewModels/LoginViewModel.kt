package com.example.app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.database.AppDatabase
import com.example.app.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface LoginViewModelContract {
    fun handleLogin(username: String, onResult: (Boolean, String) -> Unit)
}

class LoginViewModel(application: Application) : AndroidViewModel(application), LoginViewModelContract {
    private val userDao = AppDatabase.getInstance(application).userDao()

    override fun handleLogin(username: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUser(username)
            if (user == null) {
                userDao.insertUser(User(username))
                onResult(true, "Usuário criado e logado com sucesso!")
            } else {
                onResult(true, "Bem-vindo de volta, $username!")
            }
        }
    }
}