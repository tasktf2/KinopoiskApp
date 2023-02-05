package com.example.kinopoiskapp.data.remote.api

import com.example.kinopoiskapp.data.remote.response.GetMovieInfoByIdResponse
import com.example.kinopoiskapp.data.remote.response.GetPopularMoviesResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("v2.2/films/top")
    fun getPopularMovies(@Query("type") type: String = "TOP_100_POPULAR_FILMS"): Single<GetPopularMoviesResponse>

    @GET("v2.2/films/{id}")
    fun getMovieInfoById(@Path("id") id: Int): Observable<GetMovieInfoByIdResponse>
}