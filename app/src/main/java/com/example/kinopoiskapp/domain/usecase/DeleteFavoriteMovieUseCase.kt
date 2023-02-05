package com.example.kinopoiskapp.domain.usecase

import com.example.kinopoiskapp.domain.model.MovieDomain
import com.example.kinopoiskapp.domain.repo.MovieRepo
import com.example.kinopoiskapp.presentation.ui.main.MovieItemUI
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

class DeleteFavoriteMovieUseCase(private val repo: MovieRepo, private val scheduler: Scheduler) {

    fun execute(movie: MovieItemUI): Single<Unit> =
        repo.deleteFavoriteMovie(toDomain(movie)).subscribeOn(scheduler)

    private fun toDomain(movie: MovieItemUI): MovieDomain = MovieDomain(
        movieId = movie.movieId,
        movieName = movie.movieName,
        movieYear = movie.movieYear,
        moviePreviewUrl = movie.moviePreviewUrl,
        movieGenres = movie.movieGenres,
        isFavorite = movie.isFavorite
    )
}