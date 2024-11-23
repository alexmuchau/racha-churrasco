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
import com.example.racha_churrasco.components.CustomTitle
import com.example.racha_churrasco.viewmodels.SessionEntryViewModel
import kotlinx.coroutines.launch

class SessionEntryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SessionEntryScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SessionEntryScreen() {
        val context = LocalContext.current
        var sessionEntryViewModel = SessionEntryViewModel(context)

        var sessionName by remember { mutableStateOf("") }
        val activeUserId = intent.getIntExtra("activeUserId", 99)

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            CustomTitle("Login de Churrasco")
            Text("Usuário ativo: $activeUserId")
            TextField(
                value = sessionName,
                onValueChange = { sessionName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nome do Churrasco") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (sessionName.isBlank()) {
                        Toast.makeText(context, "Por favor, insira o Nome do Churrasco", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    lifecycleScope.launch {
                        val intent = sessionEntryViewModel.entrySession(sessionName, activeUserId)
                        startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }
        }
    }
}
