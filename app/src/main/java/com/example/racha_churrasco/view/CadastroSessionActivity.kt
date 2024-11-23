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
import com.example.racha_churrasco.models.Session
import kotlinx.coroutines.launch

class CadastroSessionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CadastroSessionScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CadastroSessionScreen() {
        var sessionName by remember { mutableStateOf("") }
        val context = LocalContext.current
        val sessionDao = RachaDatabase.getDatabase(context).sessionDao()
        val activeUserId = intent.getIntExtra("activeUserId", 99)

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TextField(
                value = sessionName,
                onValueChange = { sessionName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nome da Sessão") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (sessionName.isBlank()) {
                        Toast.makeText(context, "Por favor, insira o nome da sessão", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Coroutine para salvar a sessão no banco de dados
                    (context as CadastroSessionActivity).lifecycleScope.launch {
                        val newSession = Session(name = sessionName)
                        val sessionId = sessionDao.insertSession(newSession)

                        Toast.makeText(context, "Sessão criada com sucesso!", Toast.LENGTH_SHORT).show()

                        // Redirecionar para a tela de entrada na sessão
                        val intent = Intent(context, SessionEntryActivity::class.java)
                        intent.putExtra("activeUserId", activeUserId)
                        startActivity(intent)
                        finish()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cadastrar Sessão")
            }
        }
    }
}
