package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.media.metrics.LogSessionId
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.racha_churrasco.daos.ItemWithResponsible
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Item

class ItemViewModel(private val context: Context) : ViewModel() {
    val itemDao = RachaDatabase.getDatabase(context).itemDao()

    suspend fun salvarItem(nomeItem: String, valorItem: Double, responsable:Int, sessionId: Int): String {
        if (nomeItem.isBlank() || valorItem <= 0.0) {
            return "Preencha todos os campos corretamente!"
        }

        val item = Item(
            name = nomeItem,
            price = valorItem,
            sessionId = sessionId,
            responsible = responsable
        )

        itemDao.insert(item)

        return "Item salvo com sucesso!"
    }

    suspend fun updateItem(item_id: Int, newName: String, newPrice: Double) {
        itemDao.update(item_id, newName, newPrice)
    }

    suspend fun deleteItem(item_id: Int) {
        itemDao.delete(item_id)
    }

    suspend fun getItemsByResponsible(responsible: Int): List<ItemWithResponsible> {
        return itemDao.getItemsByResponsible(responsible)
    }

//    fun excluirItem(id: Int) {
//        listaItems.value = listaItems.value.filter { it.id_item != id }
//    }
//
//    fun atualizarItem(id: Int, nomeItem: String, valorItem: String): String {
//        val valorItemInt = valorItem.toDouble()
//
//        if (nomeItem.isBlank() || valorItemInt <= 0) {
//            return "Preencha todos os campos corretamente!"
//        }
//
//        // Atualiza o item na lista
//        val itemAtualizado = listaItems.value.find { it.id_item == id }
//            ?: return "Erro ao atualizar item"
//
//        listaItems.value = listaItems.value.map {
//            if (it.id_item == id) {
//                it.copy(name = nomeItem, price = valorItem.toDouble())
//            } else it
//        }
//
//        return "Item atualizado com sucesso!"
//    }
}