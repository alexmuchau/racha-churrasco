package com.example.racha_churrasco.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.viewmodels.CadastroViewModel
import com.example.racha_churrasco.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class CadastroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CadastroScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CadastroScreen() {
        val context = LocalContext.current
        var cadastroViewModel = CadastroViewModel(context)

        var name by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "username: ${ intent.getStringExtra("username") ?: "" }")
            
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nome") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, "Por favor, insira seu nome", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val username = intent.getStringExtra("username") ?: ""

                    lifecycleScope.launch {
                        val intent = cadastroViewModel.cadastroUser(name, username)
                        startActivity(intent)
                        finish()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cadastrar")
            }
        }
    }
}
