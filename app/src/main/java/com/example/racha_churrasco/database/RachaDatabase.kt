package com.example.racha_churrasco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.racha_churrasco.daos.SessionDao
import com.example.racha_churrasco.daos.UserDao
import com.example.racha_churrasco.models.Session
import com.example.racha_churrasco.models.User

@Database(entities = [User::class, Session::class], version = 2, exportSchema = false)
abstract class RachaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao // Adicione a nova DAO

    companion object {
        @Volatile
        private var INSTANCE: RachaDatabase? = null

        fun getDatabase(context: Context): RachaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RachaDatabase::class.java,
                    "racha_churrasco_db"
                )
                    .fallbackToDestructiveMigration() // Lida com alterações no esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}