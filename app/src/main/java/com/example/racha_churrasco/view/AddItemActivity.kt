package com.example.racha_churrasco.view

import android.app.Activity
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
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Item
import kotlinx.coroutines.launch

class AddItemActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddItemScreen()
        }
    }

    @Composable
    fun AddItemScreen() {
        val sessionId = intent.getIntExtra("sessionId", -1)
        var itemName by remember { mutableStateOf("") }
        var itemPrice by remember { mutableStateOf("") }
        var responsible by remember { mutableStateOf("") }

        val itemDao = RachaDatabase.getDatabase(this).itemDao()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Nome do Item") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = itemPrice,
                onValueChange = { itemPrice = it },
                label = { Text("Preço do Item") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = responsible,
                onValueChange = { responsible = it },
                label = { Text("Responsável") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (itemName.isBlank() || itemPrice.isBlank() || responsible.isBlank()) {
                        Toast.makeText(this@AddItemActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    lifecycleScope.launch {
                        try {
                            val price = itemPrice.toDoubleOrNull()
                            if (price == null || price <= 0) {
                                Toast.makeText(this@AddItemActivity, "Preço inválido", Toast.LENGTH_SHORT).show()
                                return@launch
                            }

                            val item = Item(
                                name = itemName,
                                price = price,
                                responsible = responsible,
                                sessionId = sessionId
                            )

                            itemDao.insert(item)
                            setResult(Activity.RESULT_OK) // Retorna o resultado para a Activity anterior
                            Toast.makeText(this@AddItemActivity, "Item adicionado com sucesso", Toast.LENGTH_SHORT).show()
                            finish()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddItemActivity, "Erro ao adicionar item", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Item")
            }
        }
    }
}