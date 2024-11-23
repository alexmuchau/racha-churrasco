package com.example.racha_churrasco.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginResult: StateFlow<LoginState> = _loginResult

    fun login(username: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username)
            if (user != null) {
                _loginResult.value = LoginState.ExistingUser(user)
            } else {
                val newUser = User(name = "Usuário $username", username = username)
                userRepository.insertUser(newUser)
                _loginResult.value = LoginState.NewUser(newUser)
            }
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        data class ExistingUser(val user: User) : LoginState()
        data class NewUser(val user: User) : LoginState()
    }
}
