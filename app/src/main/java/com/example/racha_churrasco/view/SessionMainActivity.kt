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

class SessionMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SessionEntryScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SessionEntryScreen() {
        val sessionId = intent.getStringExtra("sessionId")
        val sessionName = intent.getStringExtra("sessionName")
        val activeUserId = intent.getIntExtra("activeUserId", 99)
        val context = LocalContext.current
        val sessionDao = RachaDatabase.getDatabase(context).sessionDao()

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("Nome da session $sessionName")
            Text("Id do usuário ativo $activeUserId")
        }
    }
}
