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
        var sessionName by remember { mutableStateOf("") }
        val context = LocalContext.current
        val sessionDao = RachaDatabase.getDatabase(context).sessionDao()
        val activeUserId = intent.getIntExtra("activeUserId", 99)

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
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

                    // Coroutine para verificar o ID da sessão no banco
                    (context as SessionEntryActivity).lifecycleScope.launch {
                        val session = sessionDao.getSessionByName(sessionName)


                        if (session == null) {
                            Toast.makeText(context, "Churrasco não encontrada", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, CadastroSessionActivity::class.java)
                            intent.putExtra("activeUserId", activeUserId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(context, "Entrando na Churrasco: ${session.id_session}", Toast.LENGTH_SHORT).show()

                            // Redireciona para a próxima tela (Ex.: Tela Principal da Sessão)
                            val intent = Intent(context, SessionMainActivity::class.java)
                            intent.putExtra("activeUserId", activeUserId)
                            intent.putExtra("sessionId", session.id_session)
                            intent.putExtra("sessionName", session.name)
                            startActivity(intent)
                            finish() // Finaliza a Activity atual
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }
        }
    }
}
