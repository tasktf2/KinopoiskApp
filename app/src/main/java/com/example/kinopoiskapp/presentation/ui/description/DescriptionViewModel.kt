package com.example.kinopoiskapp.presentation.ui.description

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.kinopoiskapp.domain.usecase.GetMovieInfoByIdUseCase
import com.example.kinopoiskapp.presentation.base.mvi.BaseEffect
import com.example.kinopoiskapp.presentation.base.mvi.BaseViewModel
import com.example.kinopoiskapp.presentation.base.mvi.Reducer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.awaitFirst

class DescriptionViewModel(
    override val initialState: DescriptionState,
    private val getMovieInfoByIdUseCase: GetMovieInfoByIdUseCase
) : BaseViewModel<DescriptionAction, DescriptionState, BaseEffect>() {

    private val errorInternet by lazy { Exception("Произошла ошибка при загрузке данных, проверьте подключение к сети") }

    private val reducer: Reducer<DescriptionState, DescriptionAction> = { state, action ->
        when (action) {
            is DescriptionAction.ShowLoading -> state.copy(isLoading = true)
            is DescriptionAction.ShowDescription -> state.copy(
                movieDescriptionUI = action.movieDescriptionUI,
                isLoading = false
            )
            is DescriptionAction.ShowError -> state.copy(isLoading = false, error = action.error)
            else -> state
        }
    }

    init {
        bindActionGetMovieInfoById()
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .catch { Log.d(TAG, it.stackTraceToString()) }
            .onEach(states::emit)
            .launchIn(viewModelScope)
    }

    private fun bindActionGetMovieInfoById(): Flow<DescriptionAction> =
        actions.filterIsInstance<DescriptionAction.LoadDescription>()
            .map {
                DescriptionAction.ShowDescription(
                    getMovieInfoByIdUseCase.execute(it.movieId).awaitFirst()
                )
            }
            .catch { DescriptionAction.ShowError(errorInternet) }
            .onStart { DescriptionAction.ShowLoading }

    companion object {
        private const val TAG = "DescriptionViewModel"
    }
}