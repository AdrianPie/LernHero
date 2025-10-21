package com.lernhero.auth

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes.Companion.Diamond
import androidx.compose.material3.MaterialShapes.Companion.Sunny
import androidx.compose.material3.Shapes
import androidx.compose.material3.toPath
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lernhero.data.domain.PlayerRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class AuthViewModel(
    private val playerRepository: PlayerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthStateUi())
    val uiState: StateFlow<AuthStateUi> = _uiState

    fun toggleAnimate() {
        _uiState.value = _uiState.value.copy(animate = !uiState.value.animate)
    }



    fun createPlayer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.createPlayer(
                user = user,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    fun CreateCharacter(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){

    }
}