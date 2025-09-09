package com.lernhero.data.domain

import dev.gitlive.firebase.auth.FirebaseUser

interface PlayerRepository {
    suspend fun createPlayer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}