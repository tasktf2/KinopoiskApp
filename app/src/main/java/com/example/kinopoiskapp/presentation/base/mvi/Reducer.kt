package com.example.kinopoiskapp.presentation.base.mvi

typealias Reducer<S, A> = (state: S, action: A) -> S