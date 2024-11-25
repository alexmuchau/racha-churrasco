package com.example.racha_churrasco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.racha_churrasco.daos.ItemDao
import com.example.racha_churrasco.daos.UserDao
import com.example.racha_churrasco.daos.SessionDao
import com.example.racha_churrasco.models.Item
import com.example.racha_churrasco.models.User
import com.example.racha_churrasco.models.Session

@Database(
    entities = [Session::class, Item::class, User::class],
    version = 4,
    exportSchema = false
)
abstract class RachaDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun itemDao(): ItemDao
    abstract fun userDao(): UserDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
