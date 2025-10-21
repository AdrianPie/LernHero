package com.lernhero.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id : String = "",
    val isPlayer: Boolean = false,
    var hp: Int = 0,
    var mana: Int = 0,
    val maxMana: Int = 0,
    val maxHp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val speed: Int = 0,
)