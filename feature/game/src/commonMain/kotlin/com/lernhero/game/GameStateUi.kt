package com.lernhero.game


data class CharacterUiState(
    val effectActive: Boolean = false,
    val isAttacking: Boolean = false,
    val hp: Int = 0,
    val mana: Int = 0,
    val scale: Float = 1f
)

data class GameStateUi(
    val playerFirst: CharacterUiState = CharacterUiState(),
    val playerSecond: CharacterUiState = CharacterUiState(),

    val enemyFirst: CharacterUiState = CharacterUiState(),
    val enemySecond: CharacterUiState = CharacterUiState(),
    val enemyThird: CharacterUiState = CharacterUiState()
)
