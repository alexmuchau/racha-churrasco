package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.view.CadastroSessionActivity
import com.example.racha_churrasco.view.SessionMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionEntryViewModel(private val context: Context) : ViewModel() {
    private val database = RachaDatabase.getDatabase(context)
    private val sessionDao = database.sessionDao()
    private val userDao = database.userDao()

    suspend fun entrySession(name: String, activeUserId: Int, activeUserName: String): Intent {
        return withContext(Dispatchers.IO) {
            val session = sessionDao.getSessionByName(name)
            val intent: Intent
            if (session == null) {
                // Sessão não encontrada
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Churrasco não encontrado", Toast.LENGTH_SHORT).show()
                }
                intent = Intent(context, CadastroSessionActivity::class.java).apply {
                    putExtra("activeUserId", activeUserId)
                }
            } else {
                // Verifica se o usuário ativo já está associado à sessão
                val userInSession = userDao.getUsersBySession(session.id_session).find { it.id_user == activeUserId }
                if (userInSession == null) {
                    // Adiciona o usuário ativo à sessão
                    userDao.insertUser(
                        com.example.racha_churrasco.models.User(
                            id_user = activeUserId,
                            name = activeUserName, // Nome real do usuário
                            username = "ativo_$activeUserId", // Opcional, pode ser um username real
                            sessionId = session.id_session
                        )
                    )
                }

                // Sessão encontrada e usuário garantido como participante
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Churrasco encontrado", Toast.LENGTH_SHORT).show()
                }

                intent = Intent(context, SessionMainActivity::class.java).apply {
                    putExtra("activeUserId", activeUserId)
                    putExtra("sessionId", session.id_session)
                    putExtra("sessionName", session.name)
                }
            }
            intent
        }
    }
}
