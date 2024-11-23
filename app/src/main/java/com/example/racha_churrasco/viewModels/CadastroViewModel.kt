package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.view.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CadastroViewModel(private val context: Context) : ViewModel() {
    val userDao = RachaDatabase.getDatabase(context).userDao()

    suspend fun cadastroUser(name: String, username: String): Intent {
        // Coroutine to handle database query
        return withContext(Dispatchers.IO) {
            val intent: Intent

            val newUser = User(name = name, username = username)
            userDao.insertUser(newUser)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
            }

            intent = Intent(context, LoginActivity::class.java)
            intent
        }
    }
}
