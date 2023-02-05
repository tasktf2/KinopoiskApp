package com.example.kinopoiskapp.presentation.ui.main

import com.example.kinopoiskapp.presentation.base.mvi.BaseAction
import com.example.kinopoiskapp.presentation.base.mvi.BaseEffect

sealed class MoviesAction : BaseAction {

    object ShowLoading : MoviesAction()

    object LoadPopularMovies : MoviesAction()

    object LoadFavoriteMovies : MoviesAction()

    data class ShowMovies(val movies: List<MovieItemUI>) : MoviesAction()

    data class ShowError(val error: Throwable) : MoviesAction()

    data class LoadDescription(val movieId: Int) : MoviesAction()

    data class InsertFavoriteMovie(val movie: MovieItemUI) : MoviesAction()

    object FavoriteMovieToggled : MoviesAction()

    data class DeleteFavoriteMovie(val movie: MovieItemUI) : MoviesAction()

    data class Search(val query: String) : MoviesAction()

    data class ShowSearchResults(val movies: List<MovieItemUI>) : MoviesAction()
}

sealed class MoviesEffect : BaseEffect {

    data class ShowDescription(val movieId: Int) : MoviesEffect()
}
