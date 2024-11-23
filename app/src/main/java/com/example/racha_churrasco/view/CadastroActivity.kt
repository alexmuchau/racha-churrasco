package com.example.racha_churrasco.view

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
import com.example.racha_churrasco.components.CompletedTextField
import com.example.racha_churrasco.components.CustomTitle
import com.example.racha_churrasco.viewmodels.CadastroViewModel
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
        val username = intent.getStringExtra("username") ?: ""

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CustomTitle("Cadastro")
            CompletedTextField("username", username)
            
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
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cadastrar")
            }
        }
    }
}
