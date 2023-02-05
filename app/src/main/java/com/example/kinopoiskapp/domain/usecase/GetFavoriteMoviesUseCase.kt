package com.example.kinopoiskapp.domain.usecase

import com.example.kinopoiskapp.domain.model.MovieDomain
import com.example.kinopoiskapp.domain.repo.MovieRepo
import com.example.kinopoiskapp.presentation.ui.main.MovieItemUI
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

class GetFavoriteMoviesUseCase(private val repo: MovieRepo, private val scheduler: Scheduler) {

    fun execute(): Single<List<MovieItemUI>> =
        repo.getFavoriteMovies().subscribeOn(scheduler).map { moviesRemote ->
            moviesRemote.map(::toPresentation)
        }

    private fun toPresentation(model: MovieDomain): MovieItemUI = MovieItemUI(
        movieId = model.movieId,
        movieName = model.movieName,
        movieYear = model.movieYear,
        moviePreviewUrl = model.moviePreviewUrl,
        movieGenres = model.movieGenres,
        isFavorite = model.isFavorite
    )
}