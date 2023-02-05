package com.example.kinopoiskapp.data.local.db.dao

import androidx.room.*
import com.example.kinopoiskapp.data.local.model.MovieEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteMovie(movie: MovieEntity): Single<Unit>

    @Query("SELECT * FROM movie WHERE is_favorite = 1")
    fun getMovies(): Single<List<MovieEntity>>

    @Delete
    fun deleteFavoriteMovie(movie: MovieEntity): Single<Unit>
}