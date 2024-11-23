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

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen() {
        var username by remember { mutableStateOf("") }
        val context = LocalContext.current
        val userDao = RachaDatabase.getDatabase(context).userDao()

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Username") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (username.isBlank()) {
                        Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Coroutine to handle database query
                    (context as LoginActivity).lifecycleScope.launch {
                        val user = userDao.getUserByUsername(username)
                        if (user == null) {
                            Toast.makeText(context, "Usuário não encontrado", Toast.LENGTH_SHORT).show()

                            // Example: Navigate to the next screen
                            val intent = Intent(context, CadastroActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(context, "Usuário encontrado - ${user.name}", Toast.LENGTH_SHORT).show()

                            val intent = Intent(context, SessionEntryActivity::class.java)
                            intent.putExtra("activeUserId", user.id_user)
                            startActivity(intent)
                            finish()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }
        }
    }
}
