package com.lernhero.game.Character

import org.jetbrains.compose.resources.DrawableResource

data class CharacterUiModel(
    val id: String,
    val hp: Int,
    val maxHp: Int,
    val attack: Int,
    val mana: Int,
    val isPlayer: Boolean,
    val avatarRes: DrawableResource, // GOTOWY OBRAZEK
    val armorRes: DrawableResource   // GOTOWA RAMKA
)
