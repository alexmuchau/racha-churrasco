package com.example.racha_churrasco.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racha_churrasco.viewmodels.ItemViewModel

class ItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemScreen()
        }
    }

    @Composable
    fun ItemScreen(){

        var nomeItem by remember { mutableStateOf("") }
        var valorItem by remember { mutableStateOf("") }
        var id by remember { mutableStateOf(0) }
        var textoBotao by remember { mutableStateOf("Salvar") }
        var modoEditar by remember { mutableStateOf(false) }

        val itemViewModel: ItemViewModel = viewModel()
        val listaItems by itemViewModel.listaItems
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current

        // Variável de estado para exibir ou ocultar a caixa de diálogo
        var mostrarCaixaDialogo by remember { mutableStateOf(false) }

        if (mostrarCaixaDialogo) {
            ExcluirItem(onConfirm = {
                itemViewModel.excluirItem(id)
                mostrarCaixaDialogo = false
            },
                onDismiss = { mostrarCaixaDialogo = false } ) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(text = "Editar Item", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)

            Spacer(modifier = Modifier.height(15.dp))

            TextField(value = nomeItem,
                onValueChange = { nomeItem = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nome do Item") })

            Spacer(modifier = Modifier.height(15.dp))

            TextField(value = valorItem.toString(),
                onValueChange = { valorItem = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Valor do Item") })

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    // Criar o Intent para enviar dados para a SessionMainActivity
                    val intent = Intent()
                    // Aqui, adicione os dados dos itens modificados
                    intent.putExtra("itemName", nomeItem)
                    intent.putExtra("itemPrice", valorItem)

                    // Defina o resultado de volta para a Activity chamadora
                    setResult(RESULT_OK, intent)
                    finish() // Finaliza a Activity e retorna à Activity anterior
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Voltar")
            }


            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {

                    var retorno = ""
                    if (modoEditar) { // Verificar se vamos editar o item
                        retorno = itemViewModel.atualizarItem(id, nomeItem, valorItem)
                        modoEditar = false
                        textoBotao = "Salvar"
                    } else { // Senão, vamos salvar o item
                        retorno = itemViewModel.salvarItem(nomeItem, valorItem)
                    }

                    Toast.makeText(
                        context,
                        retorno,
                        Toast.LENGTH_LONG
                    ).show()

                    // Limpar os campos de formulário
                    nomeItem = ""
                    valorItem = ""
                    // Limpar o foco do formulário
                    focusManager.clearFocus()
                }) {
                Text(text = textoBotao)
            }

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                items(listaItems) { item ->
                    Text(text = "${item.name} (${item.price})",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Button(onClick = {
                            id = item.id_item
                            mostrarCaixaDialogo = true
                        }) {
                            Text(text = "Excluir")
                        }

                        Button(onClick = {
                            modoEditar = true
                            // Copiar dados do item atual
                            id = item.id_item
                            nomeItem = item.name
                            valorItem = item.price.toString()
                            textoBotao = "Atualizar"
                        }) {
                            Text(text = "Atualizar")
                        }

                        Spacer(modifier = Modifier.height(15.dp)) }
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