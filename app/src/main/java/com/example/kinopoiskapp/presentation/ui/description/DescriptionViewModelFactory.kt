package com.example.kinopoiskapp.presentation.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoiskapp.domain.usecase.GetMovieInfoByIdUseCase

class DescriptionViewModelFactory(
    private val initialState: DescriptionState,
    private val getMovieInfoByIdUseCase: GetMovieInfoByIdUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        DescriptionViewModel(initialState, getMovieInfoByIdUseCase) as T
}