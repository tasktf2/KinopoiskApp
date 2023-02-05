package com.example.kinopoiskapp.domain.repo

import com.example.kinopoiskapp.data.remote.response.GetMovieInfoByIdResponse
import com.example.kinopoiskapp.domain.model.MovieDomain
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface MovieRepo {
    fun getRemotePopularMovies(): Single<List<MovieDomain>>

    fun getMovieInfoById(id: Int): Observable<GetMovieInfoByIdResponse>

    fun getFavoriteMovies(): Single<List<MovieDomain>>

    fun insertFavoriteMovie(movie: MovieDomain): Single<Unit>

    fun deleteFavoriteMovie(movie: MovieDomain): Single<Unit>
}