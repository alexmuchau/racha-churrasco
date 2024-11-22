package com.example.racha_churrasco.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.racha_churrasco.database.AppDatabase
import com.example.racha_churrasco.models.User
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