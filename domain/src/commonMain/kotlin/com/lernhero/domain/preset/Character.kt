package com.lernhero.domain.preset

import kotlinx.serialization.Serializable





@Serializable
data class Character(
    val id : String = "",
    val isPlayer: Boolean = false,
    val avatar: String = "",
    val armor: String = "",
    val hp: Int = 0,
    val mana: Int = 0,
    val maxMana: Int = 0,
    val maxHp: Int = 0,
    val attack: Int = 0,
)