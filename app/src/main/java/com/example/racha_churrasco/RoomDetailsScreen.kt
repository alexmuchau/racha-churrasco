package com.example.racha_churrasco
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.racha_churrasco.viewModels.RoomDetailsViewModel
//
//@Composable
//fun RoomDetailsScreen(roomName: String, viewModel: RoomDetailsViewModel) {
//    val items by viewModel.items.collectAsState()
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text(
//            text = roomName,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn {
//            items(items) { item ->
//                ItemCard(item)
//            }
//        }
//    }
//}
//
//@Composable
//fun ItemCard(item: Item) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        elevation = 4.dp
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "Item: ${item.nome}", style = MaterialTheme.typography.bodyLarge)
//            Text(text = "Valor: ${item.valor}", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "Responsável: ${item.responsavel}", style = MaterialTheme.typography.bodySmall)
//        }
//    }
//}
