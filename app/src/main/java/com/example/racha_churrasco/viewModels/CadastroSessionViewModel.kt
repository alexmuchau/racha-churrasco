package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Session
import com.example.racha_churrasco.view.SessionEntryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CadastroSessionViewModel(private val context: Context) : ViewModel() {
    val sessionDao = RachaDatabase.getDatabase(context).sessionDao()

    suspend fun cadastroSession(name: String, activeUserId: Int): Intent {
        // Coroutine to handle database query
        return withContext(Dispatchers.IO) {
            val intent: Intent

            val newSession = Session(name = name)
            sessionDao.insertSession(newSession)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Cadastro do churrasco realizado com sucesso", Toast.LENGTH_SHORT).show()
            }

            intent = Intent(context, SessionEntryActivity::class.java).apply {
                putExtra("activeUserId", activeUserId)
            }
            intent
        }
    }
}
