package com.example.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.viewModels.LoginViewModel
import com.example.app.viewModels.LoginViewModelContract

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    loginViewModel: LoginViewModelContract = viewModel<LoginViewModel>()
) {
    var username by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Digite seu nome de usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (username.isNotEmpty()) {
                loginViewModel.handleLogin(username) { success, message ->
                    if (success) {
                        loginMessage = message
                        onLoginSuccess(username)
                    } else {
                        loginMessage = "Erro ao fazer login!"
                    }
                }
            } else {
                loginMessage = "O nome de usuário não pode estar vazio!"
            }
        }) {
            Text(text = "Entrar")
        }

        loginMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

