package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.view.CadastroActivity
import com.example.racha_churrasco.view.CadastroSessionActivity
import com.example.racha_churrasco.view.SessionEntryActivity
import com.example.racha_churrasco.view.SessionMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SessionEntryViewModel(private val context: Context) : ViewModel() {
    val sessionDao = RachaDatabase.getDatabase(context).sessionDao()

    suspend fun entrySession(name: String, activeUserId: Int): Intent {
        return withContext(Dispatchers.IO) {
            val session = sessionDao.getSessionByName(name)
            val intent: Intent
            if (session == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Churrasco não encontrado", Toast.LENGTH_SHORT).show()
                }
                intent = Intent(context, CadastroSessionActivity::class.java).apply{
                    putExtra("activeUserId", activeUserId)
                }
            } else {
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
