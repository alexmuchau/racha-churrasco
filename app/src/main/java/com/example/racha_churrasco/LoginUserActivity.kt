package com.example.racha_churrasco

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.racha_churrasco.screens.LoginScreen
import com.example.racha_churrasco.viewModels.LoginViewModel
import com.example.racha_churrasco.viewModels.LoginViewModelContract
import com.example.racha_churrasco.ui.theme.RachachurrascoTheme

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
    RachachurrascoTheme { // Substitua pelo tema do seu app
        LoginScreen(
            onLoginSuccess = { username ->
                // Apenas simula o sucesso do login no Preview
            },
            loginViewModel = MockLoginViewModel()
        )
    }
}
