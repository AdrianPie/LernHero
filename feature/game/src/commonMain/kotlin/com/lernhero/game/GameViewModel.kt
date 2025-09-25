package com.lernhero.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameStateUi())
    val uiState: StateFlow<GameStateUi> = _uiState

    fun changeEffectVisibility(visibility: Boolean) {
        _uiState.value = _uiState.value.copy(visibility)
    }
}