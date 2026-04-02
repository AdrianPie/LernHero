package com.lernhero.auth

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lernhero.domain.data.PlayerRepository
import com.lernhero.domain.preset.Character
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class AuthViewModel(
    private val playerRepository: PlayerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthStateUi())
    val uiState: StateFlow<AuthStateUi> = _uiState

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()


    fun toggleAnimate() {
        _uiState.value = _uiState.value.copy(animate = !uiState.value.animate)
    }

    fun setUser(newUser: FirebaseUser?) {
        _user.value = newUser
    }


    fun createPlayer(
        user: FirebaseUser?,
        character: Character,
        name: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.createPlayer(
                user = user,
                onSuccess = onSuccess,
                name = name,
                onFailure = onFailure,
                character = character
            )
        }
    }

}