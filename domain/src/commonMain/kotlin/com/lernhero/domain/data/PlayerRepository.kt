package com.lernhero.domain.data

import com.lernhero.domain.preset.Character
import dev.gitlive.firebase.auth.FirebaseUser

interface PlayerRepository {
    fun getUserId(): String?
    suspend fun createPlayer(
        user: FirebaseUser?,
        name: String,
        character: Character?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}