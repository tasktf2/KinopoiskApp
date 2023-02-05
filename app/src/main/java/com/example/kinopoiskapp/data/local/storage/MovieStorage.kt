package com.example.kinopoiskapp.data.local.storage

import com.example.kinopoiskapp.data.local.db.dao.MovieDao
import com.example.kinopoiskapp.data.local.model.MovieEntity
import io.reactivex.rxjava3.core.Single

class MovieStorage(private val movieDao: MovieDao) {

    fun insertFavoriteMovie(movie: MovieEntity): Single<Unit> = movieDao.insertFavoriteMovie(movie)

    fun deleteFavoriteMovie(movieEntity: MovieEntity): Single<Unit> = movieDao.deleteFavoriteMovie(movieEntity)

    fun getMovies(): Single<List<MovieEntity>> = movieDao.getMovies()
}