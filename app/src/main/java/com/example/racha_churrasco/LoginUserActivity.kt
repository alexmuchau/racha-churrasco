package com.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.screens.LoginScreen
import com.example.app.ui.theme.AppTheme
import com.example.app.viewModels.LoginViewModel
import com.example.app.viewModels.LoginViewModelContract

class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginSuccess = { username ->
                        Toast.makeText(this, "Bem-vindo(a), $username!", Toast.LENGTH_SHORT).show()
                        // Redirecionar para próxima tela, se necessário
                    },
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}

class MockLoginViewModel : LoginViewModelContract {
    override fun handleLogin(username: String, onResult: (Boolean, String) -> Unit) {
        // Simula uma resposta bem-sucedida
        onResult(true, "Bem-vindo(a), $username!")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    AppTheme { // Substitua pelo tema do seu app
        LoginScreen(
            onLoginSuccess = { username ->
                // Apenas simula o sucesso do login no Preview
            },
            loginViewModel = MockLoginViewModel()
        )
    }
}
