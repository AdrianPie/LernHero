package com.lernhero.domain.preset

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val character: Character?
)

