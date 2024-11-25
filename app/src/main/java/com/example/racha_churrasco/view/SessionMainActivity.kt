package com.example.racha_churrasco.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Item
import com.example.racha_churrasco.models.User
import kotlinx.coroutines.launch

class SessionMainActivity : ComponentActivity() {

    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>
    private val itemDao by lazy { RachaDatabase.getDatabase(this).itemDao() }
    private val userDao by lazy { RachaDatabase.getDatabase(this).userDao() }
    private var sessionId: Int = -1
    private var sessionName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionId = intent.getIntExtra("sessionId", -1)
        sessionName = intent.getStringExtra("sessionName") ?: "Sessão"

        // Registrar o launcher para AddItemActivity
        addItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Recarregar itens ao retornar
                loadItems()
            }
        }

        setContent {
            SessionMainScreen()
        }
    }

    @Composable
    fun SessionMainScreen() {
        var items by remember { mutableStateOf(emptyList<Item>()) }
        var users by remember { mutableStateOf(emptyList<User>()) }
        var balances by remember { mutableStateOf(emptyList<String>()) }

        // Atualiza a interface com os dados
        LaunchedEffect(Unit) {
            loadItems { loadedItems ->
                items = loadedItems
                calculateBalances(loadedItems, users) { calculatedBalances ->
                    balances = calculatedBalances
                }
            }
            loadUsers { loadedUsers ->
                users = loadedUsers
                calculateBalances(items, loadedUsers) { calculatedBalances ->
                    balances = calculatedBalances
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nome da Sessão
            Text(text = "Sessão: $sessionName", fontSize = 24.sp)

            // Botão para adicionar itens
            Button(
                onClick = {
                    val intent = Intent(this@SessionMainActivity, ItemActivity::class.java).apply {
                        putExtra("sessionId", sessionId)
                    }
                    addItemLauncher.launch(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Item")
            }

            // Exibição dos Itens
            Text(text = "Itens Trazidos", fontSize = 20.sp)
            LazyColumn(modifier = Modifier.fillMaxHeight(0.3f)) {
                if (items.isEmpty()) {
                    item {
                        Text("Nenhum item adicionado ainda.")
                    }
                } else {
                    items(items) { item ->
                        Text(
                            text = "${item.name} - R$${"%.2f".format(item.price)} - responsável: ${item.responsible}"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exibição dos Participantes
            Text(text = "Participantes", fontSize = 20.sp)
            LazyColumn(modifier = Modifier.fillMaxHeight(0.2f)) {
                if (users.isEmpty()) {
                    item {
                        Text("Nenhum participante encontrado.")
                    }
                } else {
                    items(users) { user ->
                        Text(text = user.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exibição dos Saldos
            Text(text = "Saldos", fontSize = 20.sp)
            LazyColumn(modifier = Modifier.fillMaxHeight(0.3f)) {
                if (balances.isEmpty()) {
                    item {
                        Text("Saldos ainda não calculados.")
                    }
                } else {
                    items(balances) { balance ->
                        Text(text = balance)
                    }
                }
            }
        }
    }

    private fun loadItems(callback: (List<Item>) -> Unit = {}) {
        lifecycleScope.launch {
            val loadedItems = itemDao.getItemsBySession(sessionId)
            callback(loadedItems)
        }
    }

    private fun loadUsers(callback: (List<User>) -> Unit = {}) {
        lifecycleScope.launch {
            val loadedUsers = userDao.getUsersBySession(sessionId)
            callback(loadedUsers)
        }
    }

    private fun calculateBalances(
        items: List<Item>,
        users: List<User>,
        callback: (List<String>) -> Unit
    ) {
        lifecycleScope.launch {
            // Mapear os gastos totais por responsável
            val expensesByUser = items.groupBy { it.responsible }.mapValues { entry ->
                entry.value.sumOf { it.price }
            }

            // Total de gastos da sessão
            val totalExpenses = expensesByUser.values.sum()

            // Valor ideal que cada um deveria gastar
            val equalShare = if (users.isNotEmpty()) totalExpenses / users.size else 0.0

            // Calculando o saldo de cada participante
            val balances = users.associateWith { user ->
                expensesByUser[user.name] ?: 0.0 - equalShare
            }

            // Criar a lista detalhada de saldos
            val results = mutableListOf<String>()
            balances.forEach { (user, balance) ->
                if (balance > 0) {
                    val debtors = balances.filter { it.value < 0 }
                    debtors.forEach { (debtor, debtAmount) ->
                        val amount = minOf(balance, -debtAmount)
                        if (amount > 0) {
                            results.add("${debtor.name} deve pagar R$${"%.2f".format(amount)} para ${user.name}")
                        }
                    }
                }
            }

            callback(results)
        }
    }
}
