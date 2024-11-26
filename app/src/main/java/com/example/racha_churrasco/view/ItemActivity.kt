package com.example.racha_churrasco.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha_churrasco.components.CustomTitle
import com.example.racha_churrasco.daos.ItemWithResponsible
import com.example.racha_churrasco.models.Item
import com.example.racha_churrasco.view.SessionEntryActivity
import com.example.racha_churrasco.viewmodels.ItemViewModel
import kotlinx.coroutines.launch

class ItemActivity : ComponentActivity() {
    private var sessionId: Int = -1

    private var activeUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionId = intent.getIntExtra("sessionId", -1)

        activeUserId = intent.getIntExtra("activeUserId", -1)

        setContent {
            ItemScreen()
        }
    }

    @Composable
    fun ItemScreen() {
        val context = LocalContext.current
        val itemViewModel = ItemViewModel(context)
        val focusManager = LocalFocusManager.current

        var nomeItem by remember { mutableStateOf("") }
        var valorItem by remember { mutableStateOf("") }
        var id by remember { mutableStateOf(0) }
        var textoBotao by remember { mutableStateOf("Adicionar") }
        var modoEditar by remember { mutableStateOf(false) }
        var items by remember { mutableStateOf(emptyList<ItemWithResponsible>()) }

        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                items = itemViewModel.getItemsByResponsible(activeUserId)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            IconButton(
                onClick = {
                    // Criar o Intent para enviar dados para a SessionMainActivity
//                    val intent = Intent(this@ItemActivity, SessionMainActivity::class.java).apply {
//                        putExtra("sessionId", sessionId)
//                        putExtra("activeUserId", activeUserId)
//                    }
//
//                    startActivity(intent)
                    setResult(Activity.RESULT_OK)
                    finish()
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }

            TextField(value = nomeItem,
                onValueChange = { nomeItem = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nome do Item") })

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = valorItem.toString(),
                onValueChange = { valorItem = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Valor do Item") }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    valorItem = valorItem.replace(",", ".")

                    lifecycleScope.launch {
                        if (modoEditar) { // Verificar se vamos editar o item
                            itemViewModel.updateItem(id, nomeItem, valorItem.toDouble())
                            items = itemViewModel.getItemsByResponsible(activeUserId)

                            modoEditar = false
                            textoBotao = "Salvar"

                            Toast.makeText(
                                context,
                                "Item $nomeItem atualizado com sucesso.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else { // Senão, vamos salvar o item
                            itemViewModel.salvarItem(nomeItem, valorItem.toDouble(), activeUserId, sessionId)
                            items = itemViewModel.getItemsByResponsible(activeUserId)

                            Toast.makeText(
                                context,
                                "Item criado com sucesso.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    nomeItem = ""
                    valorItem = ""
                    // Limpar o foco do formulário
                    focusManager.clearFocus()
                }) {
                Text(text = textoBotao)
            }

            Spacer(modifier = Modifier.height(15.dp))
            CustomTitle(text = "Seus Items")
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(items) { item ->
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (
                            modifier = Modifier
                                .width(200.dp)
                                .align(Alignment.CenterVertically)
                            ,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ){
                            Text(
                                text = item.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "R$${item.price}",
                                fontSize = 18.sp
                            )
                        }

                        Column (
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                ,
                                contentPadding = PaddingValues(vertical = 4.dp),
                                onClick = {
                                    lifecycleScope.launch {
                                        itemViewModel.deleteItem(item.id_item)
                                        items = itemViewModel.getItemsByResponsible(activeUserId)
                                    }
                                }
                            ) {
                                Text(
                                    fontSize = 10.sp,
                                    text = "Excluir"
                                )
                            }

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                ,
                                contentPadding = PaddingValues(vertical = 4.dp),
                                onClick = {
                                    modoEditar = true

                                    id = item.id_item
                                    nomeItem = item.name
                                    valorItem = item.price.toString()

                                    textoBotao = "Editar"
                                }
                            ) {
                                Text(
                                    fontSize = 10.sp,
                                    text = "Editar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ExcluirItem(onConfirm: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Confirmar exclusão") },
            text = { Text(text = "Tem certeza que deseja excluir este item?") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(text = "Sim, excluir")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Não, cancelar")
                }
            })
    }
}