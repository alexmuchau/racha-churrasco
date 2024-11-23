package com.example.racha_churrasco.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Item(val nome: String, val valor: String, val responsavel: String)

class RoomDetailsViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> get() = _items

    fun loadItems(roomName: String) {
        viewModelScope.launch {
            // Simulação de carregamento de dados (pode substituir por chamada de API ou BD)
            delay(1000) // Simula tempo de carregamento
            _items.value = listOf(
                Item("Alcatra", "R$100", "Gabriel"),
                Item("Carvão", "R$50", "João"),
                Item("Linguiça", "R$60", "Pedro")
            )
        }
    }
}
