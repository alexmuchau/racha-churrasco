package com.example.racha_churrasco

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class LoginSessionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginSessionScreen(onLogin = { roomName ->
                val intent = Intent(this, RoomDetailsActivity::class.java)
                intent.putExtra("ROOM_NAME", roomName)
                startActivity(intent)
            })
        }
    }
}

@Composable
fun LoginSessionScreen(onLogin: (String) -> Unit) {
    var roomName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Digite o nome da sala",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = roomName,
            onValueChange = { roomName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nome da sala") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (roomName.isNotEmpty()) {
                    onLogin(roomName)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}

@Preview
@Composable
fun LoginSessionScreenPreview() {
    LoginSessionScreen(onLogin = {})
}
