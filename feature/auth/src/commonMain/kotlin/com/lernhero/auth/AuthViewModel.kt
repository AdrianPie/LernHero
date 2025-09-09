package com.lernhero.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lernhero.data.domain.PlayerRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    private val playerRepository: PlayerRepository
): ViewModel() {
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
}