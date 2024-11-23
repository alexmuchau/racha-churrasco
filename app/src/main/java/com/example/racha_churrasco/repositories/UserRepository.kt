package com.example.racha_churrasco.repositories

import com.example.racha_churrasco.daos.UserDao
import com.example.racha_churrasco.models.User

class UserRepository(private val userDao: UserDao) {

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}
