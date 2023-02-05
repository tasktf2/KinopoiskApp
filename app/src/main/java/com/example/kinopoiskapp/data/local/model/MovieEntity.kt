package com.example.kinopoiskapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "movie_name")
    val movieName: String,
    @ColumnInfo(name = "movie_year")
    val movieYear: String,
    @ColumnInfo(name = "preview_url")
    val moviePreviewUrl: String,
    @ColumnInfo(name = "genres")
    val movieGenres: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)