package com.example.racha_churrasco.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.racha_churrasco.components.CustomTitle
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionEntryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SessionEntryScreen()
        }
    }

    @Composable
    fun SessionEntryScreen() {
        val sessionDao = RachaDatabase.getDatabase(this).sessionDao()
        val userDao = RachaDatabase.getDatabase(this).userDao()

        val activeUserId = intent.getIntExtra("activeUserId", 0) // Nome real do usuário
        var sessionName by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTitle("Entre no seu churrasco!")

            TextField(
                value = sessionName,
                onValueChange = { sessionName = it },
                label = { Text("Nome da Sessão") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (sessionName.isBlank()) {
                        Toast.makeText(this@SessionEntryActivity, "Digite o nome da sessão", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            val session = sessionDao.getSessionByName(sessionName)

                            if (session == null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@SessionEntryActivity,
                                        "Sessão não encontrada. Crie uma nova!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val intent = Intent(this@SessionEntryActivity, CadastroSessionActivity::class.java).apply {
                                    putExtra("activeUserName", activeUserId)
                                    putExtra("sessionName", sessionName)
                                }
                                startActivity(intent)
                            } else {
                                // Verifica se o usuário já está associado à sessão
                                val userInSession = userDao.getUsersBySession(session.id_session)
                                    .find { it.id_user == activeUserId }

                                if (userInSession == null) {
                                    // Adiciona o usuário à sessão
                                    userDao.updateUser(
                                        user_id = activeUserId,
                                        session_id = session.id_session
                                    )
                                }

                                val intent = Intent(this@SessionEntryActivity, SessionMainActivity::class.java).apply {
                                    putExtra("sessionId", session.id_session)
                                    putExtra("sessionName", session.name)
                                    putExtra("activeUserId", activeUserId)
                                }
                                withContext(Dispatchers.Main) {
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar na Sessão")
            }
        }
    }
}