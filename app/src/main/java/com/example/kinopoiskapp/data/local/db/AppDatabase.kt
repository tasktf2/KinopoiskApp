package com.example.kinopoiskapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kinopoiskapp.data.local.db.dao.MovieDao
import com.example.kinopoiskapp.data.local.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}