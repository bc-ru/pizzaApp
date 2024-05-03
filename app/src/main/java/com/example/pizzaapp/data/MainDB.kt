package com.example.pizzaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NameEntity::class,
    ],
    version = 1,
//    exportSchema = false
)
abstract class MainDB : RoomDatabase() {
    abstract val dao : Dao
    companion object {
        fun createDataBase(context: Context): MainDB {
            return Room.databaseBuilder(
                context,
                MainDB::class.java,
                "test.db"
            ).build()
        }

    }
}