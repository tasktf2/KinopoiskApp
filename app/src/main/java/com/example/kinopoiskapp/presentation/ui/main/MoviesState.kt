package com.example.kinopoiskapp.presentation.ui.main

import com.example.kinopoiskapp.presentation.base.mvi.BaseState

data class MoviesState(
    val movies: List<MovieItemUI>? = null,
    val visibleMovies: List<MovieItemUI>? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : BaseState

data class MovieItemUI(
    val movieId: Int,
    val movieName: String,
    val movieYear: String,
    val movieGenres: String,
    val moviePreviewUrl: String,
    val isFavorite: Boolean = false
)
