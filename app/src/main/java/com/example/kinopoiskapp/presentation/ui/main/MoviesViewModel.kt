package com.example.kinopoiskapp.presentation.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.kinopoiskapp.domain.usecase.DeleteFavoriteMovieUseCase
import com.example.kinopoiskapp.domain.usecase.GetFavoriteMoviesUseCase
import com.example.kinopoiskapp.domain.usecase.GetPopularMoviesUseCase
import com.example.kinopoiskapp.domain.usecase.InsertFavoriteMovieUseCase
import com.example.kinopoiskapp.presentation.base.mvi.BaseViewModel
import com.example.kinopoiskapp.presentation.base.mvi.Reducer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await

class MoviesViewModel(
    override val initialState: MoviesState,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase

) : BaseViewModel<MoviesAction, MoviesState, MoviesEffect>() {

    private val errorInternet by lazy { Exception("Произошла ошибка при загрузке данных, проверьте подключение к сети") }
    private val errorFavorite by lazy { Exception("Не найдено избранных фильмов") }

    private val reducer: Reducer<MoviesState, MoviesAction> = { state, action ->
        when (action) {
            is MoviesAction.ShowLoading -> state.copy(isLoading = true, error = null)
            is MoviesAction.ShowMovies -> state.copy(
                movies = action.movies,
                isLoading = false,
                error = null
            )
            is MoviesAction.ShowError -> state.copy(isLoading = false, error = action.error)
            is MoviesAction.LoadDescription -> {
                viewModelScope.launch { sideEffects.emit(MoviesEffect.ShowDescription(action.movieId)) }
                state
            }
            is MoviesAction.ShowSearchResults -> state.copy(visibleMovies = action.movies)
            else -> state
        }
    }

    init {
        listOf(
            bindActionShowSearchResults(),
            bindActionShowError(),
            bindActionGetPopularMovies(),
            bindActionLoadDescription(),
            bindActionInsertFavoriteMovie(),
            bindActionDeleteFavoriteMovie(),
            bindActionGetFavoriteMovies()
        ).merge()
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .catch { Log.d(TAG, it.stackTraceToString()) }
            .onEach(states::emit)
            .launchIn(viewModelScope)
    }

    private fun bindActionShowError(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.ShowError>()

    private fun bindActionGetPopularMovies(): Flow<MoviesAction> {

        return actions.filterIsInstance<MoviesAction.LoadPopularMovies>()
            .map<MoviesAction, MoviesAction> {
                MoviesAction.ShowMovies(
                    getPopularMoviesUseCase.execute()
                        .flatMap { remote ->
                            Single.zip(
                                Single.just(remote),
                                getFavoriteMoviesUseCase.execute()
                            ) { remoteList, local ->
                                addFavoriteMovies(remoteList, local)
                            }
                        }.await()
                )
            }.catch { MoviesAction.ShowError(errorInternet) }
            .onStart { emit(MoviesAction.ShowLoading) }
    }

    private fun bindActionGetFavoriteMovies(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.LoadFavoriteMovies>()
            .map {
                MoviesAction.ShowMovies(getFavoriteMoviesUseCase.execute().await())
            }.map {
                when {
                    it.movies.isEmpty() -> {
                        MoviesAction.ShowError(errorFavorite)
                    }
                    else -> it
                }
            }
            .catch { MoviesAction.ShowError(errorInternet) }
            .onStart { emit(MoviesAction.ShowLoading) }

    private fun bindActionLoadDescription(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.LoadDescription>()

    private fun bindActionInsertFavoriteMovie(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.InsertFavoriteMovie>().map { action ->
            insertFavoriteMovieUseCase.execute(action.movie).await()
            MoviesAction.FavoriteMovieToggled
        }

    private fun bindActionDeleteFavoriteMovie(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.DeleteFavoriteMovie>().map { action ->
            deleteFavoriteMovieUseCase.execute(action.movie).await()
            MoviesAction.FavoriteMovieToggled
        }

    private fun bindActionShowSearchResults(): Flow<MoviesAction> =
        actions.filterIsInstance<MoviesAction.Search>()
            .distinctUntilChanged()
            .map { action -> MoviesAction.ShowSearchResults(
                    if (action.query.isNotEmpty()) {
                        states.last().movies.orEmpty()
                            .filter { item ->
                                item.movieName.contains(action.query, ignoreCase = true)||
                                item.movieGenres.contains(action.query, ignoreCase = true)||
                                item.movieYear.contains(action.query, ignoreCase = true)
                            }
                    } else {
                        states.last().movies.orEmpty()
                    }
                )
            }

    private fun addFavoriteMovies(
        remote: List<MovieItemUI>,
        local: List<MovieItemUI>
    ): List<MovieItemUI> = remote.map { remoteItem ->
        when {
            local.isEmpty() -> remoteItem.copy(isFavorite = false)
            local.any { it.movieId == remoteItem.movieId } -> remoteItem.copy(isFavorite = true)
            else -> remoteItem
        }
    }

    companion object {
        private const val TAG = "MoviesViewModel"
    }
}
