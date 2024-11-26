package com.example.racha_churrasco.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.racha_churrasco.daos.ItemWithResponsible
import com.example.racha_churrasco.database.RachaDatabase
import com.example.racha_churrasco.models.Item
import com.example.racha_churrasco.models.Session
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.view.CadastroSessionActivity
import com.example.racha_churrasco.view.SessionMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionMainViewModel(private val context: Context) : ViewModel() {
    private val database = RachaDatabase.getDatabase(context)
    private val sessionDao = database.sessionDao()
    private val userDao = database.userDao()
    private val itemsDao = database.itemDao()

    suspend fun getItems(activeSessionId: Int): List<ItemWithResponsible> {
        return withContext(Dispatchers.IO) {
            val items = itemsDao.getItemsBySession(activeSessionId)
            items
        }
    }

    suspend fun getSession(activeSessionId: Int): Session? {
        val session = sessionDao.getSessionById(activeSessionId)
        return session
    }

    fun calculateBalances(
        items: List<ItemWithResponsible>
    ) : List<String> {
        // Mapear os gastos totais por responsável
        val totalSpentByUser = items.groupBy { it.userName }.mapValues { entry ->
            entry.value.sumOf { it.price }
        }

        // Calcular o total gasto por todos os itens
        val totalSpent = items.sumOf { it.price }

        // Calcular a média de gastos por pessoa
        val averageSpent = totalSpent / totalSpentByUser.size

        // Calcular quanto cada pessoa deve ou tem a receber
        val balances = totalSpentByUser.map { (userName, totalSpentByUser) ->
            val balance = totalSpentByUser - averageSpent
            if (balance > 0) {
                "$userName deve receber R$${"%.2f".format(balance)}"
            } else {
                "$userName deve pagar R$${"%.2f".format(-balance)}"
            }
        }

        return balances
    }

    suspend fun loadUsers(sessionId: Int): List<User> {
        val loadedUsers = userDao.getUsersBySession(sessionId)
        return loadedUsers
    }
}
