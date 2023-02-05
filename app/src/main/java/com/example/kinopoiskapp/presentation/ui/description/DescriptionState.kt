package com.example.kinopoiskapp.presentation.ui.description

import com.example.kinopoiskapp.presentation.base.mvi.BaseState

data class DescriptionState(
    val movieDescriptionUI: MovieDescriptionUI? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) : BaseState

data class MovieDescriptionUI(
    val movieName: String,
    val movieYear: String,
    val movieCountries: List<String>,
    val movieGenres: List<String>,
    val moviePosterUrl: String,
    val movieDescription: String
)