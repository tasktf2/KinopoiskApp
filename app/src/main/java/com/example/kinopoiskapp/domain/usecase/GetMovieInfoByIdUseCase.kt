package com.example.kinopoiskapp.domain.usecase

import com.example.kinopoiskapp.data.remote.response.GetMovieInfoByIdResponse
import com.example.kinopoiskapp.data.remote.response.MovieCountry
import com.example.kinopoiskapp.data.remote.response.MovieGenre
import com.example.kinopoiskapp.domain.repo.MovieRepo
import com.example.kinopoiskapp.presentation.ui.description.MovieDescriptionUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler

class GetMovieInfoByIdUseCase(private val repo: MovieRepo, private val scheduler: Scheduler) {

    fun execute(id: Int): Observable<MovieDescriptionUI> =
        repo.getMovieInfoById(id).subscribeOn(scheduler).map(::mapToPresentation)
    private fun mapToPresentation(model: GetMovieInfoByIdResponse): MovieDescriptionUI =
        MovieDescriptionUI(
            movieName = model.movieName,
            movieYear = model.movieYear,
            moviePosterUrl = model.moviePosterUrl,
            movieCountries = model.movieCountries.map(MovieCountry::movieCountry),
            movieGenres = model.movieGenres.map(MovieGenre::movieGenre),
            movieDescription = model.movieDescription
        )
}