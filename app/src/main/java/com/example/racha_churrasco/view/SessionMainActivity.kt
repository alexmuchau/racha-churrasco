package com.example.racha_churrasco.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.racha_churrasco.components.CustomTitle
import com.example.racha_churrasco.daos.ItemWithResponsible
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Item
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.viewmodels.LoginViewModel
import com.example.racha_churrasco.viewmodels.SessionMainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

class SessionMainActivity : ComponentActivity() {

    private lateinit var addItemLauncher: ActivityResultLauncher<Intent>
    private val itemDao by lazy { RachaDatabase.getDatabase(this).itemDao() }
    private val userDao by lazy { RachaDatabase.getDatabase(this).userDao() }
    private val sessionDao by lazy { RachaDatabase.getDatabase(this).sessionDao() }
    private var sessionId: Int = -1
    private var activeUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionId = intent.getIntExtra("sessionId", -1)
        activeUserId = intent.getIntExtra("activeUserId", -1)

        addItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Recarregar os dados quando ItemActivity for finalizada
                lifecycleScope.launch {
                    val sessionMainViewModel = SessionMainViewModel(this@SessionMainActivity)
                    val items = sessionMainViewModel.getItems(sessionId)
                    val sessionName = sessionMainViewModel.getSession(sessionId)!!.name
                    val users = sessionMainViewModel.loadUsers(sessionId)
                    val balances = sessionMainViewModel.calculateBalances(items)

                    // Atualizar os estados na composição
                    setContent {
                        SessionMainScreen(
                            items = items,
                            sessionName = sessionName,
                            users = users,
                            balances = balances
                        )
                    }
                }
            }
        }

        setContent {
            SessionMainScreen()
        }
    }

    @Composable
    fun SessionMainScreen(
        items: List<ItemWithResponsible> = emptyList(),
        sessionName: String = "",
        users: List<User> = emptyList(),
        balances: List<String> = emptyList()
    ) {
        val context = LocalContext.current
        var sessionMainViewModel = SessionMainViewModel(context)

        var items by remember { mutableStateOf(items) }
        var users by remember { mutableStateOf(users) }
        var balances by remember { mutableStateOf(balances) }
        var sessionName by remember { mutableStateOf(sessionName) }

        var isRefreshing by remember { mutableStateOf(false) }
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                items = sessionMainViewModel.getItems(sessionId)
                sessionName = sessionMainViewModel.getSession(sessionId)!!.name
                Toast.makeText(context, "SessionName: $sessionName", Toast.LENGTH_SHORT).show()

                users = sessionMainViewModel.loadUsers(sessionId)
                balances = sessionMainViewModel.calculateBalances(items)
            }
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                lifecycleScope.launch {
                    isRefreshing = true
                    items = sessionMainViewModel.getItems(sessionId)
                    sessionName = sessionMainViewModel.getSession(sessionId)!!.name
                    Toast.makeText(context, "SessionName: $sessionName", Toast.LENGTH_SHORT).show()

                    users = sessionMainViewModel.loadUsers(sessionId)
                    balances = sessionMainViewModel.calculateBalances(items)
                    isRefreshing = false
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nome da Sessão
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CustomTitle(text = "$sessionName")
                    Button(
                        onClick = {
                            val intent = Intent(this@SessionMainActivity, ItemActivity::class.java).apply {
                                putExtra("sessionId", sessionId)
                                putExtra("activeUserId", activeUserId)
                            }
                            addItemLauncher.launch(intent)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("+", fontSize = 32.sp, textAlign = TextAlign.Center)
                        }
                    }
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
                                text = "${item.name} - R$${"%.2f".format(item.price)} - responsável: ${item.userName}"
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
    }
}
