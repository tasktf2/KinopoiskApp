package com.example.kinopoiskapp.presentation.base.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<A : BaseAction, S : BaseState, E : BaseEffect> : ViewModel() {
    protected abstract val initialState: S

    protected val actions: MutableSharedFlow<A> = MutableSharedFlow()

    protected val states: MutableSharedFlow<S> = MutableSharedFlow()

    protected val sideEffects: MutableSharedFlow<E> = MutableSharedFlow()

    val observableStates: StateFlow<S> by lazy {
        states.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState
        )
    }

    val observableSideEffects: Flow<E>
        get() = sideEffects.onEach { Log.d(TAG, "Received effect: $it") }

    fun dispatch(action: A) {
        viewModelScope.launch { actions.emit(action) }
    }

    companion object {
        private const val TAG = "ViewModel"
    }
}