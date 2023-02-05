package com.example.kinopoiskapp.domain.model

data class MovieDomain(
    val movieId: Int,
    val movieName: String,
    val movieYear: String,
    val moviePreviewUrl: String,
    val movieGenres: String,
    val isFavorite: Boolean
)