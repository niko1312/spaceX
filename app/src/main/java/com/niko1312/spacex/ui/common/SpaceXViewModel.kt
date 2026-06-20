package com.niko1312.spacex.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class SpaceXViewModel<UiState, UiIntent, UiEffect> : ViewModel() {

    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect: Flow<UiEffect> = _effect.receiveAsFlow()

    protected val currentState: UiState get() = _state.value

    abstract fun initialState(): UiState

    abstract fun onIntent(intent: UiIntent)

    protected fun setState(reducer: UiState.() -> UiState) {
        _state.update(reducer)
    }

    protected fun sendEffect(effect: UiEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
