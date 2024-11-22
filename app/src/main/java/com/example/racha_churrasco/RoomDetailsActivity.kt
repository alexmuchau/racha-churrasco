package com.example.racha_churrasco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha_churrasco.viewModels.RoomDetailsViewModel

class RoomDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomName = intent.getStringExtra("ROOM_NAME") ?: "Sala Desconhecida"

        setContent {
            val viewModel: RoomDetailsViewModel = viewModel()
            LaunchedEffect(Unit) {
                viewModel.loadItems(roomName)
            }

            RoomDetailsScreen(roomName = roomName, viewModel = viewModel)
        }
    }
}
