package com.example.kinopoiskapp.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoiskapp.domain.usecase.DeleteFavoriteMovieUseCase
import com.example.kinopoiskapp.domain.usecase.GetFavoriteMoviesUseCase
import com.example.kinopoiskapp.domain.usecase.GetPopularMoviesUseCase
import com.example.kinopoiskapp.domain.usecase.InsertFavoriteMovieUseCase

class MoviesViewModelFactory(
    private val initialState: MoviesState,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MoviesViewModel(
            initialState,
            getPopularMoviesUseCase,
            getFavoriteMoviesUseCase,
            insertFavoriteMovieUseCase,
            deleteFavoriteMovieUseCase
        ) as T
}