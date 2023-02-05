package com.example.kinopoiskapp.presentation.ui.description

import com.example.kinopoiskapp.presentation.base.mvi.BaseAction

sealed class DescriptionAction : BaseAction {

    object ShowLoading : DescriptionAction()

    data class LoadDescription(val movieId: Int) : DescriptionAction()

    data class ShowDescription(val movieDescriptionUI: MovieDescriptionUI) : DescriptionAction()

    data class ShowError(val error: Throwable) : DescriptionAction()
}