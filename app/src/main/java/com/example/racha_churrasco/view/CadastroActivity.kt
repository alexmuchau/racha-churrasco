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
        var name by remember { mutableStateOf("") }
        val context = LocalContext.current
        val userDao = RachaDatabase.getDatabase(context).userDao()

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

                    // Coroutine para salvar o usuário no banco de dados
                    (context as CadastroActivity).lifecycleScope.launch {
                        val username = intent.getStringExtra("username") ?: ""

                        // Criar e salvar o usuário no banco de dados
                        val newUser = User(name = name, username = username)
                        userDao.insertUser(newUser)

                        Toast.makeText(context, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

                        // Navegar para a próxima tela
                        val intent = Intent(context, LoginActivity::class.java)
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
