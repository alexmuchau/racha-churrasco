package com.example.racha_churrasco.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.models.Item

class ItemViewModel : ViewModel() {

    var listaItems = mutableStateOf(listOf<Item>())
        private set

    private var idCounter = 1 // Variável para garantir IDs únicos

    fun salvarItem(nomeItem: String, valorItem: String): String {
        // Tenta converter valorItem para Int
        val valorItem = valorItem.toDouble() ?: return "O valor deve ser um número inteiro válido!"

        // Se algum dos dados estiver em branco ou inválido
        if (nomeItem.isBlank() || valorItem <= 0.0) {
            return "Preencha todos os campos corretamente!"
        }

        // Cria o item com um ID único
        val item = Item(
            id_item = idCounter++,
            name = nomeItem,
            price = valorItem, sessionId = 0, responsible = "0"
        )

        listaItems.value = listaItems.value + item
        return "Item salvo com sucesso!"
    }

    fun excluirItem(id: Int) {
        listaItems.value = listaItems.value.filter { it.id_item != id }
    }

    fun atualizarItem(id: Int, nomeItem: String, valorItem: String): String {
        // Tenta converter valorItem para Int
        val valorItemInt = valorItem.toIntOrNull() ?: return "O valor deve ser um número inteiro válido!"

        if (nomeItem.isBlank() || valorItemInt <= 0) {
            return "Preencha todos os campos corretamente!"
        }

        // Atualiza o item na lista
        val itemAtualizado = listaItems.value.find { it.id_item == id }
            ?: return "Erro ao atualizar item"

        listaItems.value = listaItems.value.map {
            if (it.id_item == id) {
                it.copy(name = nomeItem, price = valorItem.toDouble())
            } else it
        }

        return "Item atualizado com sucesso!"
    }
}