package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.view.CadastroActivity
import com.example.racha_churrasco.view.LoginActivity
import com.example.racha_churrasco.view.SessionEntryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(private val context: Context) : ViewModel() {
    val userDao = RachaDatabase.getDatabase(context).userDao()

    suspend fun loginUser(username: String): Intent {
        // Coroutine to handle database query
        return withContext(Dispatchers.IO) {
            val user = userDao.getUserByUsername(username)
            val intent: Intent
            if (user == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                }
                intent = Intent(context, CadastroActivity::class.java).apply {
                    putExtra("username", username)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Usuário encontrado - ${user.name}", Toast.LENGTH_SHORT).show()
                }

                intent = Intent(context, SessionEntryActivity::class.java).apply {
                    putExtra("activeUserId", user.id_user)
                }
            }
            intent
        }
    }
}
